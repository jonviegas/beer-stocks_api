package com.jonservices.beerstocks.docs;

import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.MessageResponseDTO;
import com.jonservices.beerstocks.data.dto.QuantityDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "Beer Controller")
public interface BeerControllerDocs {

    @Operation(summary = "Find all beers")
    @ApiResponse(responseCode = "200", description = "Returns OK status")
    @ResponseStatus(HttpStatus.OK)
    List<BeerDTO> findAll();

    @Operation(summary = "Finds a beer by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status if the beer exists"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST status when an invalid id format is passed"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when the beer does not exists")
    })
    @ResponseStatus(HttpStatus.OK)
    BeerDTO findById(@PathVariable Long id);

    @Operation(summary = "Finds a beer by its name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status if beer exists"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when beer does not exist")
    })
    @ResponseStatus(HttpStatus.OK)
    BeerDTO findByName(@PathVariable String name);

    @Operation(summary = "Saves a beer in the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Returns CREATED status when a new beer is saved"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST when missing required fields or wrong field range value")
    })
    @ResponseStatus(HttpStatus.CREATED)
    BeerDTO create(@RequestBody @Valid BeerDTO beerDTO);

    @Operation(summary = "Increments beer quantity on stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status when stock is incremented"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST when missing required fields or wrong field range value"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when beer does not exist")
    })
    @ResponseStatus(HttpStatus.OK)
    BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO);

    @Operation(summary = "Decrements beer quantity on stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status when stock is decremented"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST when missing required fields or wrong field range value"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when beer does not exist")
    })
    @ResponseStatus(HttpStatus.OK)
    BeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO);

    @Operation(summary = "Removes a beer from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns OK status when a beer is deleted"),
            @ApiResponse(responseCode = "400", description = "Returns BAD REQUEST status when an invalid id format is passed"),
            @ApiResponse(responseCode = "404", description = "Returns NOT FOUND status when the beer does not exists")
    })
    @ResponseStatus(HttpStatus.OK)
    MessageResponseDTO delete(@PathVariable Long id);

}
