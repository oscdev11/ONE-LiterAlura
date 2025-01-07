package com.aluracursos.literalura.model;

import com.aluracursos.literalura.dto.LibroRecord;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 500)
    private String titulo;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "libro_autor",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private List<Autor> autores;
    private String lenguaje;
    private Double numeroDescargas;

    public Libro() {
    }

    public Libro(LibroRecord libroRecord) {
        this.titulo = libroRecord.titulo();
        this.lenguaje = String.join(", ", libroRecord.idiomas());
        this.numeroDescargas = libroRecord.numeroDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autor) {
        this.autores = autor;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(String lenguaje) {
        this.lenguaje = lenguaje;
    }

    public Double getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(Double numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    @Override
    public String toString() {
        return "\n------ LIBRO ------\n" +
                "Titulo: '" + titulo +
                "\nAutores: \n" + (autores != null ? autores.stream().map(Autor::toString).collect(Collectors.joining("\n")) : "No hay datos de autores") +
                "\nIdiomas: " + lenguaje +
                "\nNúmero de descargas: " + numeroDescargas +
                "\n-------------------\n";
    }
}
