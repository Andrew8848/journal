package com.ddmu.journal.service;

import com.ddmu.journal.model.*;
import com.ddmu.journal.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class LogService {
    @Autowired
    private LogRepository logRepository;

    @Autowired
    private ActionService actionService;

    public Map<String, Object> getNewestLogsByPage(int page, int size) throws InterruptedException {
        List<Log> logs = new ArrayList<Log>();

        Pageable paging = PageRequest.of(page, size);

        Page<Log> logsPage = logRepository.getLogsByPage(paging);
        logs = logsPage.getContent();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("currentPage", logsPage.getNumber());
        body.put("totalItems", logsPage.getTotalElements());
        body.put("totalPages", logsPage.getTotalPages());
        body.put("logs", logs);

        return body;
    }

    public Map<String, Object> getNewestLogsByEmailOnPage(int page, int size, List<Doctor> doctors) throws InterruptedException{
        List<Log> logs = new ArrayList<Log>();

        Pageable paging = PageRequest.of(page, size);

        Page<Log> logsPage = logRepository.getLogsByUsersOnPage(
                paging,
                doctors.stream().map(doctor -> doctor.getEmail()).collect(Collectors.toList())
        );

        logs = logsPage.getContent();

        Map<String, Object> body = new HashMap<String, Object>();
        body.put("currentPage", logsPage.getNumber());
        body.put("totalItems", logsPage.getTotalElements());
        body.put("totalPages", logsPage.getTotalPages());
        body.put("logs", logs);

        return body;
    }

    public Log setLog(Log log){
        log.setLogDateTime(new DateTime(new Date(System.currentTimeMillis()), new Time(System.currentTimeMillis())));
        return logRepository.save(log);
    }

    public Log createJournalLog(User user, Journal newJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), newJournal, actionService.getActionByValue("CREATED"));
        return logRepository.save(log);
    }

    public Log updateJournalLog(User user, Journal oldJournal, Journal newJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), oldJournal, newJournal, actionService.getActionByValue("UPDATED"));
        return logRepository.save(log);
    }

    public Log hideJournalLog(User user, Journal oldJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), oldJournal, null, actionService.getActionByValue("HIDDED"));
        return logRepository.save(log);
    }

    public Log deleteJournalLog(User user, Journal oldJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), oldJournal,null,  actionService.getActionByValue("DELETED"));
        return logRepository.save(log);
    }

    public Log openJournalLog(User user, Journal newJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), newJournal, actionService.getActionByValue("OPEN"));
        return logRepository.save(log);
    }

    public Log restoreJournalLog(User user, Journal newJournal){
        Log log = new Log(user, new DateTime().setDateTimeNow(), newJournal, actionService.getActionByValue("RESTORED"));
        return logRepository.save(log);
    }
}
