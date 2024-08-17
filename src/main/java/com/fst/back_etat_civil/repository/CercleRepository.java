package com.fst.back_etat_civil.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fst.back_etat_civil.model.Cercle;
import com.fst.back_etat_civil.model.Region;

@Repository
public interface CercleRepository extends JpaRepository<Cercle, Long> {
    //List<Cercle> findByNom(String nom);
Cercle findByCode(String code);
List<Cercle> findByRegion(Region region);
Boolean existsByCode(String code);
Boolean existsByNomIgnoreCase(String code);

@Query("SELECT a FROM Cercle a WHERE " +
		   "(CAST(a.id AS string) LIKE %:keyword% OR " +
		   "a.nom LIKE %:keyword% OR " +
		   "a.code LIKE %:keyword% OR " +
		   "a.region.nom LIKE %:keyword% OR " +
		   "a.autre LIKE %:keyword% ) AND "
		   + "a.region.nom <> 'Diaspora'")
 Page<Cercle> searchByKeywordInAllColumns(@Param("keyword") String keyword, Pageable pageable);

}
