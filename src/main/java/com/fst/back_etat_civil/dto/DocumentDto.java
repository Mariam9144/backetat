package com.fst.back_etat_civil.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {


    private Long id;
    private String nom;
    private String type;
    private long citoyen;
}
