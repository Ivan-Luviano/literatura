package com.alura.literatura.service;

import org.springframework.web.client.RestTemplate;

public class ConsumoAPI {
    public String obtenerDatos(String url) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, String.class);
    }

}

