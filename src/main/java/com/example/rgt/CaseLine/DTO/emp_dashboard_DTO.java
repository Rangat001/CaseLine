package com.example.rgt.CaseLine.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.ALWAYS)
public class emp_dashboard_DTO {
    //                                          Data that we need to show on emp dashboard only No.
    //                                          Case Data Show from emp_service
    private int totalAssignedCases;
    private int totalPosts;
    private int active;
    private int close;

    // Constructors
    public emp_dashboard_DTO() {
    }

    public emp_dashboard_DTO( int totalAssignedCases, int totalPosts, int active , int close) {
        this.totalAssignedCases = totalAssignedCases;
        this.totalPosts = totalPosts;
        this.active = active;
        this.close = close;
    }
}
