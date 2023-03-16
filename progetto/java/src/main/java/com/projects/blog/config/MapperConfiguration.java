package com.projects.blog.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {


    /**
     * Metodo per la creazione di un Bean che permette di semplificare la mappatura tra oggetti diversi. Questo permetterà
     * di copiare valori provienienti da un oggetto ad un altro. In questo modo si rispetta il DRY e non si scrive codice
     * ripetitivo solo per la copia di valori.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
