package com.ddmu.journal.restcontroller;

import com.ddmu.journal.model.jwt.JwtRequest;
import com.ddmu.journal.model.jwt.JwtResponse;
import com.ddmu.journal.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/user")
public class JwtController {

    @Autowired
    private JwtService jwtService;

    @PostMapping({"/authenticate"})
    public ResponseEntity<Map<String, Object>> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        Map<String, Object> auth = new HashMap<String, Object>();
        JwtResponse jwtToken = jwtService.createJwtToken(jwtRequest);
        auth.put("name",jwtToken.getUser().getDoctor().getNameSurname().getName());
        auth.put("surname", jwtToken.getUser().getDoctor().getNameSurname().getSurname());
        auth.put("email", jwtToken.getUser().getDoctor().getEmail());
        auth.put("role", jwtToken.getUser().getRoles());
        auth.put("jwtToken", jwtToken.getJwtToken());
        return ResponseEntity.ok().body(auth);
    }
}
