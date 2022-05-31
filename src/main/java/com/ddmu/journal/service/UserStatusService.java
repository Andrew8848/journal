package com.ddmu.journal.service;

import com.ddmu.journal.model.UserStatus;
import com.ddmu.journal.repositories.UserStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserStatusService {
    @Autowired
    private UserStatusRepository userStatusRepository;

    public UserStatus getStatusByValue(String value){
        return userStatusRepository.findByValue(value);
    }

}
