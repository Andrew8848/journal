package com.ddmu.journal.init;

import com.ddmu.journal.model.*;
import com.ddmu.journal.repositories.*;
import com.ddmu.journal.service.JournalService;
import com.ddmu.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.Collection;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @Autowired
    private JournalStatusRepository journalStatusRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private ActionRepository actionRepository;

//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JournalService journalService;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(alreadySetup) return;
        Privilege readPrivilege = createPrivilegeIfNotFound("READ");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE");
        Privilege editPrivilege = createPrivilegeIfNotFound("EDIT");
        Privilege deletePrivilege = createPrivilegeIfNotFound("DELETE");

        createRoleIfNotFound("OWNER", Arrays.asList(readPrivilege, writePrivilege, editPrivilege, deletePrivilege));
        createRoleIfNotFound("STAFF", Arrays.asList(readPrivilege, writePrivilege, editPrivilege, deletePrivilege));
        createRoleIfNotFound("USER", Arrays.asList(readPrivilege));

        createUserStatusIfNotFound("ACTIVE");
        createUserStatusIfNotFound("INACTIVE");
        createUserStatusIfNotFound("BANNED");
        createUserStatusIfNotFound("UNCONFIRMED");

        createJournalStatusIfNotFound("AVAILABLE");
        createJournalStatusIfNotFound("DELETED");
        createJournalStatusIfNotFound("HIDDEN");
        createJournalStatusIfNotFound("EDITING");
        createJournalStatusIfNotFound("OUTDATE");

        createConsultantIfNotFound("POLICLINIC");
        createConsultantIfNotFound("HOSPITAL");

        createActionIfNotFound("CREATED");
        createActionIfNotFound("DELETED");
        createActionIfNotFound("UPDATED");
        createActionIfNotFound("HIDDED");
        createActionIfNotFound("RESTORED");
        createActionIfNotFound("OPEN");

//        User user = userService.getUserById(1L);
//
//        Journal journal = new Journal();
//        journal.setDoctor(user.getDoctor());
//        journal.setDiagnosis("Dididididididiadiaidaidaidaidaidiagnose Example");
//
//        journal.setDateTimeLastModified(new DateTime(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis())));
//        journal.setDateTimePublication(new DateTime(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis())));
//
//        Patient patient = new Patient(new NameSurname("Сергей", "Иванович"), 30);
//
//        journal.setPatient(patient);
//
//        journal.setConsultant(consultantRepository.findByValue("HOSPITAL"));
//
//        journal.setJournalStatus(journalStatusRepository.findByValue("AVAILABLE"));
//
//
//        journalService.createJournal(journal);
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String value){
        Privilege privilege = privilegeRepository.findByValue(value);
        if(privilege == null){
            privilege = new Privilege(value);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String value, Collection<Privilege> privileges){
        Role role = roleRepository.findByValue(value);
        if(role == null){
            role = new Role(value);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    UserStatus createUserStatusIfNotFound(String value){
        UserStatus userStatus = userStatusRepository.findByValue(value);
        if(userStatus == null){
            userStatus = new UserStatus(value);
            userStatusRepository.save(userStatus);
        }
        return userStatus;
    }

    @Transactional
    JournalStatus createJournalStatusIfNotFound(String value){
        JournalStatus journalStatus = journalStatusRepository.findByValue(value);
        if(journalStatus == null){
            journalStatus = new JournalStatus(value);
            journalStatusRepository.save(journalStatus);
        }
        return journalStatus;
    }

    @Transactional
    Consultation createConsultantIfNotFound(String value){
        Consultation consultant = consultationRepository.findByValue(value);
        if(consultant == null){
            consultant = new Consultation(value);
            consultationRepository.save(consultant);
        }
        return consultant;
    }
    @Transactional
    Action createActionIfNotFound(String value){
        Action action = actionRepository.findByValue(value);
        if(action == null){
            action = new Action(value);
            actionRepository.save(action);
        }
        return action;
    }

}
