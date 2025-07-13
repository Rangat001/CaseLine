package com.example.rgt.CaseLine.DTO;

public class AdminDashboardDTO {
    private int totalCase;
    private int totalPost;
    private int totalMembers;
    private int[] activeCloseRatio;

    // Constructors
    public AdminDashboardDTO() {
    }

    public AdminDashboardDTO(int totalCase, int totalPost, int totalMembers, int[] activeCloseRatio) {
        this.totalCase = totalCase;
        this.totalPost = totalPost;
        this.totalMembers = totalMembers;
        this.activeCloseRatio = activeCloseRatio;
    }

    // Getters and Setters
    public int getTotalCase() {
        return totalCase;
    }

    public void setTotalCase(int totalCase) {
        this.totalCase = totalCase;
    }

    public int getTotalPost() {
        return totalPost;
    }

    public void setTotalPost(int totalPost) {
        this.totalPost = totalPost;
    }

    public int getTotalMembers() {
        return totalMembers;
    }

    public void setTotalMembers(int totalMembers) {
        this.totalMembers = totalMembers;
    }

    public int[] getActiveCloseRatio() {
        return activeCloseRatio;
    }

    public void setActiveCloseRatio(int[] activeCloseRatio) {
        this.activeCloseRatio = activeCloseRatio;
    }
}
