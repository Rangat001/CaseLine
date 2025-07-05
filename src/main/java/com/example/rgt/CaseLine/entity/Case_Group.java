package com.example.rgt.CaseLine.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "case_group")
@Data
public class Case_Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer case_group_Id;

    private Integer user_id;

    private Integer case_id;

    private role role;

    public enum role {
        reporter,editor,admin // add all enum values
    }

}
