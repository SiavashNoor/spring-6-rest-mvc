package com.fibofx.spring6restmvc.controller;


import com.fibofx.spring6restmvc.model.BeerDTO;
import com.fibofx.spring6restmvc.services.BeerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@RestController

public class BeerController {

    public static  final String BEER_PATH = "/api/v1/beer";
    public static final String BEER_PATH_ID = BEER_PATH+"/{beerId}";
    private final BeerService beerService;

    @PatchMapping(BEER_PATH_ID)
    public ResponseEntity updateBeerPatchById(@PathVariable("beerId")UUID beerId , @RequestBody BeerDTO beer){
       if( beerService.patchBeerById(beerId,beer).isEmpty()){
           throw new NotFoundException();
       }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEER_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("beerId") UUID beerId){
       if(! beerService.deleteById(beerId)){

           throw new NotFoundException();
       }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEER_PATH_ID)
    public ResponseEntity<BeerDTO> updateById(@PathVariable("beerId") UUID beerId , @RequestBody BeerDTO beer){
       if( beerService.updateBeerById(beerId,beer).isEmpty()){
           throw  new NotFoundException();
       }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEER_PATH)
    public ResponseEntity<BeerDTO> handlePost(@Validated @RequestBody BeerDTO beer){

        BeerDTO savedBeer = beerService.saveNewBeer(beer);
        HttpHeaders headers = new HttpHeaders();
        //It's better for post operations return a location of created object in serverside.we do this in headers .
        headers.add("Location",BEER_PATH+"/"+savedBeer.getId().toString());
        return new ResponseEntity<>(headers,HttpStatus.CREATED);
    }


    @GetMapping(BEER_PATH)
    public List<BeerDTO> listBeers(){
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID id ){
        log.debug("get beer by id  in controller ");
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);

    }





}
