package com.jonservices.beerstocks.mocks;

import com.jonservices.beerstocks.builder.BeerDTOBuilder;
import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.QuantityDTO;
import com.jonservices.beerstocks.data.model.Beer;
import com.jonservices.beerstocks.mapper.BeerMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BeerTestMocks {

    public static final String BEER_API_URL_PATH = "/beers";
    public static final BeerMapper BEER_MAPPER = BeerMapper.INSTANCE;
    public static final BeerDTO EXPECTED_BEER_DTO = BeerDTOBuilder.builder().build().toBeerDTO();
    public static final BeerDTO ANOTHER_BEER_DTO = BeerDTOBuilder.builder().build().aDifferentOne();
    public static final Beer EXPECTED_BEER = BEER_MAPPER.toModel(EXPECTED_BEER_DTO);
    public static final long VALID_BEER_ID = EXPECTED_BEER_DTO.getId();
    public static final long INVALID_BEER_ID = 0L;
    public static final String VALID_BEER_NAME = EXPECTED_BEER_DTO.getName();
    public static final String INVALID_BEER_NAME = "Sol";
    public static final QuantityDTO VALID_QUANTITY_DTO = new QuantityDTO(3);
    public static final QuantityDTO INVALID_QUANTITY_DTO = new QuantityDTO(30);
    public static final List<BeerDTO> ALL_BEERS_DTO = Arrays.asList(EXPECTED_BEER_DTO, ANOTHER_BEER_DTO);
    public static final List<Beer> ALL_BEERS = ALL_BEERS_DTO.stream().map(BEER_MAPPER::toModel).collect(Collectors.toList());
    public static final Optional<Beer> EXPECTED_OPTIONAL_BEER = Optional.of(BEER_MAPPER.toModel(EXPECTED_BEER_DTO));
    
}
