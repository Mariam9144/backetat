package com.fst.back_etat_civil.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
   // List<Region> findByNom(String nom);
	Boolean existsByCode(String code);
	Boolean existsByNomIgnoreCase(String code);

	 @Query("SELECT a FROM Region a WHERE " +
	           "(CAST(a.id AS string) LIKE %:keyword% OR " +
	           "a.nom LIKE %:keyword% OR " +
	           "a.code LIKE %:keyword% OR " +
	           "a.autre LIKE %:keyword%) AND " +
	           "a.nom <> 'Diaspora'")
 Page<Region> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);
	 
	 @Query("SELECT a FROM Region a WHERE " +
	           "(CAST(a.id AS string) LIKE %:keyword% OR " +
	           "a.nom LIKE %:keyword% OR " +
	           "a.code LIKE %:keyword% OR " +
	           "a.autre LIKE %:keyword%) AND " +
	           "a.nom = 'Diaspora'")
Page<Region> searchByKeywordInAllColumnsDiapora(@Param("keyword") String keyword, Pageable pageable);


}
