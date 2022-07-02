package com.jonservices.beerstocks.service;

import com.jonservices.beerstocks.data.dto.BeerDTO;
import com.jonservices.beerstocks.data.dto.MessageResponseDTO;
import com.jonservices.beerstocks.data.enums.StockAction;
import com.jonservices.beerstocks.data.model.Beer;
import com.jonservices.beerstocks.exceptions.BeerAlreadyRegisteredException;
import com.jonservices.beerstocks.exceptions.BeerNotFoundException;
import com.jonservices.beerstocks.exceptions.BeerStockExceededException;
import com.jonservices.beerstocks.mapper.BeerMapper;
import com.jonservices.beerstocks.repository.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class BeerService {

    private final BeerMapper beerMapper = BeerMapper.INSTANCE;
    @Autowired
    private BeerRepository beerRepository;

    public List<BeerDTO> findAll() {
        return beerRepository.findAll().stream().map(beerMapper::toDTO).collect(Collectors.toList());
    }

    public BeerDTO findById(Long id) {
        final Beer beer = verifyIfExists(id);
        return beerMapper.toDTO(beer);
    }

    public BeerDTO findByName(String name) {
        final Beer beer = verifyIfExists(name);
        return beerMapper.toDTO(beer);
    }

    public BeerDTO create(BeerDTO beerDTO) {
        verifyIfAlreadyRegistered(beerDTO.getName());
        verifyIfStockExceeded(beerDTO.getQuantity(), beerDTO.getMax());
        final Beer beerToSave = beerMapper.toModel(beerDTO);
        final Beer savedBeer = beerRepository.save(beerToSave);
        return beerMapper.toDTO(savedBeer);
    }

    public BeerDTO updateStock(Long id, int quantity, StockAction action) {
        final Beer beer = verifyIfExists(id);
        final int updatedQuantity = action.equals(StockAction.INCREMENT) ?
                beer.getQuantity() + quantity : // Increment
                beer.getQuantity() - quantity; // Decrement
        verifyIfStockExceeded(updatedQuantity, beer.getMax());
        beer.setQuantity(updatedQuantity);
        final Beer stockUpdatedBeer = beerRepository.save(beer);
        return beerMapper.toDTO(stockUpdatedBeer);
    }

    public MessageResponseDTO delete(Long id) {
        verifyIfExists(id);
        beerRepository.deleteById(id);
        return MessageResponseDTO.builder().message("Deleted beer with id " + id).build();
    }

    private Beer verifyIfExists(Object value) {
        final String type = value instanceof Long ? "id" : "name";
        final Supplier<BeerNotFoundException> notFoundException = () -> new BeerNotFoundException(type, value);
        if (type.equals("id"))
            return beerRepository.findById((Long) value).orElseThrow(notFoundException);
        else
            return beerRepository.findByName((String) value).orElseThrow(notFoundException);
    }

    private void verifyIfAlreadyRegistered(String name) {
        if (beerRepository.findByName(name).isPresent())
            throw new BeerAlreadyRegisteredException(name);
    }

    private void verifyIfStockExceeded(int quantity, int max) {
        if (quantity > max || quantity < 0)
            throw new BeerStockExceededException(max);
    }

}
