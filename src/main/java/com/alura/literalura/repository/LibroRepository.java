package com.alura.literalura.repository;

import com.alura.literalura.model.Idioma;
import com.alura.literalura.model.Libros;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libros,Long> {
    @EntityGraph(attributePaths = {"autor"})
    Optional<Libros> findByTitulo(String titulo);
    @EntityGraph(attributePaths = {"autor"})
    List<Libros> findAll();
    @EntityGraph(attributePaths = {"autor"})
    List<Libros> findByIdioma(Idioma idioma);

}
