package com.poc.merge_poc.rest;

import com.poc.merge_poc.entity.models.EmployeeEntity;
import com.poc.merge_poc.rest.request.BindingRequest;
import com.poc.merge_poc.rest.request.CreateLinkToken;
import com.poc.merge_poc.rest.request.ExchangeAccessToken;
import com.poc.merge_poc.service.MergeService;
import com.merge.api.hris.types.LinkToken;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/merge")
public class MergeController {

    private final MergeService mergeService;

    public MergeController(MergeService mergeService) {
        this.mergeService = mergeService;
    }

    @PostMapping("/link-token")
    public ResponseEntity<LinkToken> createLinkToken(@RequestBody CreateLinkToken createLinkToken) {
        try {
            LinkToken linkToken = mergeService.createLinkToken(createLinkToken);
            return ResponseEntity.ok(linkToken);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }


    @PostMapping("/exchange-access-token")
    public void exchangeAccessToken(@RequestBody ExchangeAccessToken exchangeAccessToken) {
        mergeService.exchangeAccessToken(exchangeAccessToken);
    }

    @GetMapping("/{client_id}/integration_employees")
    public List<EmployeeEntity> startFetchEmployee(@PathVariable("client_id") String clientId) {
        return mergeService.getEmployeeList(clientId);
    }

    @GetMapping("/{client_id}/horizon_employees")
    public JSONArray getHorizonEmployee(@PathVariable("client_id") String clientId) {
        return mergeService.getHorizonEmployeeList(clientId);
    }

    @PostMapping("/binding")
    public String binding(@RequestBody BindingRequest bindingRequest) {
        mergeService.bindingEmployee(bindingRequest);
        return "{\"status\":\"success\"}";
    }

    @PostMapping("/update")
    public String updateRemote() {
        mergeService.updateEmployee();
        return "{\"status\":\"success\"}";
    }

    @PostMapping("/process-events")
    public String processEvents() {
        mergeService.processEvents();
        return "{\"status\":\"success\"}";
    }
}