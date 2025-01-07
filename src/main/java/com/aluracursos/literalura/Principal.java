package com.aluracursos.literalura;

import com.aluracursos.literalura.repository.AutorRepository;
import com.aluracursos.literalura.repository.LibroRepository;
import com.aluracursos.literalura.service.RespuestaService;

import java.util.Scanner;

public class Principal {

    private final Scanner obj = new Scanner(System.in);
    private RespuestaService service = new RespuestaService();
    private LibroRepository libroRepository;
    private AutorRepository autorRepository;

    public Principal(LibroRepository repository, AutorRepository autorRepository) {
        this.libroRepository = repository;
        this.autorRepository = autorRepository;
    }

    public void muestraMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    \n---------------------------
                    Elija la opción a través de su número: 
                    
                    1. Buscar libro por título
                    2. Listar libros registrados
                    3. Listar autores registrados
                    4. Listar autores vivos en un determinado año
                    5. Listar libros por idioma
                    0. Salir
                    """;

            System.out.println(menu);
            opcion = obj.nextInt();
            obj.nextLine();

            selector(opcion);
        }
    }

    private void selector(int opcion) {
        switch (opcion) {
            case 1:
                service.buscarLibroApi(libroRepository, autorRepository);
                break;
            case 2:
                service.listarLibrosRegistrados(libroRepository);
                break;
            case 3:
                service.listarAutoresRegistrados(autorRepository);
                break;
            case 4:
                service.listarAutoresVivosPorAnio(autorRepository);
                break;
            case 5:
                service.listarLibrosPorIdioma(libroRepository);
                break;
            case 0:
                System.out.println("Cerrando la aplicación...");
                break;
        }
    }
}
