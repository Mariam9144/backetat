package com.fst.back_etat_civil.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Citoyen;


@Repository
public interface CitoyenRepository extends JpaRepository<Citoyen, Long> {
    //List<Citoyen> findByNom(String nom);
	Boolean existsByTelephone(String telephone);
	List<Citoyen> findByCle(String cle);
	Boolean existsByNiciv(String niciv);
	Citoyen findByNiciv(String niciv);
	
	@Query("SELECT a FROM Citoyen a WHERE " +
			   "CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.prenom LIKE %:keyword% OR " +
			   "a.telephone LIKE %:keyword% OR " +
	           "a.niciv LIKE %:keyword% OR " +
	           "a.genre LIKE %:keyword% OR " +
	           "a.profession.libelle LIKE %:keyword% OR " +
	           "TO_CHAR(a.dateNaissance, 'YYYY-MM-DD') LIKE %:keyword% OR " +
	           "a.lieuNaissance.nom LIKE %:keyword% OR " +
	           "a.lieuNaissance.commune.cercle.region.nom LIKE %:keyword% OR " +
	           "a.lieuNaissance.commune.cercle.nom LIKE %:keyword%")
	    Page<Citoyen> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);

}
