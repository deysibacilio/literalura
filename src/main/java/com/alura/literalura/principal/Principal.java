package com.alura.literalura.principal;

import com.alura.literalura.model.Datos;
import com.alura.literalura.model.DatosAutor;
import com.alura.literalura.model.DatosLibros;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private static final String SEARCH = "?search=";

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    private ConvierteDatos conversor = new ConvierteDatos();

    private Scanner teclado = new Scanner(System.in);

    private List<DatosLibros> datosLibros = new ArrayList<>();

    private List<DatosAutor> datosAutores = new ArrayList<>();

    public void muestraElMenu() {
        var opcion = -1;
        var numeroOpciones = 5; // número total de opciones en el menú
        do {
            var menu = """
                \n------------ BIENVENIDO -------------
                Elija la opción a través de su número:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                """;

            System.out.println(menu);

            try {
                opcion = Integer.parseInt(teclado.nextLine()); // Leer la opción como entero
                if (opcion < 0 || opcion > numeroOpciones) {
                    System.out.println("Opción fuera de rango. Por favor, elija una opción válida.");
                    continue; // Reiniciar el ciclo si la opción no es válida
                }

                switch (opcion) {
                    case 1:
                        buscarLibro();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Finalizando la aplicación\nNos vemos..\n\n (\\__/)\n (='.'=)\n (\")_(\") ");
                        break;
                    default:
                        System.out.println("Opción inválida");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese solo números.");
            }
        } while (opcion != 0);
    }


    // ----- MÉTODOS PRINCIPALES ------

    private DatosLibros getDatosLibro(){
        System.out.println("Ingrese el titulo del libro que desea buscar");
        String tituloLibro = teclado.nextLine();
        String json = consumoAPI.obtenerDatos(URL_BASE + SEARCH + tituloLibro.replace(" ", "+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
           // System.out.println("---- Libro encontrado -----");
            DatosLibros datos = libroBuscado.get();
            datosAutores.addAll(datos.autor());
            return datos;
        }else{
            System.out.println("Libro no encontrado: " + tituloLibro);
            return null;
        }

    }

    private void buscarLibro() {
        DatosLibros datos = getDatosLibro();
        if (datos != null) {
            datosLibros.add(datos);
            System.out.println(datos);
        }
    }

    private void listarLibrosRegistrados() {
        datosLibros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        datosAutores.forEach(System.out::println);
    }

    private void listarAutoresVivos() {

        System.out.println("Ingrese el año para listar autores vivos:");
        int año = teclado.nextInt();
        teclado.nextLine(); // Consumir el salto de línea después del número

        List<DatosAutor> autoresVivos = new ArrayList<>();

        for (DatosAutor autor : datosAutores) {
            // Verificar si el autor está vivo en el año especificado
            if (autor.fechaDeFallecimiento() == null || autor.fechaDeFallecimiento() > año) {
                autoresVivos.add(autor);
            }
        }

        // Mostrar los autores vivos
        if (autoresVivos.isEmpty()) {
            System.out.println("No hay autores vivos en el año " + año);
        } else {
            System.out.println("Autores vivos en el año " + año + ":");
            for (DatosAutor autor : autoresVivos) {
                System.out.println(autor.nombre() + " (nacido en " + autor.fechaDeNacimiento() + ")");
            }
        }

    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingrese el idioma para listar libros:");
        String idioma = teclado.nextLine();

        List<DatosLibros> librosPorIdioma = datosLibros.stream()
                .filter(libro -> libro.idiomas().contains(idioma))
                .collect(Collectors.toList());

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma);
        } else {
            System.out.println("Libros en el idioma '" + idioma + "':");
            librosPorIdioma.forEach(System.out::println);
        }
    }

}



//private DatosLibros getDatosLibro(){
//    System.out.println("Ingrese el titulo del libro que desea buscar");
//    String tituloLibro = teclado.nextLine();
//    String json = consumoAPI.obtenerDatos(URL_BASE + SEARCH + tituloLibro.replace(" ", "+"));
//    System.out.println(json);
//    DatosLibros datos = conversor.obtenerDatos(json, DatosLibros.class);
//    return datos;
//}



//metodo de buscar libro

//Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
//        .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase())) //el titulo del libro tiene ese valor "titulolibro", y colocar el libro en mayuscula
//        .findFirst(); //traer el primer resultado
//
//        if (libroBuscado.isPresent()) {
//        System.out.println(libroBuscado.get().toString());
//        } else {
//        System.out.println("Libro no encontrado");
//        }


// ----------- OBTENER DATOS DEL API ------

//var json = consumoAPI.obtenerDatos(URL_BASE);
//        System.out.println(json);
//var datos = conversor.obtenerDatos(json, Datos.class);
//        System.out.println(datos);


//--------TRABAJANDO CON ESTADISTICAS---------

//DoubleSummaryStatistics est = datos.resultados().stream()
//        .filter(d -> d.numeroDeDescargas() > 0)
//        .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
//
//        System.out.println("Cantidad media de descargas: " + est.getAverage());
//        System.out.println("Cantidad máxima de descargas: " + est.getMax());
//        System.out.println("Cantidad mínima de descargas: " + est.getMin());
//        System.out.println("Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount());


//----------10 LIBROS MÁS DESCARGADOS-----------

// System.out.println("Top 10 libros más descargados");
//        datos.resultados().stream() //dentro de la lista de resultados vamos a usar los streams
//                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed()) //necesitamos ordenar y comparar cual es el que tiene mayor o menor descargas, y luego lo ponemos al revés
//        .limit(10) //limitar a 10 libros
//                .map(l -> l.titulo().toUpperCase()) //convertir a mayuscula
//        .forEach(System.out::println); //imprime los 10 libros