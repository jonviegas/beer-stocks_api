package com.jonservices.beerstocks.controller;

import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.MessageResponseDTO;
import com.jonservices.beerstocks.data.dto.QuantityDTO;
import com.jonservices.beerstocks.data.enums.StockAction;
import com.jonservices.beerstocks.docs.BeerControllerDocs;
import com.jonservices.beerstocks.service.BeerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/beers")
@CrossOrigin(maxAge = 3600)
public class BeerController implements BeerControllerDocs {

    @Autowired
    private BeerService beerService;

    @GetMapping
    public List<BeerDTO> findAll() {
        return beerService.findAll();
    }

    @GetMapping("/{id}")
    public BeerDTO findById(@PathVariable Long id) {
        return beerService.findById(id);
    }

    @GetMapping("/search/{name}")
    public BeerDTO findByName(@PathVariable String name) {
        return beerService.findByName(name);
    }

    @PostMapping
    public BeerDTO create(@RequestBody @Valid BeerDTO beerDTO) {
        return beerService.create(beerDTO);
    }

    @PatchMapping("/{id}/increment")
    public BeerDTO increment(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) {
        return beerService.updateStock(id, quantityDTO.getQuantity(), StockAction.INCREMENT);
    }

    @PatchMapping("{id}/decrement")
    public BeerDTO decrement(@PathVariable Long id, @RequestBody @Valid QuantityDTO quantityDTO) {
        return beerService.updateStock(id, quantityDTO.getQuantity(), StockAction.DECREMENT);
    }

    @DeleteMapping("{id}")
    public MessageResponseDTO delete(@PathVariable Long id) {
        return beerService.delete(id);
    }

}
