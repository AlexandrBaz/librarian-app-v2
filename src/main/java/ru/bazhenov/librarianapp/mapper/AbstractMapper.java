package ru.bazhenov.librarianapp.mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bazhenov.librarianapp.dto.AbstractDto;
import ru.bazhenov.librarianapp.models.AbstractEntity;

import java.util.Objects;


public class AbstractMapper<E extends AbstractEntity, D extends AbstractDto> implements Mapper<E, D> {
    private ModelMapper modelMapper;
    private final Class<E> entity;
    private final Class<D> dto;

    @Autowired
    public AbstractMapper(Class<E> entity, Class<D> dto) {
        this.entity = entity;
        this.dto = dto;
    }

    @Override
    public E toEntity(D dto) {
        return Objects.isNull(dto) ? null
                : modelMapper.map(dto, entity);
    }

    @Override
    public D toDTO(E entity) {
        return Objects.isNull(entity) ? null
                : modelMapper.map(entity, dto);
    }

    Converter<D, E> toEntityConverter() {
        return mappingContext -> {
            D source = mappingContext.getSource();
            E destination = mappingContext.getDestination();
            mapBookField(source, destination);
            mapPersonField(source, destination);
            return mappingContext.getDestination();
        };
    }

    void mapBookField(D source, E destination) {
    }

    void mapPersonField(D source, E destination) {
    }

    Converter<E, D> toDtoConverter() {
        return mappingContext -> {
            E source = mappingContext.getSource();
            D destination = mappingContext.getDestination();
            mapBookField(source, destination);
            mapPersonField(source, destination);
            mapBookDateExpiration(source, destination);
            mapBookReturnIsExpired(source, destination);
            mapTotalDaysExpire(source, destination);
            return mappingContext.getDestination();
        };
    }

    void mapBookField(E source, D destination) {
    }

    void mapPersonField(E source, D destination) {
    }

    void mapBookDateExpiration(E source, D destination) {
    }

    void mapBookReturnIsExpired(E source, D destination) {
    }

    void mapTotalDaysExpire(E source, D destination) {}

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
