package com.fst.back_etat_civil.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommuneDto {

    private Long id;
    private String nom;
    private String code;
    private String autre;

    private long cercle;
}
