package com.fst.back_etat_civil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VqfDto {


    private Long id;
    private String nom;
    private String code;
    private String autre;
    private long commune;
}
