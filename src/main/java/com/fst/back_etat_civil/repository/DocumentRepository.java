package com.fst.back_etat_civil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Document;
import com.fst.back_etat_civil.model.Vqf;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByNom(String nom);
    List<Document> findByCitoyen(Citoyen citoyen);
    
    @Query("SELECT a FROM Document a WHERE " +
			   "CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.nom LIKE %:keyword% OR " +
			   "a.type LIKE %:keyword% OR " +
			   "CAST(a.date AS string) LIKE %:keyword% OR " +
			   "a.citoyen.niciv LIKE %:keyword% OR " +
	           "a.citoyen.telephone LIKE %:keyword% OR " +
	           "a.type LIKE %:keyword%")
	    Page<Document> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);

}
