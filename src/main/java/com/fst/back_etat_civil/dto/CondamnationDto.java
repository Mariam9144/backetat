package com.fst.back_etat_civil.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CondamnationDto {


    private Long id;
    private String juridiction;
    private String natureDelitCrime;
    private Date dateCondamnation;
    private Date dateDetention;
    private Date dateDelitCrime;
    private String quantum;
    private long citoyen;
    
}
