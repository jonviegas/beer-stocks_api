package com.jonservices.beerstocks.service;

import com.jonservices.beerstocks.builder.BeerDTOBuilder;
import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.MessageResponseDTO;
import com.jonservices.beerstocks.data.enums.StockAction;
import com.jonservices.beerstocks.data.model.Beer;
import com.jonservices.beerstocks.exceptions.BeerAlreadyRegisteredException;
import com.jonservices.beerstocks.exceptions.BeerNotFoundException;
import com.jonservices.beerstocks.exceptions.BeerStockExceededException;
import com.jonservices.beerstocks.repository.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.jonservices.beerstocks.mocks.BeerTestMocks.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BeerServiceTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerService beerService;

    @Test // Find all
    @DisplayName("Should return all registered beers")
    void shouldReturnAllRegisteredBeers() {
        // when
        when(beerRepository.findAll()).thenReturn(ALL_BEERS);
        final List<BeerDTO> returnedBeersDTOList = beerService.findAll();

        // then
        assertThat(returnedBeersDTOList).isNotEmpty();
        assertThat(returnedBeersDTOList).isEqualTo(ALL_BEERS_DTO);
    }

    @Test // Find by id
    @DisplayName("When registered beer searched by its id then it should be returned")
    void whenRegisteredBeerSearchedByItsIdThenItShouldBeReturned() {
        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(EXPECTED_OPTIONAL_BEER);
        final BeerDTO returnedBeerDTO = beerService.findById(VALID_BEER_ID);

        // then
        assertThat(returnedBeerDTO).isEqualTo(EXPECTED_BEER_DTO);
    }

    @Test // Find by name
    @DisplayName("When registered beer searched by its name then it should be returned")
    void whenRegisteredBeerSearchedByItsNameThenItShouldBeReturned() {
        // given
        final String beerName = EXPECTED_BEER_DTO.getName();

        // when
        when(beerRepository.findByName(beerName)).thenReturn(Optional.of(EXPECTED_BEER));
        final BeerDTO returnedBeerDTO = beerService.findByName(beerName);

        // then
        assertThat(returnedBeerDTO).isEqualTo(EXPECTED_BEER_DTO);
    }

    @Test // Save beer
    @DisplayName("When beer informed then it should be created")
    void whenBeerInformedThenItShouldBeCreated() {
        // when
        when(beerRepository.save(EXPECTED_BEER)).thenReturn(EXPECTED_BEER);
        final BeerDTO returnedBeerDTO = beerService.create(EXPECTED_BEER_DTO);

        // then
        assertThat(returnedBeerDTO).isEqualTo(EXPECTED_BEER_DTO);
    }

    @Test // Increment
    @DisplayName("Should increase total quantity in stock of a beer")
    void shouldIncreaseTotalQuantityInStockOfABeer() throws Exception {
        // given
        final Beer expectedBeerToIncrementStock = BEER_MAPPER.toModel(BeerDTOBuilder.builder().build().toBeerDTO());
        final int quantityToIncrement = VALID_QUANTITY_DTO.getQuantity();

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(expectedBeerToIncrementStock));
        beerService.updateStock(VALID_BEER_ID, quantityToIncrement, StockAction.INCREMENT);

        // ArgumentCaptor
        ArgumentCaptor<Beer> argumentCaptor = ArgumentCaptor.forClass(Beer.class);
        verify(beerRepository).save(argumentCaptor.capture());
        final Beer returnedBeerDTO = argumentCaptor.getValue();

        // then
        assertThat(returnedBeerDTO.getQuantity()).isEqualTo(5);
    }

    @Test // Decrement
    @DisplayName("Should decrease total quantity in stock of a beer")
    void shouldDecreaseTotalQuantityInStockOfABeer() throws Exception {
        // given
        final Beer expectedBeerToDecrementStock = BEER_MAPPER.toModel(BeerDTOBuilder.builder().build().aDifferentOne());
        final int quantityToDecrement = VALID_QUANTITY_DTO.getQuantity();
        final long beerId = expectedBeerToDecrementStock.getId();

        // when
        when(beerRepository.findById(beerId)).thenReturn(Optional.of(expectedBeerToDecrementStock));
        beerService.updateStock(beerId, quantityToDecrement, StockAction.DECREMENT);

        // ArgumentCaptor
        ArgumentCaptor<Beer> argumentCaptor = ArgumentCaptor.forClass(Beer.class);
        verify(beerRepository).save(argumentCaptor.capture());
        final Beer returnedBeerDTO = argumentCaptor.getValue();

        // then
        assertThat(returnedBeerDTO.getQuantity()).isEqualTo(2);
    }

    @Test // Delete Beer
    @DisplayName("Should delete beer by its id")
    void shouldDeleteBeerByItsId() {
        // given
        final MessageResponseDTO expectedMessageResponseDTO = MessageResponseDTO
                .builder().message("Deleted beer with id " + EXPECTED_BEER_DTO.getId()).build();

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(EXPECTED_OPTIONAL_BEER);
        final MessageResponseDTO returnedMessageResponseDTO = beerService.delete(VALID_BEER_ID);

        // then
        assertThat(expectedMessageResponseDTO).isEqualTo(returnedMessageResponseDTO);
    }

    @Test // Find by id BeerNotFoundException
    @DisplayName("When invalid id informed then it should thrown an exception")
    void whenInvalidIdInformedThenItShouldThrownAnException() {
        // when
        when(beerRepository.findById(INVALID_BEER_ID)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> beerService.findById(INVALID_BEER_ID))
                .isInstanceOf(BeerNotFoundException.class)
                .hasMessageContaining("Beer not found with id %s", INVALID_BEER_ID);
    }

    @Test // Find by Name BeerNotFoundException
    @DisplayName("When invalid name informed then it should thrown an exception")
    void whenInvalidNameInformedThenItShouldThrownAnException() {
        // when
        when(beerRepository.findByName(INVALID_BEER_NAME)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> beerService.findByName(INVALID_BEER_NAME))
                .isInstanceOf(BeerNotFoundException.class)
                .hasMessageContaining("Beer not found with name %s", INVALID_BEER_NAME);
    }

    @Test // Create Beer BeerAlreadyRegisteredException
    @DisplayName("When registered beer informed then it should thrown an exception")
    void whenRegisteredBeerInformedThenItShouldThrownAnException() {
        // when
        when(beerRepository.findByName(VALID_BEER_NAME)).thenReturn(Optional.of(EXPECTED_BEER));

        // then
        assertThatThrownBy(() -> beerService.create(EXPECTED_BEER_DTO))
                .isInstanceOf(BeerAlreadyRegisteredException.class)
                .hasMessageContaining("Beer with name " + VALID_BEER_NAME + " already registered in the system");
    }

    @Test // Increment BeerStockExceededException
    @DisplayName("When beer stock exceeds its limit then it should thrown an exception")
    void whenBeerStockExceedItsLimitThenItShouldThrownAnException() {
        // given
        final Beer expectedBeerToIncrementStock = BEER_MAPPER.toModel(BeerDTOBuilder.builder().build().toBeerDTO());
        final int quantityToIncrement = INVALID_QUANTITY_DTO.getQuantity();

        // when
        when(beerRepository.findById(VALID_BEER_ID)).thenReturn(Optional.of(expectedBeerToIncrementStock));

        // then
        assertThatThrownBy(() -> beerService.updateStock(VALID_BEER_ID, quantityToIncrement, StockAction.INCREMENT))
                .isInstanceOf(BeerStockExceededException.class)
                .hasMessageContaining("Quantity is less than 0 or stock exceeds max quantity of: %s", expectedBeerToIncrementStock.getMax());
    }
}
