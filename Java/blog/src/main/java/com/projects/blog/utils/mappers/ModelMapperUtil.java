package com.projects.blog.utils.mappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ModelMapperUtil {

    private final ModelMapper modelMapper;

    protected <S, T> List<T> mapList(List<S> source, Class<T> target){
        return source.stream().map(elem -> modelMapper.map(elem, target)).toList();
    }

    protected <S, T> T map(S source, Class<T> target){
        return modelMapper.map(source, target);
    }
}
