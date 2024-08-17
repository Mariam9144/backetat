package com.fst.back_etat_civil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Citoyen;
import com.fst.back_etat_civil.model.Condamnation;

@Repository
public interface CondamnationRepository extends JpaRepository<Condamnation, Long> {

    
    List<Condamnation> findByCitoyen(Citoyen citoyen);
    
    @Query("SELECT a FROM Condamnation a WHERE " +
			   "CAST(a.id AS string) LIKE %:keyword% OR " +
			   "a.juridiction LIKE %:keyword% OR " +
			   "a.quantum LIKE %:keyword% OR " +
			   "a.natureDelitCrime LIKE %:keyword% OR " +
			   "CAST(a.dateCondamnation AS string) LIKE %:keyword% OR " +
			   "CAST(a.dateDetention AS string) LIKE %:keyword% OR " +
			   "CAST(a.dateDelitCrime AS string) LIKE %:keyword% OR " +
			   "a.citoyen.niciv LIKE %:keyword% OR " +
	           "a.citoyen.telephone LIKE %:keyword%")
	    Page<Condamnation> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);

}
