package ru.bazhenov.librarianapp.mapper;

import ru.bazhenov.librarianapp.dto.AbstractDto;
import ru.bazhenov.librarianapp.models.AbstractEntity;

public interface Mapper <E extends AbstractEntity, D extends AbstractDto> {
    E toEntity(D dto);
    D toDTO(E entity);
}
