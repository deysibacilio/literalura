package com.alura.literalura.principal;

import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String SEARCH = "?search=";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvierteDatos conversor = new ConvierteDatos();

    private Scanner teclado = new Scanner(System.in);

    public void muestraElMenu(){

        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);



        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream() //dentro de la lista de resultados vamos a usar los streams
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed()) //necesitamos ordenar y comparar cual es el que tiene mayor o menor descargas, y luego lo ponemos al revés
                .limit(10) //limitar a 10 libros
                .map(l -> l.titulo().toUpperCase()) //convertir a mayuscula
                .forEach(System.out::println); //imprime los 10 libros

        //Busqueda de libros por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+SEARCH+tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase())) //el titulo del libro tiene ese valor "titulolibro", y colocar el libro en mayuscula
                .findFirst(); //traer el primer resultado

        if (libroBuscado.isPresent()){
            System.out.println(libroBuscado.get().toString());
        }else{
            System.out.println("Libro no encontrado");
        }


    }
}

////Trabajando con estadisticas
//DoubleSummaryStatistics est = datos.resultados().stream()
//        .filter(d -> d.numeroDeDescargas() > 0)
//        .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
//
//        System.out.println("Cantidad media de descargas: " + est.getAverage());
//        System.out.println("Cantidad máxima de descargas: " + est.getMax());
//        System.out.println("Cantidad mínima de descargas: " + est.getMin());
//        System.out.println("Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount());