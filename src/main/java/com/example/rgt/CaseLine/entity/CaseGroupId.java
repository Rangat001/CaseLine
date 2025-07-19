package com.example.rgt.CaseLine.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class CaseGroupId implements Serializable {
    private Integer caseId;
    private Integer userId;


    public CaseGroupId(Integer caseId, Integer userId) {
        this.caseId = caseId;
        this.userId = userId;
    }

    public CaseGroupId() {

    }

    // Getters, Setters, equals(), and hashCode()
    // Required for proper key behavior

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CaseGroupId)) return false;
        CaseGroupId that = (CaseGroupId) o;
        return Objects.equals(caseId, that.caseId) &&
                Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseId, userId);
    }

}
