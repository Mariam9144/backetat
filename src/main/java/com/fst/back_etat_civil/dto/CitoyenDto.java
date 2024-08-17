package com.fst.back_etat_civil.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitoyenDto {

    private Long id;

    private String niciv;


    private String nom;

    private String prenom;

    private Date dateNaissance;

    private String telephone;

    private long profession;

    private String genre;


    private String portrait;;


    private String civilite;

    private String prenomPere;

  
    private long professionPere;

    private String nomMere;

    private String prenomMere;

    private long professionMere;

    private long adresse;
    
    private long lieuNaissance;
    
    private String rue;

    private String porte;
    
    private String autre;
    
    
    private String cle;

}
