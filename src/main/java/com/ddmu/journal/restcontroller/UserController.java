package com.ddmu.journal.restcontroller;

import com.ddmu.journal.exceptions.ApiMessage;
import com.ddmu.journal.model.DateTime;
import com.ddmu.journal.model.User;
import com.ddmu.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.Date;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private static final String KEY_API_MESSAGE = "apimessage";
    @Autowired
    private UserService userService;

    private final String keyApiMessage = "apimessage";

    @PostMapping("/registerNewUser")
    public ResponseEntity<Map<String, Object>> registerNewUser(@RequestBody User user) {
        Map<String, Object> body = new HashMap<String, Object>();

        if(userService.emailIsExist(user.getDoctor().getEmail())){
            body.put(keyApiMessage, new ApiMessage("EMAIL_EXIST", "email already exists"));
            return ResponseEntity.unprocessableEntity().body(body);
        }
        body.put(keyApiMessage, new ApiMessage("SUCCESSFULLY", "your account has been successfully registered"));
        userService.createUser(user);
        return ResponseEntity.ok().body(body);
    }

    @GetMapping("/chekToken")
    @PreAuthorize("hasAnyRole('STAFF', 'OWNER')")
    public ResponseEntity<Boolean> checkToken(){
        return ResponseEntity.ok(true);
    }

    @GetMapping("/getAllElevatedUsers")
    public ResponseEntity<Map<String, Object>> getAllUsers(){
        List<User> users = null;
        Map<String, Object> body = new HashMap<String, Object>();

        users = userService.getAllElevatedUsers();

        users = users.stream().map(user -> {
            user.setPassword(null);
            return user;
        }).collect(Collectors.toList());

        body.put("users", users);

        return ResponseEntity.ok(body);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> getNewestLogsByPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ){

        try{
            return ResponseEntity.ok().body(userService.getAllUsersByPage(page, size));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(null);
        }

    }

    @PostMapping("/elevateUser")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> elevateUserById(@RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(userService.setUserRole(id, "STAFF")){
            body.put(KEY_API_MESSAGE,  new ApiMessage("SETSTUFF", "Successfully setting stuff user"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERRORSETSTUFF", "failed setting stuff user"));
        return ResponseEntity.ok().body(body);
    }

    @PostMapping("/dropUser")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> dropUserById(@RequestParam Long id){
        Map<String, Object> body = new HashMap<String, Object>();
        if(userService.deleteUserRole(id, "STAFF")){
            body.put(KEY_API_MESSAGE,  new ApiMessage("SETSTUFF", "Successfully setting stuff user"));
            return ResponseEntity.ok(body);
        }
        body.put(KEY_API_MESSAGE,  new ApiMessage("ERRORSETSTUFF", "failed setting stuff user"));
        return ResponseEntity.ok().body(body);
    }

}
