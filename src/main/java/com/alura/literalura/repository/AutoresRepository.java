package com.alura.literalura.repository;

import com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores, Long> {
    Optional<Autores> findByNombre(String nombre);

    // Metodo para traer los datos de los libros asociados a los autores
    @EntityGraph(attributePaths = "libros")
    @Query("SELECT a FROM Autores a")
    List<Autores> findAllWithLibros();

    // Método para buscar autores vivos en un año específico
    @EntityGraph(attributePaths = "libros")
    List<Autores> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(Integer fechaDeNacimiento, Integer fechaDeFallecimiento);

}
