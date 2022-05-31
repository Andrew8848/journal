package com.ddmu.journal.service;

import com.ddmu.journal.model.*;
import com.ddmu.journal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    public List<User> getAllUsers(){
//        return userRepository.findAllUsers();
//    }

    public Map<String, Object> getAllUsersByPage(int page, int size) throws InterruptedException {
        List<User> users = new ArrayList<User>();

        Pageable paging = PageRequest.of(page, size);

        Page<User> pageJournal = userRepository.findAllUsers(paging);
        users = pageJournal.getContent();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("currentPage", pageJournal.getNumber());
        body.put("totalItems", pageJournal.getTotalElements());
        body.put("totalPages", pageJournal.getTotalPages());
        body.put("users", users);

        return body;
    }

    public List<User> getAllElevatedUsers(){
        return userRepository.findAllElevatedUsers();
    }

    public User createUser(User user){

        user.setDateTimeCreation(new DateTime().setDateTimeNow());
        user.setDateTimeWasOnline(new DateTime().setDateTimeNow());
        user.setRoles(roleService.getRolesByValues(Arrays.asList(new String[] {"USER"})));
        user.setUserStatus(userStatusService.getStatusByValue("INACTIVE"));
        user.setPassword(getEncodedPassword(user.getPassword()));

        return userRepository.save(user);
    }

    public boolean emailIsExist(String email) {

        if (this.userRepository.emailIsExist(email) == 1) {
            return true;
        }

        return false;
    }

    public User getUserByEmail(String email){
       return userRepository.findByEmail(email);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public String getEncodedPassword(String password){
        return passwordEncoder.encode(password);
    }

    public boolean setUserRole(Long id, String staff){
        try{
            User user = userRepository.getById(id);
            Collection<Role> roles = user.getRoles();
            if(roles.stream().map(role -> {
                return role.getValue();
            }).collect(Collectors.toList()).contains(staff)){
                return true;
            }
            roles.add(roleService.getRoleByValue(staff));
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        } catch (Error e){
            return false;
        }
    }

    public boolean deleteUserRole(Long id, String staff) {
        try{
            User user = userRepository.getById(id);
            Collection<Role> roles = user.getRoles();
            if(!roles.stream().map(role -> {
                return role.getValue();
            }).collect(Collectors.toList()).contains(staff)){
                return true;
            }
            if(staff.equals("USER")){
                return false;
            }
            roles = roleService.getRolesByValues(Arrays.asList(new String[] {"USER"}));
            user.setRoles(roles);
            userRepository.save(user);
            return true;
        } catch (Error e){
            return false;
        }
    }
}
