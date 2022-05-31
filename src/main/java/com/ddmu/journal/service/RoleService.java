package com.ddmu.journal.service;

import com.ddmu.journal.model.Role;
import com.ddmu.journal.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role getRoleByValue(String value){
        return roleRepository.findByValue(value);
    }

    public Collection<Role> getRolesByValues(Collection<String> values){
        return values.stream().map(s -> roleRepository.findByValue(s)).collect(Collectors.toCollection(ArrayList::new));
    }
}
