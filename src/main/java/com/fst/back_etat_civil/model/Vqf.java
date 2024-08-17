package com.fst.back_etat_civil.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vqf")
public class Vqf {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)

	    @Column( name = "id")
	    private Long id;
	    
	    @NotNull
	    @Column(unique = true)
	    private String code;


	    @NotNull
	    private String nom;

	    
	    private String autre;


	    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
		private Commune commune;
		
		/*
		 * @OneToMany(mappedBy = "lieuNaissance", cascade = CascadeType.ALL)
		 * 
		 * @JsonProperty(access = Access.WRITE_ONLY) private List<Citoyen> citoyens;
		 */


    
}
