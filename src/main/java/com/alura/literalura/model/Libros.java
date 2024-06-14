package com.alura.literalura.model;

import jakarta.persistence.*;

@Entity
@Table(name="libros")
public class Libros {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY)  // Usamos LAZY para evitar cargar innecesariamente los datos
    @JoinColumn(name = "autor_id", nullable = false)
    private Autores autor;

    @Enumerated(EnumType.STRING)
    private Idioma idioma;

    private Double numeroDeDescargas;


    public Libros(){}

    public Libros(DatosLibros datosLibros) {
       this.titulo = datosLibros.titulo();
       this.autor =  new Autores(datosLibros.autor().get(0));
       this.idioma = Idioma.fromString(datosLibros.idioma().get(0));
       this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autores getAutor() {
        return autor;
    }

    public void setAutor(Autores autor) {
        this.autor = autor;
    }

    public Idioma getIdioma() {
        return idioma;
    }

    public void setIdioma(Idioma idioma) {
        this.idioma = idioma;
    }

    public Double getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Double numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    @Override
    public String toString() {
        return "\n****** LIBRO ******"+
                "\n  Título: '" + titulo + '\'' +
                ",\n  Autor: " + (autor != null ? autor.getNombre() : "Desconocido") +
                ",\n  Idioma: " + idioma +
                ",\n  Número de Descargas: " + numeroDeDescargas +
                "\n*******************";
    }
}
