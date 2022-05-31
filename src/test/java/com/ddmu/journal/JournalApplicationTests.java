package com.ddmu.journal;

import com.ddmu.journal.model.Doctor;
import com.ddmu.journal.model.Journal;
import com.ddmu.journal.repositories.JournalRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
class JournalApplicationTests {

	@Autowired
	private JournalRepository journalRepository;

	@Test
	void contextLoads() {

		List<Doctor> doctors = new ArrayList<Doctor>();

		Doctor doctor1 = new Doctor();
		doctor1.setEmail("nataly.chery@mail.com");
		doctors.add(doctor1);

		Doctor doctor2 = new Doctor();
		doctor2.setEmail("andrey.4ery@gmail.com");
		doctors.add(doctor2);

		List<String> emails = doctors.stream().map(d -> d.getEmail()).collect(Collectors.toList());

		List<Journal> journals = new ArrayList<Journal>();

		Pageable paging = PageRequest.of(0, 77);

		Page<Journal> pageJournal = journalRepository.findNewestJournalsByEmailsAndDateTimeOnPage(
				paging,
				emails,
				Arrays.asList(new String[]{"DELETED"})
		);

		journals = pageJournal.getContent();

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("currentPage", pageJournal.getNumber());
		body.put("totalItems", pageJournal.getTotalElements());
		body.put("totalPages", pageJournal.getTotalPages());
		body.put("journals", journals);


	}

}
