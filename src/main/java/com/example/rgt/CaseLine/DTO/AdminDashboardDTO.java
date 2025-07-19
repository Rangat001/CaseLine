package com.example.rgt.CaseLine.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AdminDashboardDTO {
    private String org_name;
    private int totalCase;
    private int totalPost;
    private int totalMembers;
    private String activeCloseRatio;

    // Constructors
    public AdminDashboardDTO() {
    }

    public AdminDashboardDTO(String org_name,int totalCase, int totalPost, int totalMembers, String activeCloseRatio) {
        this.org_name = org_name;
        this.totalCase = totalCase;
        this.totalPost = totalPost;
        this.totalMembers = totalMembers;
        this.activeCloseRatio = activeCloseRatio;
    }
}
