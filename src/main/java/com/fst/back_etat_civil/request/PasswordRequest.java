package com.fst.back_etat_civil.request;

import lombok.Data;

@Data
public class PasswordRequest {
	private String password ;
    private  String newPassword ;
    private String confirmation ;
    
}
