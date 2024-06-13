package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer fechaDeNacimiento,
        @JsonAlias("death_year") Integer fechaDeFallecimiento
       // @JsonAlias("title") List<DatosLibros> libros
) {
    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public Integer fechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    @Override
    public Integer fechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

//    @Override
//    public List<DatosLibros> libros() {
//        return libros;
//    }

    @Override
    public String toString() {
        return "DatosAutor{" +
                "nombre='" + nombre + '\'' +
                ", fechaDeNacimiento='" + fechaDeNacimiento + '\'' +
                ", fechaDeFallecimiento='" + fechaDeFallecimiento + '\'' +
               //", libros=" + libros.stream().map(DatosLibros::titulo).collect(Collectors.joining(";")) +
                '}';
    }
}
