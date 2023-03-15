package com.projects.blog.utils.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    /**
     *
     * @param source Lista di elementi che devono essere convertiti
     * @param target Classe target in cui copiare gli attributi dalla sorgente
     * @return Lista degli elementi convertiti
     * @param <S> Tipologia degli elementi in lista da convertire
     * @param <T> Tipologia della classe alla quale tutti gli elementi dovranno essere convertiti
     */
    protected <S, T> List<T> mapList(List<S> source, Class<T> target){
        /**
         *  Permette di effettuare il mapping di una lista di elementi in una classe specifica.
         *  In questo caso creo uno stream, applico il metodo map a cui passerò una funzione.
         *  Nella funzione (arrow) vengono passati tutti gli elementi, uno per volta, e questa
         *  restituirà la mappatura eseguita sull'elemento nella classe "target". Il tutto verrà
         *  poi convertito in lista.
         */
        return source.stream().map(elem -> this.map(elem, target)).toList();
    }

    protected <S, T> T map(S source, Class<T> target){
        return modelMapper.map(source, target);
    }
}
