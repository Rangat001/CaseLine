package com.example.rgt.CaseLine.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_group")
@Data
public class Group {
    @Id
    private int group_id;

    private int case_id;
}
