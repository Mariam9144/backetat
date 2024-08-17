package com.fst.back_etat_civil.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Region;
import com.fst.back_etat_civil.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);
    List<User> findUsersById(Long Id);

    //List<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
    
    @Query("SELECT a FROM User a WHERE " +
 		   "CAST(a.id AS string) LIKE %:keyword% OR " +
 		   "a.username LIKE %:keyword% OR " +
			"a.email LIKE %:keyword%")
  Page<User> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);



}
