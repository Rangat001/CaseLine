package com.example.rgt.CaseLine.DTO;

import com.example.rgt.CaseLine.entity.Case_Group;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Case_Member_DTO {
    int user_id;
    String name;
    String email;
    Case_Group.role role;

    public Case_Member_DTO(int user_id, String name, String email, Case_Group.role role) {
        this.user_id = user_id;
        this.name = name;
        this.email = email;
        this.role = role;
    }
}
