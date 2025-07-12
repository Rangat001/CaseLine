package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.CaseRepository;
import com.example.rgt.CaseLine.Repository.PostRepository;
import com.example.rgt.CaseLine.entity.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private PostRepository postRepository;

    //                                         Case Creation
    public void createCase(Case entity){
        caseRepository.save(entity);
    }

    //              Return All Case Of Admin
    public List<Case> getCasesByAdminId(int adminId) {
        return caseRepository.findAllByCreatedBy(adminId);
    }

    //    Return Count Case of Admin
    public int countOfCases(int adminId){
        return getCasesByAdminId(adminId).size();
    }

    //     total Posts in all Cases
    public int countOfPosts(int adminId){
        return postRepository.countTotalPostsByCreatedBy(adminId);
    }

}