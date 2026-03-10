package com.alura.literatura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DatosLibro(

        @JsonAlias("title")
        String titulo,

        @JsonAlias("authors")
        List<DatosAutor> autores,

        @JsonAlias("languages")
        List<String> idiomas,

        @JsonAlias("download_count")
        Double descargas

) {}