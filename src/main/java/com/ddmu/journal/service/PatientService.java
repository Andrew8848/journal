package com.ddmu.journal.service;

import com.ddmu.journal.model.Patient;
import com.ddmu.journal.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;


    public Patient getPatientByNameAndSurname(String name, String surname, Date dateOfBirth){
        return patientRepository.findByNameAndSurname(name, surname, dateOfBirth);
    }
}
