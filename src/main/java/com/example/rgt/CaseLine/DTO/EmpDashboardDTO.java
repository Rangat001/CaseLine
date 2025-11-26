package com.example.rgt.CaseLine.DTO;

import com.example.rgt.CaseLine.entity.Case_Group;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EmpDashboardDTO {
    String user_name;
    Case_Group.role user_role;
    Integer user_id;
    Integer org_id;
    Integer total_case;
    Integer post_count;
    Integer completed_case_count;
    Integer pending_case_count;
    List<CaseDetailsDTO> caseDetailsDTOList;

    public EmpDashboardDTO(String user_name, Case_Group.role user_role, Integer user_id, Integer org_id, Integer total_case,
                           Integer post_count, Integer completed_case_count, Integer pending_case_count,
                           List<CaseDetailsDTO> caseDetailsDTOList) {
        this.user_role = user_role;
        this.user_name = user_name;
        this.user_id = user_id;
        this.org_id = org_id;
        this.total_case = total_case;
        this.post_count = post_count;
        this.completed_case_count = completed_case_count;
        this.pending_case_count = pending_case_count;
        this.caseDetailsDTOList = caseDetailsDTOList;
    }

}
