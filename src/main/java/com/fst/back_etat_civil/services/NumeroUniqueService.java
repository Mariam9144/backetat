package com.fst.back_etat_civil.services;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class NumeroUniqueService {

    // Générer des codes de "000A" à "999Z"
    private List<String> generateCodes() {
        List<String> codes = new ArrayList<>();
        for (int i = 0; i <= 999; i++) {
            for (char c = 'A'; c <= 'Z'; c++) {
                codes.add(String.format("%03d%c", i, c));
            }
        }
        //Collections.shuffle(codes); // Mélanger la liste
        return codes;
    }

    // Générer un numéro unique combinant l'année de naissance, le code de commune, le genre et un code
    public String generateNumeroUnique(String cle,int seq) {
        List<String> codes = generateCodes();
        // Sélectionner le premier code de la liste mélangée 25999
        String code = codes.get(seq);
        // Combiner la cle et le numero de sequence de 000A a 999Z
        String numeroUnique = String.format("%s%s", cle, code);
        return numeroUnique;
    }
    
}
