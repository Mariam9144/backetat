package com.fst.back_etat_civil.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "condamnation")
public class Condamnation {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    private String juridiction;

    @NotNull
    @Column( name = "natureDelitCrime")
    private String natureDelitCrime;
    
    private String quantum;

    
    private Date dateCondamnation;
    
    
    private Date dateDetention;
    
   
    private Date dateDelitCrime;
    
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Citoyen citoyen;

}
