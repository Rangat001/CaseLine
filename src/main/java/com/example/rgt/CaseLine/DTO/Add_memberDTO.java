package com.example.rgt.CaseLine.DTO;

import com.example.rgt.CaseLine.entity.Case_Group;
import com.example.rgt.CaseLine.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Add_memberDTO {
    int case_id;
    String email;
    Case_Group.role role;

    public Add_memberDTO(int case_id, String email,Case_Group.role role){
        this.case_id = case_id;
        this.email = email;
        this.role = role;
    }


}
