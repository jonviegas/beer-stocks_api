package com.jonservices.beerstocks.mapper;

import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.model.Beer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BeerMapper {

    BeerMapper INSTANCE = Mappers.getMapper(BeerMapper.class);

    Beer toModel(BeerDTO beerDTO);

    BeerDTO toDTO(Beer beer);

}
