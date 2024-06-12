package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosAutor(
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") String fechaDeNacimiento,
        @JsonAlias("death_year") String fechaDeFallecimiento
) {
    @Override
    public String nombre() {
        return nombre;
    }

    @Override
    public String fechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    @Override
    public String fechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }
}
