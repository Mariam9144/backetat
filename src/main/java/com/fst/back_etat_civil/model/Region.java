package com.fst.back_etat_civil.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Data
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "region")
public class Region {

    public Region(long l, String string, String string2, String string3) {
		// TODO Auto-generated constructor stub
	}


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column( name = "id")
    private Long id;
    
    @NotNull
    @Column(unique = true)
    private String code;


    @NotNull
    @Column(unique = true)
    private String nom;

    
    private String autre;


    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
	@JsonProperty(access = Access.WRITE_ONLY)
	private List<Cercle> cercles;

    
}
