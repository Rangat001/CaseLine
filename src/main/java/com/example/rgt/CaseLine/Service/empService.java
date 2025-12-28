package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.CaseRepository;
import com.example.rgt.CaseLine.Repository.Case_GrupRepository;
import com.example.rgt.CaseLine.Repository.PostRepository;
import com.example.rgt.CaseLine.entity.Case;
import com.example.rgt.CaseLine.entity.Case_Group;
import jakarta.persistence.AttributeOverride;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class empService {

    @Autowired
    private Case_GrupRepository caseGrupRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CaseRepository caseRepository;


    //    List of Cases assigned to emp_id
    public List<Case> empCases(int emp_id){
        List<Integer> caseIds = caseGrupRepository.List_CaseIdByUser(emp_id);
        List<Case> cases = caseRepository.findBycaseIdIn(caseIds);
        return cases;
    }

    public int countOfCases(int emp_id){
        int count = caseGrupRepository.CountOfCaseByUserId(emp_id);    //  return No. Case Under the Employee
        return count;
    }

    public int countOfPosts(int emp_id){
        //      Logic to count total posts made by emp_id
        int count = postRepository.countTotalPosts(emp_id);
        return count;
    }

    public int[] Active_Close_ratio(int emp_id){
        // Logic to calculate active and close cases ratio for emp_id
        int active = caseRepository.countOngoingCasesByUser(emp_id);
        int close = caseRepository.countClosedCasesByUser(emp_id);

        return new int[]{active, close};
    }

}
