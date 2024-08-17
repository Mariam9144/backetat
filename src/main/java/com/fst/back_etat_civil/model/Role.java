package com.fst.back_etat_civil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;


    //
   @ManyToMany(fetch = FetchType.EAGER)
    //@JsonIgnore
    private Set<User> users = new HashSet<>();


	public Role(Integer id, ERole name) {
		super();
		this.id = id;
		this.name = name;
	}
   
   

}
