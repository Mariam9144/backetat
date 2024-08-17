package com.fst.back_etat_civil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Profession;

@Repository
public interface ProfessionRepository extends JpaRepository<Profession, Long> {
	Boolean existsByLibelleIgnoreCase(String libelle);
	
	@Query("SELECT a FROM Profession a WHERE " +
			   "CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.libelle LIKE %:keyword%")
	 Page<Profession> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);
}