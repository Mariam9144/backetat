package com.fst.back_etat_civil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Commune;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {
   // List<Commune> findByNom(String nom);
	List<Commune> findByCercle(Cercle cercle);
	Boolean existsByCode(String code);
	Boolean existsByNomIgnoreCase(String code);
	//Boolean existsByNom(String code);
	
	@Query("SELECT a FROM Commune a WHERE " +
			   "( CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.code LIKE %:keyword% OR " +
			   "a.cercle.nom LIKE %:keyword% OR " +
			   "a.cercle.region.nom LIKE %:keyword% OR " +
			   "a.autre LIKE %:keyword% ) AND " +
            "a.cercle.region.nom <> 'Diaspora'")
	    Page<Commune> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);
	
	@Query("SELECT a FROM Commune a WHERE " +
			   "( CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.code LIKE %:keyword% OR " +
			   "a.cercle.nom LIKE %:keyword% OR " +
			   "a.cercle.region.nom LIKE %:keyword% OR " +
			   "a.autre LIKE %:keyword% ) AND " +
               "a.cercle.region.nom = 'Diaspora'")
	    Page<Commune> searchByKeywordInAllColumnsPayes(@Param("keyword") String keyword, Pageable pageable);
	Commune findByCode(String substring);

}
