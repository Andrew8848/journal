package com.ddmu.journal.service;

import com.ddmu.journal.model.Consultation;
import com.ddmu.journal.repositories.ConsultationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultantService {

    @Autowired
    private ConsultationRepository consultantRepository;

    public Consultation getConsultantByValue(String value){
        return consultantRepository.findByValue(value);
    }
}
