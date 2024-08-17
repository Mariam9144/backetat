package com.fst.back_etat_civil.util;

public class DataSanitizer {
    public static String removeNullCharacters(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("\u0000", "");
    }
}

