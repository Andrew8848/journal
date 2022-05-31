package com.ddmu.journal.service;

import com.ddmu.journal.model.Action;
import com.ddmu.journal.repositories.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActionService {
    @Autowired
    private ActionRepository actionRepository;

    public Action getActionByValue(String value){
        return actionRepository.findByValue(value);
    }
}
