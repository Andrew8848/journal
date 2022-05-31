package com.ddmu.journal.restcontroller;

import com.ddmu.journal.model.Doctor;
import com.ddmu.journal.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogService logService;


    @GetMapping("/newest")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getNewestLogsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size
    ){

        try{
            return ResponseEntity.ok().body(logService.getNewestLogsByPage(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping("/newestByDoctor")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getNewestLogsByEmailOnPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3")int size,
            @RequestBody List<Doctor> doctors
    ){
        try{
            return ResponseEntity.ok().body(logService.getNewestLogsByEmailOnPage(page, size, doctors));

        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }
    }
}
