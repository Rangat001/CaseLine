package com.example.rgt.CaseLine.DTO;

import com.example.rgt.CaseLine.entity.Case;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

public class CaseDetailsDTO {
    private Integer caseId;
    private Integer orgId;
    private String title;
    private Case.CaseType caseType;
    private Case.Status status;
    private String description;
    private Integer createdBy;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CaseDetailsDTO(Integer caseId, Integer orgId, String title, Case.CaseType caseType, Case.Status status, String description, Integer createdBy, String createdByName, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.caseId = caseId;
        this.orgId = orgId;
        this.title = title;
        this.caseType = caseType;
        this.status = status;
        this.description = description;
        this.createdBy = createdBy;
        this.createdByName = createdByName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

}
