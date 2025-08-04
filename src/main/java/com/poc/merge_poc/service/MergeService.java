package com.poc.merge_poc.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.merge_poc.entity.ClientToken;
import com.poc.merge_poc.entity.models.EmployeeBinding;
import com.poc.merge_poc.entity.models.EmployeeEntity;
import com.poc.merge_poc.feigns.PocEmployee;
import com.poc.merge_poc.feigns.schema.EmployeeProfileUpdateRequest;
import com.poc.merge_poc.mapper.EmployeeMapper;
import com.poc.merge_poc.repository.ClientTokenRepository;
import com.poc.merge_poc.repository.EmployeeBindingRepository;
import com.poc.merge_poc.repository.EmployeeEntityRepository;
import com.poc.merge_poc.repository.LoggedRequestRepository;
import com.poc.merge_poc.rest.request.BindingRequest;
import com.poc.merge_poc.rest.request.CreateLinkToken;
import com.poc.merge_poc.rest.request.ExchangeAccessToken;
import com.poc.merge_poc.util.JwtTokenUtil;
import com.jayway.jsonpath.JsonPath;
import com.merge.api.MergeApiClient;
import com.merge.api.core.SyncPagingIterable;
import com.merge.api.hris.types.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class MergeService {

    private final MergeClientFactory mergeClientFactory;
    private final ClientTokenRepository clientTokenRepository;
    private final EmployeeEntityRepository employeeEntityRepository;
    private final EmployeeBindingRepository employeeBindingRepository;
    private final Map<String, MergeApiClient> clientCache = new ConcurrentHashMap<>();
    private final LoggedRequestRepository loggedRequestRepository;

    private final PocEmployee pocEmployee;

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    private final EmployeeMapper employeeMapper;

        public MergeService(MergeClientFactory mergeClientFactory, ClientTokenRepository clientTokenRepository, EmployeeEntityRepository employeeEntityRepository, EmployeeBindingRepository employeeBindingRepository, LoggedRequestRepository loggedRequestRepository, PocEmployee pocEmployee, EmployeeMapper employeeMapper, ObjectMapper objectMapper) {
        this.mergeClientFactory = mergeClientFactory;
        this.clientTokenRepository = clientTokenRepository;
        this.employeeEntityRepository = employeeEntityRepository;
        this.employeeBindingRepository = employeeBindingRepository;
        this.loggedRequestRepository = loggedRequestRepository;
        this.pocEmployee = pocEmployee;
        this.employeeMapper = employeeMapper;
    }


    public MergeApiClient getClient(String accountToken) {
        return clientCache.computeIfAbsent(accountToken,
                mergeClientFactory::createClient);
    }

    /**
     * Creates a link token for HRIS integration.
     *
     * @return The generated LinkToken object.
     */

    public LinkToken createLinkToken(CreateLinkToken createLinkToken) {
//
        ModelPermissionDeserializerRequest disable = ModelPermissionDeserializerRequest.builder().isEnabled(false).build();
        Map<String, ModelPermissionDeserializerRequest> readDisable = Map.of("READ", disable);
        IndividualCommonModelScopeDeserializerRequest disableDepedent = IndividualCommonModelScopeDeserializerRequest.builder()
                .modelName("Dependent")
                .modelPermissions(readDisable).build();
        Map<String, Optional<List<IndividualCommonModelScopeDeserializerRequest>>> hris = Map.of("hris", Optional.of(List.of(disableDepedent)));
//
        EndUserDetailsRequest req = EndUserDetailsRequest.builder()
                .endUserEmailAddress("sam.liu@joinhorizons.com")
                .endUserOrganizationName(createLinkToken.name())
                .endUserOriginId(createLinkToken.id())
                .addCategories(CategoriesEnum.HRIS)
//                .integration("hibob")
//                .categoryCommonModelScopes(hris)
                /*
        ApiError{message: Error with status code 400, statusCode: 400, body: {non_field_errors=[Linked account common model scopes update failed with message: Scope overrides in link not enabled for this organization]}}
                 */
                .build();
        return getClient("").hris().linkToken().create(req);
    }

    /**
     * use public token(temporary) to exchange access token(permanent)
     */
    public void exchangeAccessToken(ExchangeAccessToken exchangeAccessToken) {
        AccountToken accountToken = getClient("").hris().accountToken().retrieve(exchangeAccessToken.publicToken());
        String token = accountToken.getAccountToken();
        log.info("Retrieved account token: {}", accountToken.getAccountToken());
        // Store the account token for future use
        clientTokenRepository.save(new ClientToken(exchangeAccessToken.clientId(), exchangeAccessToken.clientName(), token));
    }


    @Value("${max.employee.size}")
    private Integer maxEmployeeSizeToGet;

    @Transactional
    public void getAndSaveEmployeeDataFromMerge(String clientId) {
        ClientToken clientToken = clientTokenRepository.findByClientId(clientId);
        String accessToken = clientToken.getAccessToken();
        log.info("Retrieved access token: {}", accessToken);
        EmployeesListRequestExpandItem[] expands = EmployeesListRequestExpandItem.values();
        SyncPagingIterable<Employee> employees = getClient(accessToken).hris().employees().list(EmployeesListRequest.builder()
//                .expand(Arrays.asList(EmployeesListRequestExpandItem.COMPANY, EmployeesListRequestExpandItem.EMPLOYMENTS))
                .expand(Arrays.asList(expands))
                .pageSize(20).build());
        ArrayList<EmployeeEntity> employeeEntities = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeEntity employeeEntity = employeeMapper.toEntity(employee, employee.toString(), clientId);
            employeeEntities.add(employeeEntity);
            if(maxEmployeeSizeToGet!=null && employeeEntities.size() ==maxEmployeeSizeToGet){
                break;
            }
        }
        employeeEntityRepository.saveAll(employeeEntities);
    }

    /**
     * get all integration employees under specific client
     *
     * @param clientId
     * @return
     */
    public List<EmployeeEntity> getEmployeeList(String clientId) {
        return employeeEntityRepository.fetchAllByClientId(clientId);
    }

    @Value("${client.admin.id}")
    private Long clientAdminId;

    public JSONArray getHorizonEmployeeList(String clientId) {
        String token = JwtTokenUtil.generateToken(clientAdminId, jwtSecretKey);
                String response = pocEmployee.getEmployees(false, token);
        JSONArray employees = JsonPath.read(response, "$.data.rows");
        return employees;
    }


    public void getEmployeeById(String id, String client_id) {
        ClientToken clientToken = clientTokenRepository.findByClientId(client_id);
        String accessToken = clientToken.getAccessToken();
        log.info("Retrieved access token: {}", accessToken);
        Employee employee = getClient(accessToken).hris().employees().retrieve(id);
        log.info("Retrieved employee: {}", employee);
    }

    /**
     * binding hris employee with horizons employee
     *
     * @param bindingRequest
     */
    public void bindingEmployee(BindingRequest bindingRequest) {
        Optional<EmployeeEntity> employeeEntityOptional = employeeEntityRepository.findById(bindingRequest.integrationEmployeeId());
        if (employeeEntityOptional.isPresent()) {
            EmployeeBinding employeeBinding = employeeBindingRepository.findByHorizonsEmployeeIdOrEmployeeEntity_Id(bindingRequest.horizonEmployeeId(), bindingRequest.integrationEmployeeId());
            if (employeeBinding == null) {
                employeeBinding = new EmployeeBinding();
            }
            employeeBinding.setEmployeeEntity(employeeEntityOptional.get());
            employeeBinding.setHorizonsEmployeeId(bindingRequest.horizonEmployeeId());
            employeeBinding.setHorizonsUserId(bindingRequest.horizonUserId());
            employeeBindingRepository.save(employeeBinding);
        }
    }


    public void updateEmployee() {
        employeeBindingRepository.findAll().forEach(this::updateRemoteEmployee);
    }

    @Value("${juno.admin.id}")
    private String junoAdminId;

    private void updateRemoteEmployee(EmployeeBinding employeeBinding) {
        String token = JwtTokenUtil.generateToken(Long.valueOf(junoAdminId), jwtSecretKey);
                String response = pocEmployee.getEmployeeProfileFields(employeeBinding.getHorizonsEmployeeId(), token);
        JSONArray nameSections = JsonPath.read(response, "$.data.rows[?(@.section == 'Identity')].values[?(@.group_name=='Name')]");
        Integer sectionId = JsonPath.read(nameSections.toJSONString(), "$[0].section_id");

        EmployeeEntity employeeEntity = employeeBinding.getEmployeeEntity();
        String firstName = employeeEntity.getFirstName();
        String lastName = employeeEntity.getLastName();
        String horizonsEmployeeId = employeeBinding.getHorizonsEmployeeId();
        EmployeeProfileUpdateRequest employeeProfileUpdateRequest = EmployeeProfileUpdateRequest.newEmployeeProfileUpdateRequest(horizonsEmployeeId, firstName, lastName, Long.valueOf(sectionId));
        log.info("update employee profile {}", employeeProfileUpdateRequest);
        pocEmployee.updateEmployeeProfile(horizonsEmployeeId, token, employeeProfileUpdateRequest);
    }



//    @Data
//    static class LinkedAccount {
//        private String id;
//        @JsonProperty("end_user_origin_id")
//        private String clientName;
//        @JsonProperty("end_user_origin_id")
//        private String clientId;
//    }

    @Transactional
    public void processEvents() {
        loggedRequestRepository.findByProcessedNullOrProcessedFalse().forEach(loggedRequest -> {
            loggedRequest.setProcessed(true);
            String body = loggedRequest.getBody();
            log.info("processing body: {}", body);
            String event = JsonPath.read(body, "$.hook['event']");
            log.info("processed event: {}", event);
            if(event.equals("LinkedAccount.sync_completed")){
                HashMap<String,String> linkedAccount = JsonPath.read(body, "$.linked_account");
                String clientId = linkedAccount.get("end_user_origin_id");
                log.info("get and save employee data from merge, clientId: {}", clientId);
                this.getAndSaveEmployeeDataFromMerge(clientId);
                log.info("sync complete");
            }
        });
    }

}
