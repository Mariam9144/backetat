package com.fst.back_etat_civil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Commune;
import com.fst.back_etat_civil.model.Vqf;

@Repository
public interface VqfRepository extends JpaRepository<Vqf,Long> {
    List<Vqf> findByNom(String nom);
    List<Vqf> findById(String id);
    List<Vqf> findByCommune(Commune commune);
    Boolean existsByCode(String code);
	Boolean existsByNom(String code);
	
	@Query("SELECT a FROM Vqf a WHERE " +
			   "(CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.code LIKE %:keyword% OR " +
			   "a.commune.nom LIKE %:keyword% OR " +
			   "a.commune.cercle.nom LIKE %:keyword% OR " +
			   "a.commune.cercle.region.nom LIKE %:keyword% OR " +
			   "a.autre LIKE %:keyword%) AND a.commune.cercle.region.nom <> 'Diaspora' ")
	    Page<Vqf> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);

	@Query("SELECT a FROM Vqf a WHERE " +
			   "(CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.code LIKE %:keyword% OR " +
			   "a.commune.nom LIKE %:keyword% OR " +
			   "a.commune.cercle.nom LIKE %:keyword% OR " +
			   "a.commune.cercle.region.nom LIKE %:keyword% OR " +
			   "a.autre LIKE %:keyword%) AND a.commune.cercle.region.nom = 'Diaspora' ")
	    Page<Vqf> searchByKeywordInAllColumnsVille(@Param("keyword") String keyword, Pageable pageable);
	boolean existsByNomIgnoreCase(String normalize);
	Commune findByCode(String substring);

}
