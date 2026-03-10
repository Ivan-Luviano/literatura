package com.alura.literatura.principal;

import com.alura.literatura.dto.DatosLibro;
import com.alura.literatura.dto.DatosRespuesta;
import com.alura.literatura.model.Autor;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;

import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();

    private AutorRepository autorRepository;
    private LibroRepository libroRepository;

    private final String URL_BASE = "https://gutendex.com/books/?search=";

    public Principal(AutorRepository autorRepository, LibroRepository libroRepository) {
        this.autorRepository = autorRepository;
        this.libroRepository = libroRepository;
    }

    public void muestraMenu() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("""
                    
                    ===== LITERALURA =====
                    
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    
                    """);

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {

                case 1 -> buscarLibro();
                case 2 -> listarLibros();
                case 3 -> listarAutores();
                case 4 -> autoresVivosPorAnio();
                case 5 -> librosPorIdioma();
                case 0 -> System.out.println("Cerrando aplicación...");
                default -> System.out.println("Opción inválida");

            }
        }
    }

    private void buscarLibro() {

        System.out.println("Escribe el nombre del libro:");
        var nombreLibro = teclado.nextLine();

        var json = consumoApi.obtenerDatos(URL_BASE + nombreLibro.replace(" ", "+"));

        DatosRespuesta datos = conversor.obtenerDatos(json, DatosRespuesta.class);

        if (datos.resultados().isEmpty()) {
            System.out.println("No se encontraron libros con ese nombre.");
            return;
        }

        DatosLibro libro = datos.resultados().get(0);

        System.out.println("\n========== LIBRO ENCONTRADO ==========");
        System.out.println("Título: " + libro.titulo());
        System.out.println("Autor: " + libro.autores().get(0).nombre());
        System.out.println("Idioma: " + libro.idiomas().get(0));
        System.out.println("Descargas: " + libro.descargas());
        System.out.println("======================================");

        String nombreAutor = libro.autores().get(0).nombre();

        Optional<Autor> autorExistente = autorRepository.findByNombre(nombreAutor);

        Autor autor;

        if (autorExistente.isPresent()) {
            autor = autorExistente.get();
        } else {
            autor = new Autor(
                    libro.autores().get(0).nombre(),
                    libro.autores().get(0).nacimiento(),
                    libro.autores().get(0).muerte()
            );
            autorRepository.save(autor);
        }

        Libro libroEntidad = new Libro(
                libro.titulo(),
                libro.idiomas().get(0),
                libro.descargas(),
                autor
        );

        libroRepository.save(libroEntidad);

        System.out.println("Libro guardado en la base de datos.\n");
    }

    private void listarLibros() {

        var libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        libros.forEach(libro -> {
            System.out.println("\n========== LIBRO ==========");
            System.out.println("Título: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Descargas: " + libro.getDescargas());
            System.out.println("===========================");
        });
    }

    private void listarAutores() {

        var autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados.");
            return;
        }

        autores.forEach(autor -> {
            System.out.println("\n========== AUTOR ==========");
            System.out.println("Nombre: " + autor.getNombre());
            System.out.println("Nacimiento: " + autor.getFechaNacimiento());
            System.out.println("Muerte: " + autor.getFechaMuerte());
            System.out.println("===========================");
        });
    }

    private void autoresVivosPorAnio() {

        System.out.println("Ingrese el año:");
        int anio = teclado.nextInt();
        teclado.nextLine();

        var autores = autorRepository.findAll();

        autores.stream()
                .filter(a -> a.getFechaNacimiento() != null && a.getFechaNacimiento() <= anio)
                .filter(a -> a.getFechaMuerte() == null || a.getFechaMuerte() >= anio)
                .forEach(a -> System.out.println("Autor: " + a.getNombre()));
    }

    private void librosPorIdioma() {

        System.out.println("""
            
            Ingrese idioma:
            es - Español
            en - Inglés
            fr - Francés
            pt - Portugués
            
            """);

        String idioma = teclado.nextLine();

        var libros = libroRepository.findByIdioma(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
            return;
        }

        libros.forEach(libro -> {
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor().getNombre());
        });
    }
}