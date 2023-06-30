package ru.bazhenov.librarianapp.mapper;

import ru.bazhenov.librarianapp.dto.AbstractDTO;
import ru.bazhenov.librarianapp.models.AbstractEntity;

public interface Mapper <E extends AbstractEntity, D extends AbstractDTO> {
    E toEntity(D dto);
    D toDTO(E entity);
}
