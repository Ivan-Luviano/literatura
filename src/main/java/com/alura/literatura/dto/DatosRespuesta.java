package com.alura.literatura.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DatosRespuesta(

        @JsonAlias("results")
        List<DatosLibro> resultados

) {}
