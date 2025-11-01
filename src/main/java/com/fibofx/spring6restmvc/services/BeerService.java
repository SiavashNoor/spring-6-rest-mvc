package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {




    Beer getBeerById(UUID id );

    List<Beer> listBeers();

    Beer saveNewBeer(Beer beer);

    void updateBeerById(UUID beerId, Beer beer);

    void deleteById(UUID beerId);

    void patchBeerById(UUID beerId, Beer beer);

}
