package com.fst.back_etat_civil.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.fst.back_etat_civil.model.Role;
import com.fst.back_etat_civil.repository.RoleRepository;

public class RoleService {
	
	@Autowired
	RoleRepository roleRepository;
	
	public void createRole(Role role) {
        
        role = roleRepository.save(role);
       
    }

}
