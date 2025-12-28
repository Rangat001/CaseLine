package com.example.rgt.CaseLine.Service;

import com.example.rgt.CaseLine.DTO.Case_Member_DTO;
import com.example.rgt.CaseLine.Repository.*;
import com.example.rgt.CaseLine.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private Case_GrupRepository caseGrupRepository;

    @Autowired
    private OrgRepository orgRepository;

    @Autowired
    private groupRepository groupRepository;
    @Autowired
    private UserRepository userRepository;

    //                                   Case Creation
    public void createCase(Case entity) {
        caseRepository.save(entity);
    }

    //                            Create INtial Case_Group
    public boolean check_intial_grp(int Admin_id, int case_id) {
        List<Case_Group> list = caseGrupRepository.findAllByCase_id(case_id);
        if (list.size() == 0) {
            return true;
        }
        return false;
    }

    public void intial_grp(int Admin_id, int case_id) {
        Case_Group case_group = new Case_Group();
        CaseGroupId id = new CaseGroupId();
        id.setCaseId(case_id);
        id.setUserId(Admin_id);

        //              First need Group id to create Case_group
        Group group = new Group();
        group.setCase_id(case_id);
        groupRepository.save(group);


        case_group.setId(id);
        case_group.setGroupId(groupRepository.findGroupIdByCaseId(case_id));
        case_group.setRole(Case_Group.role.admin);
        caseGrupRepository.save(case_group);
    }


    //                             Return All Case Of Admin
    public List<Case> getCasesByAdminId(int adminId) {
        return caseRepository.findAllByCreatedBy(adminId);
    }

    //                         Org Name
    public String getOrgName(int org_id) {
        return orgRepository.findorg_nameById(org_id);
    }

    //                             Return Count Case of Admin
    public int countOfCases(int adminId) {
        return getCasesByAdminId(adminId).size();
    }

    //                              total Posts in all Cases
    public int countOfPosts(int adminId) {
        return postRepository.countTotalPostsByCreatedBy(adminId);
    }

    //                              total member under Admin
    public int countMembers(int adminId) {
        return caseGrupRepository.countUniqueMembersByAdmin(adminId);
    }

    //           Active Close Ration
    public int[] Active_Close_ratio(int adminId) {
        return new int[]{caseRepository.countOngoingCases(adminId), caseRepository.countClosedCases(adminId)};
    }


    //                      Add member to Group
    public void Add_member(Case_Group caseGroup) {
        int grp_id = groupRepository.findGroupIdByCaseId(caseGroup.getId().getCaseId());
        caseGroup.setGroupId(grp_id);
        caseGrupRepository.save(caseGroup);
    }


    //                      Edit Case Details with Memebers
    public void editCase(Case entity) {
        caseRepository.save(entity);
    }


    //                      Get Members of a Case
    public List<Case_Member_DTO> getMembersByCaseId(int caseId) {

        List<Case_Group> members = caseGrupRepository.findAllByCase_id(caseId);
        List<Case_Member_DTO> users = new ArrayList<>();

        for (Case_Group member : members) {
            int id = member.getId().getUserId();
            Optional<User> user = userRepository.findById(id);
            Case_Member_DTO instance = new Case_Member_DTO(id,
                    user.get().getName(),
                    user.get().getEmail(),
                    member.getRole());
            users.add(instance);
        }
        return users;
    }


    //                            * Edit member role
    @Transactional
    public void editMemberRole(int caseId, int userId, Case_Group.role newRole) {
        caseGrupRepository.updateRoleByCaseIdAndUserId(caseId, userId, newRole);
    }

    //                           * Delete Member from Case
    @Transactional
    public void deleteMemberFromCase(int caseId, int userId) {
        int affected_row = caseGrupRepository.deleteById(caseId, userId);
    }


}


