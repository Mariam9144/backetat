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
@Table(name = "document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column( name = "id")
    private Long id;


    @NotNull
    @Column( name = "nom")
    private String nom;

    @NotNull
    @Column( name = "type")
    private String type;

    @Temporal(TemporalType.DATE)
    @Column(name = "date", columnDefinition = "DATE DEFAULT CURRENT_DATE", insertable = false, updatable = false)
    private Date date;
    
    @ManyToOne //(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Citoyen citoyen;

}
