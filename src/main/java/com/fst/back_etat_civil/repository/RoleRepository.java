package com.fst.back_etat_civil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.ERole;
import com.fst.back_etat_civil.model.Role;
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(ERole nom);
    List<Role> findById(Long Id);
}