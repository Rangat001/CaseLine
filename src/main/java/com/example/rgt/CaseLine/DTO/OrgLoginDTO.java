package com.example.rgt.CaseLine.DTO;


import com.example.rgt.CaseLine.Enum.OrgType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgLoginDTO {
    private String org_name;
    private String owner_name;
    private String email;
    private String password;
    private OrgType org_type;
}
