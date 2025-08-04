package com.poc.merge_poc.entity.models.base;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@MappedSuperclass
@Data
public class MergeBaseEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "remote_id")
    private String remoteId;

    @Column(name = "created_at")
    private OffsetDateTime CreatedAt;

    @Column(name = "modified_at")
    private OffsetDateTime modifiedAt;

    @Column(name = "remote_was_deleted")
    private boolean remoteWasDeleted;

//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(clientName = "field_mappings", columnDefinition = "json")
//    private FieldMapping fieldMappings;
//
//    @Column(clientName = "remote_data", columnDefinition = "json")
//    @JdbcTypeCode(SqlTypes.JSON)
//    private List<RemoteData> remoteData;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "field_mappings", columnDefinition = "json")
    private Map<String, JsonNode> fieldMappings;

    @Column(name = "remote_data", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<com.merge.api.hris.types.RemoteData> remoteData;

    @Column(name = "raw_data", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private String rawData;

}
