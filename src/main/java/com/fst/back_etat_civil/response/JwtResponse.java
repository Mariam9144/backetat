package com.fst.back_etat_civil.response;

import java.util.Set;

import com.fst.back_etat_civil.model.Role;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Basic";
    private String username;
    private String email;
    private Set<Role> roles;

    public JwtResponse(String jwt, Long id, String username, String email, Set<Role> roles) {

        this.token=jwt;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }
}
