package com.example.rgt.CaseLine.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "cases")  // use actual table name if different
public class Case {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer caseId;

    private Integer orgId;

    @Column(length = 300)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private CaseType caseType;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Status status;

    @Column(length = 5000)
    private String description;

    private Integer createdBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Enum for caseType
    public enum CaseType {
        criminal, political, legal, event, accident , scam // add all enum values
    }

    public enum Status {
        ongoing, closed, archived
    }

}
