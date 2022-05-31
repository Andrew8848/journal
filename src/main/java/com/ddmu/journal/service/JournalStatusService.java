package com.ddmu.journal.service;

import com.ddmu.journal.model.JournalStatus;
import com.ddmu.journal.repositories.JournalStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JournalStatusService {

    @Autowired
    private JournalStatusRepository journalStatusRepository;

    public JournalStatus getJournalStatusByValue(String value){
        return journalStatusRepository.findByValue(value);
    }
}
