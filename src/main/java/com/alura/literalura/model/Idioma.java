package com.alura.literalura.model;

public enum Idioma {

    ESPANOL("es"),
    INGLES("en"),
    FRANCES("fr"),
    FINLANDES("fi"),
    PORTUGUES("pt");

    private String idiomaLibros;

    Idioma (String idiomaLibros){
        this.idiomaLibros = idiomaLibros;
    }

    public static Idioma fromString(String text){
        for (Idioma idioma : Idioma.values()){
            if(idioma.idiomaLibros.equalsIgnoreCase(text)){
                return idioma;
            }
        }
        throw new IllegalArgumentException("Ningun idioma encontrado: "+ text);
    }
}
