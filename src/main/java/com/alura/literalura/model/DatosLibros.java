package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(

        @JsonAlias("title") String titulo,

        @JsonAlias("authors") List<DatosAutor> autor,

        @JsonAlias("languages") List<String> idioma,

        @JsonAlias("download_count") Double numeroDeDescargas
) {

    //    public String toString() {
//        return "\n-----LIBRO-----"+
//                "\nTitulo: " + titulo +
//                "\nAutor: " + autor +
//                "\nIdiomas: " + idiomas+ //creo que puedo crear una clase de idiomas
//                "\nNumero de descargas: " + numeroDeDescargas +
//                "\n---------------";
//    }
//    @Override
//    public String toString() {
//        return "\n-----LIBRO-----" +
//                "\nTitulo: " + titulo +
//                "\nAutor: " + autor.stream().map(DatosAutor::nombre).collect(Collectors.joining(",")) +
//                "\nIdiomas: " + idioma.stream().collect(Collectors.joining(",")) +
//                "\nNumero de descargas: " + numeroDeDescargas +
//                "\n---------------";
//    }
}
