package com.example.rgt.CaseLine.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "Case_Group")
@Table(name = "case_group")
@Data
public class Case_Group {

    // EmbeddedId class to represent composite key   Here Case_id+user_id = primary key
    @EmbeddedId
    private CaseGroupId id;

    @Column(name = "group_id")
    private Integer groupId;

    @Enumerated(EnumType.STRING)
    private role role;

    public enum role {
        reporter,editor,admin // add all enum values
    }

}
