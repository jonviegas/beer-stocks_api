package com.jonservices.beerstocks.builder;

import com.jonservices.beerstocks.data.dto.BeerDTO;
import lombok.Builder;

@Builder
public class BeerDTOBuilder {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String name = "Skol Beats";

    @Builder.Default
    private String brand = "Ambev";

    @Builder.Default
    private int max = 5;

    @Builder.Default
    private int quantity = 2;

    public BeerDTO toBeerDTO() {
        return new BeerDTO(id,
                name,
                brand,
                max,
                quantity);
    }

    public BeerDTO aDifferentOne() {
        BeerDTO beerDTO = toBeerDTO();
        beerDTO.setId(2L);
        beerDTO.setName("Brahma");
        beerDTO.setMax(10);
        beerDTO.setQuantity(5);
        return beerDTO;
    }

}
