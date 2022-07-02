package com.jonservices.beerstocks.controller;

import com.jonservices.beerstocks.builder.BeerDTOBuilder;
import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.MessageResponseDTO;
import com.jonservices.beerstocks.data.enums.StockAction;
import com.jonservices.beerstocks.exceptions.BeerAlreadyRegisteredException;
import com.jonservices.beerstocks.exceptions.BeerNotFoundException;
import com.jonservices.beerstocks.exceptions.BeerStockExceededException;
import com.jonservices.beerstocks.service.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.jonservices.beerstocks.mocks.BeerTestMocks.*;
import static com.jonservices.beerstocks.utils.JSONConvertionUtils.asJsonString;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BeerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    @BeforeEach
    void setupEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((s, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test // GET Find All
    @DisplayName("When GET is called then all beers and ok status is returned")
    void whenGETIsCalledThenAllBeersAndOkStatusIsReturned() throws Exception {
        // when
        when(beerService.findAll()).thenReturn(ALL_BEERS_DTO);

        // then
        mockMvc.perform(get(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(ALL_BEERS_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(EXPECTED_BEER_DTO.getName())))
                .andExpect(jsonPath("$[0].brand", is(EXPECTED_BEER_DTO.getBrand())))
                .andExpect(jsonPath("$[1].max", is(ANOTHER_BEER_DTO.getMax())))
                .andExpect(jsonPath("$[1].quantity", is(ANOTHER_BEER_DTO.getQuantity())));
    }

    @Test // GET Find by id
    @DisplayName("When GET is called with valid id then ok status is returned")
    void whenGETIsCalledWithValidIdThenOkStatusIsReturned() throws Exception {
        // when
        when(beerService.findById(VALID_BEER_ID)).thenReturn(EXPECTED_BEER_DTO);

        // then
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_BEER_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(EXPECTED_BEER_DTO.getName())));
    }

    @Test // GET Find by Name
    @DisplayName("When GET is called with valid name then ok status is returned")
    void whenGETIsCalledWithValidNameThenOkStatusIsReturned() throws Exception {
        // when
        when(beerService.findByName(VALID_BEER_NAME)).thenReturn(EXPECTED_BEER_DTO);

        // then
        mockMvc.perform(get(BEER_API_URL_PATH + "/search/" + VALID_BEER_NAME)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_BEER_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(EXPECTED_BEER_DTO.getName())))
                .andExpect(jsonPath("$.brand", is(EXPECTED_BEER_DTO.getBrand())));
    }

    @Test // POST Save Beer
    @DisplayName("When POST is called then it should create beer")
    void whenPOSTIsCalledThenItShouldCreateBeer() throws Exception {
        // when
        when(beerService.create(EXPECTED_BEER_DTO)).thenReturn(EXPECTED_BEER_DTO);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_BEER_DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_BEER_DTO.getName())));
    }

    @Test // PATCH Increment
    @DisplayName("When PATCH is called then it should increment beer on stock")
    void whenPATCHIsCalledThenItShouldIncrementBeerOnStock() throws Exception {
        // given
        final BeerDTO incrementedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        final int quantityToIncrement = VALID_QUANTITY_DTO.getQuantity();
        incrementedBeerDTO.setQuantity(incrementedBeerDTO.getQuantity() + quantityToIncrement);

        // when
        when(beerService.updateStock(VALID_BEER_ID, quantityToIncrement, StockAction.INCREMENT)).thenReturn(incrementedBeerDTO);

        // then "/api/v1/beers/{id}/increment"
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/increment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VALID_QUANTITY_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(incrementedBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(incrementedBeerDTO.getBrand())))
                .andExpect(jsonPath("$.quantity", is(incrementedBeerDTO.getQuantity())));
    }

    @Test // PATCH Decrement
    @DisplayName("When PATCH is called then it should decrement beer on stock")
    void whenPATCHIsCalledThenItShouldDecrementBeerOnStock() throws Exception {
        // given
        final BeerDTO decrementedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        final int quantityToDecrement = VALID_QUANTITY_DTO.getQuantity();
        decrementedBeerDTO.setQuantity(decrementedBeerDTO.getQuantity() - quantityToDecrement);

        // when
        when(beerService.updateStock(VALID_BEER_ID, quantityToDecrement, StockAction.DECREMENT)).thenReturn(decrementedBeerDTO);

        // then "/api/v1/beers/{id}/increment"
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/decrement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(VALID_QUANTITY_DTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(decrementedBeerDTO.getName())))
                .andExpect(jsonPath("$.brand", is(decrementedBeerDTO.getBrand())))
                .andExpect(jsonPath("$.quantity", is(decrementedBeerDTO.getQuantity())));
    }

    @Test // DELETE Delete by id
    @DisplayName("When DELETE is called then it should delete beer")
    void whenPOSTIsCalledThenItShouldDeleteBeer() throws Exception {
        // given
        final MessageResponseDTO expectedMessageResponseDTO = MessageResponseDTO
                .builder().message("Deleted beer with id " + VALID_BEER_ID).build();

        // when
        when(beerService.delete(VALID_BEER_ID)).thenReturn(expectedMessageResponseDTO);

        // then
        mockMvc.perform(delete(BEER_API_URL_PATH + "/" + VALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is(expectedMessageResponseDTO.getMessage())));
    }

    @Test // GET Find by id BeerNotFoundException
    @DisplayName("When GET is called with an invalid id then it should thrown an error")
    void whenGETIsCalledWithAnInvalidIdThenItShouldThrownAnError() throws Exception {
        // when
        doThrow(BeerNotFoundException.class).when(beerService).findById(INVALID_BEER_ID);

        // then
        mockMvc.perform(get(BEER_API_URL_PATH + "/" + INVALID_BEER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // GET Find by name BeerNotFoundException
    @DisplayName("When GET is called with an invalid name then it should thrown an error")
    void whenGETIsCalledWithAnInvalidNameThenItShouldThrownAnError() throws Exception {
        // when
        doThrow(BeerNotFoundException.class).when(beerService).findByName(INVALID_BEER_NAME);

        // then
        mockMvc.perform(get(BEER_API_URL_PATH + "/search/" + INVALID_BEER_NAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test // POST Create beer BeerAlreadyRegisteredException
    @DisplayName("When POST is called with a registered beer name then it should thrown an error")
    void whenPOSTIsCalledWithARegisteredBeerNameThenShouldThrownAnError() throws Exception {
        // when
        when(beerService.create(EXPECTED_BEER_DTO)).thenThrow(BeerAlreadyRegisteredException.class);

        // then
        mockMvc.perform(post(BEER_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(EXPECTED_BEER_DTO)))
                .andExpect(status().isBadRequest());
    }

    @Test // PATCH Increment BeerStockExceededException
    @DisplayName("When PATCH is called with quantity exceeding stock limit then it should thrown an error")
    void whenPATCHIsCalledWithQuantityExceedingStockLimitThenItShouldThrownAnError() throws Exception {
        // given
        final BeerDTO incrementedBeerDTO = BeerDTOBuilder.builder().build().toBeerDTO();
        final int quantityToIncrement = INVALID_QUANTITY_DTO.getQuantity();

        incrementedBeerDTO.setQuantity(incrementedBeerDTO.getQuantity() + quantityToIncrement);

        // when
        when(beerService.updateStock(VALID_BEER_ID, quantityToIncrement, StockAction.INCREMENT)).thenThrow(BeerStockExceededException.class);

        // then
        mockMvc.perform(patch(BEER_API_URL_PATH + "/" + VALID_BEER_ID + "/increment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(INVALID_QUANTITY_DTO)))
                .andExpect(status().isBadRequest());
    }

}
