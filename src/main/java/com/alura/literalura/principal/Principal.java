package com.alura.literalura.principal;

import com.alura.literalura.model.*;
import com.alura.literalura.repository.AutoresRepository;
import com.alura.literalura.repository.LibroRepository;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;


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

    private LibroRepository libroRepository;

    private AutoresRepository autoresRepository;


    public Principal(LibroRepository libroRepository, AutoresRepository autoresRepository) {
        this.libroRepository = libroRepository;
        this.autoresRepository = autoresRepository;
    }

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
                    continue; // Reinicia el ciclo si la opción no es válida
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
                        System.out.println("Finalizando la aplicación\nNos vemos..\n\n (\\__/)\n (='.'=)\n (\")_(\")    ...by ©DEYSI"+"\n\n");
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
            try {
                DatosAutor datosAutor = datos.autor().get(0);

                // Buscar si el libro ya existe en la base de datos
                Optional<Libros> libroExistente = libroRepository.findByTitulo(datos.titulo());

                if (libroExistente.isPresent()) {
                    // Si el libro ya existe, solo lo mostramos y no intentamos guardarlo de nuevo
                    System.out.println(libroExistente.get().toString());
                } else {
                    // Buscar si el autor ya existe en la base de datos
                    Optional<Autores> autorExistente = autoresRepository.findByNombre(datosAutor.nombre());

                    Autores autor;
                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                    } else {
                        // Guardar el autor si no existe
                        autor = new Autores(datosAutor);
                        autoresRepository.save(autor);
                    }

                    // Crear el libro con el autor existente o recién creado
                    Libros libro = new Libros(datos);
                    libro.setAutor(autor);
                    libroRepository.save(libro);
                    System.out.println(libro.toString());

                    // Llamar al método crearLibro para asociar el libro al autor
                    crearLibro(datos, autor);
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("No se encontró ningún autor para el libro.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error al buscar o guardar el libro: " + e.getMessage());
            }
        }
    }
    private void crearLibro(DatosLibros datosLibros, Autores autor) {
        // Verificar si el libro ya existe para evitar duplicados
        Optional<Libros> libroExistente = libroRepository.findByTitulo(datosLibros.titulo());
        if (libroExistente.isPresent()) {
            System.out.println("El libro '" + datosLibros.titulo() + "' se a guardado en la Base de Datos.");
        } else {
            Libros libro = new Libros(datosLibros);
            libro.setAutor(autor); // Establecer el autor en el libro
            autor.getLibros().add(libro); // Agregar el libro a la lista de libros del autor
            libroRepository.save(libro); // Guardar el libro
            autoresRepository.save(autor); // Guardar el autor con la lista actualizada de libros
            System.out.println("Libro '" + libro.getTitulo() + "' creado y asociado al autor '" + autor.getNombre() + "'.");
        }
    }


    private void listarLibrosRegistrados() {
        List<Libros> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("---- Libros Registrados ----");
            for (Libros libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void listarAutoresRegistrados() {
        List<Autores> autores = autoresRepository.findAllWithLibros();
        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
        } else {
            System.out.println("---- Autores Registrados ----");
            for (Autores autor : autores) {
                System.out.println(autor.toString());
            }
        }
    }

    private void listarAutoresVivos() {

        try {
            System.out.println("Ingrese el año para listar los autores vivos:");
            int año = teclado.nextInt();
            teclado.nextLine(); // Consumir el salto de línea pendiente

            // Validar que el año ingresado sea válido (por ejemplo, mayor que cero si es un año válido)
            if (año <= 0) {
                System.out.println("El año ingresado no es válido.");
                return;
            }

            // Llamar al método del repositorio para buscar autores vivos en el año especificado
            List<Autores> autores = autoresRepository.findByFechaDeNacimientoLessThanEqualAndFechaDeFallecimientoGreaterThanEqual(año, año);

            if (autores.isEmpty()) {
                System.out.println("No hay autores vivos en el año " + año);
            } else {
                System.out.println("---- Autores Vivos en el Año " + año + " ----");
                for (Autores autor : autores) {
                    System.out.println(autor.toString());
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Debes ingresar un número entero para el año.");
            // Limpiar el buffer del scanner
            teclado.nextLine();
        }
    }

    private void listarLibrosPorIdioma() {
        boolean entradaValida = false;
        Scanner teclado = new Scanner(System.in);


        while (!entradaValida) {
            System.out.println("Ingrese el idioma para listar libros: ");

            // Guía de idiomas válidos
            System.out.println("--- Guía de idiomas válidos ---");
            System.out.println("es - Español");
            System.out.println("en - Inglés");
            System.out.println("fr - Francés");
            System.out.println("fi - Finlandés");
            System.out.println("pt - Portugués");

            String idiomaStr = teclado.nextLine().trim().toLowerCase(); // Leer entrada del usuario y limpiar espacios

            switch (idiomaStr) {
                case "es":
                case "en":
                case "fr":
                case "fi":
                case "pt":
                    try {
                        Idioma idioma = convertirACodigoEnum(idiomaStr); // Convertir el código a enum Idioma
                        List<Libros> librosPorIdioma = libroRepository.findByIdioma(idioma);

                        if (librosPorIdioma.isEmpty()) {
                            System.out.println("No se encontraron libros en el idioma: " + idioma);
                        } else {
                            System.out.println("Libros en el idioma '" + idioma + "':");
                            librosPorIdioma.forEach(libro -> System.out.println(libro.toString()));
                        }
                        entradaValida = true; // Marcar que la entrada fue válida para salir del bucle
                    } catch (IllegalArgumentException e) {
                        System.out.println("No se encontraron libros en el idioma: " + idiomaStr);
                    }
                    break;
                default:
                    System.out.println("Por favor, ingrese un idioma válido.");
                    break;
            }
        }
    }
    // Método para convertir el código de idioma a enum Idioma
    private Idioma convertirACodigoEnum(String codigoIdioma) {
        switch (codigoIdioma) {
            case "es":
                return Idioma.ESPANOL;
            case "en":
                return Idioma.INGLES;
            case "fr":
                return Idioma.FRANCES;
            case "fi":
                return Idioma.FINLANDES;
            case "pt":
                return Idioma.PORTUGUES;
            default:
                throw new IllegalArgumentException("Código de idioma no válido: " + codigoIdioma);
        }
    }

}

