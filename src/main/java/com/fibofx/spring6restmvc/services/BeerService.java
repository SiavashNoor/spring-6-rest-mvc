package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {




    Optional<BeerDTO> getBeerById(UUID id );

    List<BeerDTO> listBeers();

    BeerDTO saveNewBeer(BeerDTO beer);

    void updateBeerById(UUID beerId, BeerDTO beer);

    void deleteById(UUID beerId);

    void patchBeerById(UUID beerId, BeerDTO beer);

}
