package com.alura.literatura.model;

import jakarta.persistence.*;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String idioma;
    private Double descargas;

    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro(String titulo, String idioma, Double descargas, Autor autor){
        this.titulo = titulo;
        this.idioma = idioma;
        this.descargas = descargas;
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public Double getDescargas() {
        return descargas;
    }

    public Autor getAutor() {
        return autor;
    }
}