package com.fst.back_etat_civil.request;

import lombok.Data;

import java.util.Set;

@Data

public class SignupRequest {
    private String username ;
    private  String password ;
    private String email ;
    private Set<String> role ;


}
