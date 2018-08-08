package com.example.internshipapp.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public interface DtoMapper<Entity, DTO> {

    DTO toDto(Entity entity);

    Entity toEntity(DTO dto);

    default List<DTO> listToDtos(List<Entity> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default Page<DTO> pageToDtos(Page<Entity> entities) {
        List<DTO> dtoList = entities.stream().map(this::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtoList, entities.getPageable(), entities.getTotalElements());
    }
}
