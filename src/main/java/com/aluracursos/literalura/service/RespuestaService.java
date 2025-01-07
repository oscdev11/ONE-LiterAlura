package com.aluracursos.literalura.service;

import com.aluracursos.literalura.dto.AutorRecord;
import com.aluracursos.literalura.dto.LibroRecord;
import com.aluracursos.literalura.dto.RespuestaRecord;
import com.aluracursos.literalura.model.Autor;
import com.aluracursos.literalura.model.Libro;
import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RespuestaService {
    private final Scanner obj = new Scanner(System.in);
    private final String BASE_URL = "https://gutendex.com/books/?search=";
    private final ConvierteDatos convierteDatos = new ConvierteDatos();

    public void buscarLibroApi(LibroRepository libroRepository, AutorRepository autorRepository) {
        System.out.println("Ingrese el nombre del libro que desea buscar:");
        var tituloLibro = obj.nextLine();
        var libroEnBD = libroRepository.findByTituloContainsIgnoreCase(tituloLibro.trim());

        if (libroEnBD.isPresent()) {
            System.out.println("\nEl libro ya existe: " + libroEnBD.get());
        } else {
            var url = BASE_URL + tituloLibro.replace(" ", "%20");
            var json = ConsumoApi.obtenerDatos(url);
            RespuestaRecord respuesta = convierteDatos.obtenerDatos(json, RespuestaRecord.class);

            if (respuesta.libros() != null && !respuesta.libros().isEmpty()) {
                LibroRecord libroRecord = respuesta.libros().get(0);
                Libro nuevoLibro = new Libro(libroRecord);

                nuevoLibro.setAutores(new ArrayList<>());
                libroRepository.save(nuevoLibro);

//                List<Autor> autores = libroRecord.autores().stream().map(autor -> autorRepository.findByNombreContainsIgnoreCase(autor.nombre()).orElseGet(() -> {
//                    Autor nuevoAutor = new Autor(autor);
//                    return autorRepository.save(nuevoAutor);
//                })).toList();

                List<Autor> autores = new ArrayList<>();
                for (AutorRecord autorRecord : libroRecord.autores()) {
                    Autor autor;
                    Optional<Autor> autorExistente = autorRepository.findByNombreContainsIgnoreCase(autorRecord.nombre());

                    if (autorExistente.isPresent()) {
                        autor = autorExistente.get();
                    } else {
                        autor = new Autor(autorRecord);
                        autor = autorRepository.save(autor);
                    }
                    autores.add(autor);
                }

                nuevoLibro.setAutores(autores);
                libroRepository.save(nuevoLibro);

                System.out.println("\nLibro guardado:\n " + nuevoLibro);
            } else {
                System.out.println("Libro no encontrado");
            }
        }
    }

    public void listarLibrosRegistrados(LibroRepository repository) {
        List<Libro> libros = repository.findAll();
        if (!libros.isEmpty()) {
            libros.forEach(System.out::println);
        } else {
            System.out.println("Aún no has registrado libros");
        }
    }

    public void listarAutoresRegistrados(AutorRepository autorRepository) {
        List<Autor> autores = autorRepository.findAutorConLibros();
        if (!autores.isEmpty())
            imprimirInformacionAutores(autores);
        else
            System.out.println("Aún no hay autores registrados");
    }

    public void listarAutoresVivosPorAnio(AutorRepository autorRepository) {
        System.out.println("Ingrese el año vivo de autor(es) que desea buscar: d");
        Integer anioFallecimiento = obj.nextInt();
        try {
            List<Autor> autores = autorRepository.findAutorConLibrosPorAnioAutor(anioFallecimiento);
            if (!autores.isEmpty())
                imprimirInformacionAutores(autores);
            else
                System.out.println("No existen autores vivos en este año");
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un número válido: " + e.getMessage());
        }
    }

    private void imprimirInformacionAutores(List<Autor> autores) {
        autores.forEach(a -> {
            String librosString = a.getLibros().stream().map(Libro::getTitulo).collect(Collectors.joining(" | "));
            System.out.println(
                    "-----------------------" +
                            "\nNombre: " + a.getNombre() +
                            "\nFecha de nacimiento: " + a.getAnioNacimiento() +
                            "\nFecha de fallecimiento: " + a.getAnioFallecimiento() +
                            "\nLibros: " + librosString +
                            "\n-----------------------\n");
        });
    }

    public void listarLibrosPorIdioma(LibroRepository libroRepository) {
        System.out.println("""
                Ingrese el idioma para buscar los libros: 
                es - Español
                en - Inglés
                fr - Frances
                pt - Portugues
                hu - Hungarius
                """);
        String idioma = obj.next();
        List<Libro> libros = libroRepository.findByLenguajeContainsIgnoreCase(idioma);
        if (!libros.isEmpty())
            libros.forEach(System.out::println);
        else
            System.out.println("No existen libros registrados con el idioma: " + idioma);
    }
}
