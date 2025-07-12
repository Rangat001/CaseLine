package com.example.rgt.CaseLine.DTO;

public class CasePostCountDTO {
    private Integer caseId;
    private Long postCount;

    public CasePostCountDTO(Integer caseId, Long postCount) {
        this.caseId = caseId;
        this.postCount = postCount;
    }

    // Getters
    public Integer getCaseId() {
        return caseId;
    }

    public Long getPostCount() {
        return postCount;
    }
}
