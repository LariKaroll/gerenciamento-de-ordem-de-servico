package com.projeto.service_user.model.util;

public final class LoginNormalizer {

    private LoginNormalizer() {
    }

    public static String normalizar(String login) {
        if (login == null) {
            return "";
        }
        
        String limpo = login.trim();

        if (limpo.contains("@")) {
            return limpo.toLowerCase();
        }
        
        return limpo.replaceAll("[.\\-]", "");
    }
}
