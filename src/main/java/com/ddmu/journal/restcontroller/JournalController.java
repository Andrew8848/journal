package com.ddmu.journal.restcontroller;

import com.ddmu.journal.exceptions.ApiMessage;
import com.ddmu.journal.model.*;
import com.ddmu.journal.service.ConsultantService;
import com.ddmu.journal.service.JournalService;
import com.ddmu.journal.service.JournalStatusService;
import com.ddmu.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/journal")
public class JournalController {

    @Autowired
    private JournalService journalService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConsultantService consultantService;

    @Autowired
    JournalStatusService journalStatusService;

    private final String KEY_API_MESSAGE = "apimessage";

    private final String HEADER_TOKEN = "authorization";

    @PostMapping("/createJournal")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Journal> postNewJournal(@RequestHeader HttpHeaders headers, @RequestBody Journal journal) {
            return ResponseEntity.ok().body(journalService.createJournal(headers.getFirst(HEADER_TOKEN), journal));
    }

    @GetMapping("/newest")
    public ResponseEntity<Map<String, Object>>getNewestJournalByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
        ){

        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByPage(page, size, Arrays.asList(new String[]{"AVAILABLE"})));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/newestForElevatedUser")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>>getNewestJournalByPageForElevatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ){

        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByPage(page, size, Arrays.asList(new String[]{"AVAILABLE", "HIDDEN"})));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @GetMapping("/deletedRecently")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>>getDeletedRecentlyJournalByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ){
        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByPage(page, size, Arrays.asList(new String[]{"DELETED"})));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping("/deletedRecentlyByDoctor")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getDeletedRecentlyJournalByDoctorOnPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestBody List<Doctor> doctors
    ){
        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByEmailOnPage(page, size, doctors, Arrays.asList(new String[]{"DELETED"})));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/newestByDoctor")
    public ResponseEntity<Map<String, Object>> getNewestJournalByDoctorOnPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestBody List<Doctor> doctors
    ){
        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByEmailOnPage(page, size, doctors, Arrays.asList(new String[]{"AVAILABLE"})));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/newestByDoctorForElevatedUser")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>> getNewestJournalByDoctorOnPageForElevatedUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestBody List<Doctor> doctors
    ){
        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByEmailOnPage(page, size, doctors, Arrays.asList(new String[]{"AVAILABLE", "HIDDEN"})));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }



    @PostMapping("/deletedNewestByEmail")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getDeletedNewestJournalByEmailOnPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestBody List<Doctor> doctors
    ){
        try{
            return ResponseEntity.ok().body(journalService.getNewestJournalByEmailOnPage(page, size, doctors, Arrays.asList(new String[]{"DELETED"})));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/editJournal")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>> editJournalById(@RequestHeader HttpHeaders headers, @RequestBody Journal journal){
        Map<String, Object> body = new HashMap<>();
        if(journalService.editJournalById(headers.getFirst(HEADER_TOKEN), journal)){
            body.put(KEY_API_MESSAGE,  new ApiMessage("EDITED", "Journal is successfully edited"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERROR_EDIT", "Failed to edit journal"));
        return ResponseEntity.badRequest().body(body);
    }

    @PostMapping("/hideJournal")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>> hideJournalById(@RequestHeader HttpHeaders headers, @RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(journalService.hideJournalById(id, headers.getFirst(HEADER_TOKEN))){
            body.put(KEY_API_MESSAGE,  new ApiMessage("HIDED", "Successfully hidded journal"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERROR_HIDE", "Failed to hide journal"));
        return ResponseEntity.badRequest().body(body);
    }

    @PostMapping("/openJournal")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>> openJournalById(@RequestHeader HttpHeaders headers, @RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(journalService.openJournalById(id, headers.getFirst(HEADER_TOKEN))){
            body.put(KEY_API_MESSAGE,  new ApiMessage("OPENED", "Successfully opened journal"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERROR_OPEN", "Failed to open journal"));
        return ResponseEntity.badRequest().body(body);
    }

    @PostMapping("/deleteJournal")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Map<String, Object>> deletedJournalById(@RequestHeader HttpHeaders headers, @RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(journalService.deleteJournalById(id, headers.getFirst(HEADER_TOKEN))){
            body.put(KEY_API_MESSAGE,  new ApiMessage("DELETED", "Successfully deleted journal"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERROR_DELETE", "Failed to delete journal"));
        return ResponseEntity.badRequest().body(body);
    }

    @PostMapping("/restoreDeletedJournal")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> restoreDeletedJournalById(@RequestHeader HttpHeaders headers, @RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(journalService.restoreJournalById(id, headers.getFirst(HEADER_TOKEN))){
            body.put(KEY_API_MESSAGE,  new ApiMessage("RESTORED", "Successfully restored journal"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERROR_RESTORE", "Failed to restore journal"));
        return ResponseEntity.badRequest().body(body);
    }


}
