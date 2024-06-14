package com.alura.literalura.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombre;

    private Integer fechaDeNacimiento;

    private Integer fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Libros> libros = new ArrayList<>();

    public Autores() {
    }

    public Autores(DatosAutor datosAutores) {
        this.nombre = datosAutores.nombre();
        this.fechaDeNacimiento = datosAutores.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutores.fechaDeFallecimiento();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(Integer fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public Integer getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(Integer fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libros> getLibros() {
        return libros;
    }

    public void setLibros(List<Libros> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        StringBuilder librosToString = new StringBuilder();
        try {
            Hibernate.initialize(libros); // Intenta inicializar la colección libros
            if (libros != null) {
                for (Libros libro : libros) {
                    librosToString.append(libro.getTitulo()).append(", ");
                }
                if (librosToString.length() > 0) {
                    librosToString.setLength(librosToString.length() - 2); // Eliminar la última coma y espacio
                }
            }
            return "Autores{" +
                    "nombre='" + nombre + '\'' +
                    ", fechaDeNacimiento=" + fechaDeNacimiento +
                    ", fechaDeFallecimiento=" + fechaDeFallecimiento +
                    ", libros=[" + librosToString.toString() + "]" +
                    '}';
        } catch (LazyInitializationException e) {
            return "Autores{" +
                    "nombre='" + nombre + '\'' +
                    ", fechaDeNacimiento=" + fechaDeNacimiento +
                    ", fechaDeFallecimiento=" + fechaDeFallecimiento +
                    ", libros=No inicializado" +
                    '}';
        }
    }
}
