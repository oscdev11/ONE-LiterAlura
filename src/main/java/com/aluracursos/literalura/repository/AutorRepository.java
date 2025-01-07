package com.aluracursos.literalura.repository;

import com.aluracursos.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreContainsIgnoreCase(String nombre);

    @Query("SELECT a FROM Autor a JOIN a.libros l")
    List<Autor> findAutorConLibros();
    
    @Query("SELECT a FROM Autor a JOIN a.libros l WHERE a.anioNacimiento <= :anioFallecimiento and a.anioFallecimiento >= :anioFallecimiento")
    List<Autor> findAutorConLibrosPorAnioAutor(Integer anioFallecimiento);

}
