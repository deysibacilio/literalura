package com.alura.literalura.repository;

import com.alura.literalura.model.Autores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutoresRepository extends JpaRepository<Autores, Long> {
    Optional<Autores> findByNombre(String nombre);
    // Método para buscar autores por un rango de fechas
    //@Query("SELECT a FROM Autores a WHERE a.fechaDeNacimiento >= :fechaInicio AND a.fechaDeFallecimiento <= :fechaFin")
    // Método para buscar autores vivos en un año específico
    List<Autores> findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(Integer fechaDeNacimiento, Integer fechaDeFallecimiento);

}
