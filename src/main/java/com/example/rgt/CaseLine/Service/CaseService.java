package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.Repository.CaseRepository;
import com.example.rgt.CaseLine.entity.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseService {
    @Autowired
    private CaseRepository caseRepository;

        public Case getCaseDetails(int id) {
            try{
                Case Case = caseRepository.findById(id);
                if(Case == null) {
                    throw new RuntimeException("Case not found with id: " + id);
                }
                return Case;
            }catch (Exception e){
                throw new RuntimeException("Error retrieving case details: " + e.getMessage());
            }
        }
}
