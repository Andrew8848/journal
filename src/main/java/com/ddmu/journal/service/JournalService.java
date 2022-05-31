package com.ddmu.journal.service;

import com.ddmu.journal.model.*;
import com.ddmu.journal.repositories.JournalRepository;
import com.ddmu.journal.repositories.UserRepository;
import com.ddmu.journal.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class JournalService {

    @Autowired
    private JournalRepository journalRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    private JournalStatusService journalStatusService;

    @Autowired
    private ActionService actionService;

    @Autowired
    private LogService logService;

    @Autowired
    private JwtUtil jwtUtil;

    public Journal createJournal(String bearerToken, Journal journal) {
        String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
        User user = userService.getUserByEmail(token);
        journal.setDoctor(user.getDoctor());
        Patient patient = patientService.getPatientByNameAndSurname(
                journal.getPatient().getNameSurname().getName(),
                journal.getPatient().getNameSurname().getSurname(),
                journal.getPatient().getDateOfBirth());
        if(patient != null) {
            journal.setPatient(patient);
        }
        journal.setConsultation(consultantService.getConsultantByValue(journal.getConsultation().getValue()));
        journal.setJournalStatus(journalStatusService.getJournalStatusByValue("AVAILABLE"));
        journal.setDateTimePublication(new DateTime().setDateTimeNow());

        Journal savedJournal = journalRepository.save(journal);
        logService.createJournalLog(user, savedJournal);
        return savedJournal;
    }

    public Map<String, Object> getNewestJournalByPage(int page, int size, List<String> filter) throws InterruptedException {
        List<Journal> journals = new ArrayList<Journal>();

        Pageable paging = PageRequest.of(page, size);

        Page<Journal> pageJournal = journalRepository.findNewestJournalsByDateTimeOnPage(paging, filter);
        journals = pageJournal.getContent();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("currentPage", pageJournal.getNumber());
        body.put("totalItems", pageJournal.getTotalElements());
        body.put("totalPages", pageJournal.getTotalPages());
        body.put("journals", journals);

        return body;
    }

    public Map<String, Object> getNewestJournalByEmailOnPage(int page, int size, List<Doctor> doctors, List<String> filter) throws InterruptedException{
        List<Journal> journals = new ArrayList<Journal>();

        Pageable paging = PageRequest.of(page, size);

        Page<Journal> pageJournal = journalRepository.findNewestJournalsByEmailsAndDateTimeOnPage(
                paging,
                doctors.stream().map(doctor -> doctor.getEmail()).collect(Collectors.toList()),
                filter
        );

        journals = pageJournal.getContent();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("currentPage", pageJournal.getNumber());
        body.put("totalItems", pageJournal.getTotalElements());
        body.put("totalPages", pageJournal.getTotalPages());
        body.put("journals", journals);
        return body;
    }

    public boolean editJournalById(String bearerToken, Journal journal){
        try {
            String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
            User user = userService.getUserByEmail(token);
            Journal oldJournal = journalRepository.getById(journal.getId());
            if(oldJournal.getJournalStatus().getValue().equals("DELETED")) {
                return false;
            }
            oldJournal.setJournalStatus(journalStatusService.getJournalStatusByValue("OUTDATE"));

            Journal newJournal = new Journal();

            newJournal.setDoctor(journal.getDoctor());
            Patient patient = patientService.getPatientByNameAndSurname(
                    journal.getPatient().getNameSurname().getName(),
                    journal.getPatient().getNameSurname().getSurname(),
                    journal.getPatient().getDateOfBirth()
            );
            if (patient != null) {
                newJournal.setPatient(patient);
            } else if(patient == null){
                Patient newPatient = new Patient(
                        new NameSurname(journal.getPatient().getNameSurname().getName(),
                        journal.getPatient().getNameSurname().getSurname()),
                        journal.getPatient().getDateOfBirth()
                );
                newJournal.setPatient(newPatient);
            }

            newJournal.setDiagnosis(journal.getDiagnosis());
            newJournal.setConsultation(consultantService.getConsultantByValue(journal.getConsultation().getValue()));
            newJournal.setJournalStatus(journalStatusService.getJournalStatusByValue("AVAILABLE"));
            newJournal.setDateTimePublication(journal.getDateTimePublication());
            newJournal.setDateTimeLastModified(new DateTime().setDateTimeNow());

            Journal addedJournal = journalRepository.save(newJournal);
            logService.updateJournalLog(user, oldJournal, addedJournal);
            return true;
        } catch (Error e){
            return false;
        }
    }

    public boolean hideJournalById(Long id, String bearerToken){
        try {
            String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
            User user = userService.getUserByEmail(token);
            Journal journal = journalRepository.getById(id);
            journal.setJournalStatus(journalStatusService.getJournalStatusByValue("HIDDEN"));
            logService.hideJournalLog(user, journal);
            journalRepository.save(journal);
            return true;
        } catch (Error e){
            return false;
        }
    }

    public boolean deleteJournalById(Long id, String bearerToken){
        try {
            String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
            User user = userService.getUserByEmail(token);
            Journal journal = journalRepository.getById(id);
            journal.setJournalStatus(journalStatusService.getJournalStatusByValue("DELETED"));
            logService.deleteJournalLog(user, journal);
            journalRepository.save(journal);
            return true;
        } catch (Error e){
            return false;
        }
    }

    public boolean openJournalById(Long id, String bearerToken){
        try {
            String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
            User user = userService.getUserByEmail(token);
            Journal journal = journalRepository.getById(id);
            if(!journal.getJournalStatus().getValue().equals("HIDDEN")) {
                return false;
            }
            journal.setJournalStatus(journalStatusService.getJournalStatusByValue("AVAILABLE"));
            logService.openJournalLog(user, journal);
            journalRepository.save(journal);
            return true;
        } catch (Error e){
            return false;
        }
    }

    public boolean restoreJournalById(Long id, String bearerToken){
        try {
            String token = jwtUtil.getUserNameFromToken(bearerToken.substring(7, bearerToken.length()));
            User user = userService.getUserByEmail(token);
            Journal journal = journalRepository.getById(id);
            if(!journal.getJournalStatus().getValue().equals("DELETED")) {
                return false;
            }
            journal.setJournalStatus(journalStatusService.getJournalStatusByValue("AVAILABLE"));
            logService.restoreJournalLog(user, journal);
            journalRepository.save(journal);
            return true;
        } catch (Error e){
            return false;
        }
    }
}
