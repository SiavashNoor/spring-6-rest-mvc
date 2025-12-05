package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.entities.Beer;
import com.fibofx.spring6restmvc.mappers.BeerMapper;
import com.fibofx.spring6restmvc.model.BeerDTO;
import com.fibofx.spring6restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;



    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.findById(id)
                .orElse(null)));
    }


    /**
     * beerRepository.findAll()
     * Calls the repository to get all Beer entities from the database.
     * The result is typically a List<Beer>.
     * .stream()
     * Turns the list into a stream so you can process each item one by one using functional operations (map, filter, etc.).
     * .map(beerMapper::beerToBeerDto)
     * For each Beer entity in the stream, convert it to a BeerDTO using the beerMapper.
     * beerMapper::beerToBeerDto is a method reference, basically shorthand for:
     */
    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .stream()
                .map(beerMapper::beerToBeerDto)
                .collect(Collectors.toList());


    }



    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {
        BeerDTO dto = beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beer)));
        beerRepository.flush();
        return dto;

    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID beerId, BeerDTO beer) {

        AtomicReference<Optional<BeerDTO>> atomicReference =new AtomicReference<>();
        beerRepository.findById(beerId).ifPresentOrElse(foundBeer ->{
            foundBeer.setBeerName(beer.getBeerName());
            foundBeer.setBeerStyle(beer.getBeerStyle());
            foundBeer.setUpc(beer.getUpc());
            foundBeer.setPrice(beer.getPrice());
            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));
        },()->{

            atomicReference.set(Optional.empty());
        });
        return atomicReference.get();
    }

    @Override
    public boolean deleteById(UUID beerId) {
        if(beerRepository.existsById(beerId)){
            beerRepository.deleteById(beerId);
            return true;
        }
        return false;
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID beerId, BeerDTO beer) {
        AtomicReference<Optional<BeerDTO>> atomicReference = new AtomicReference<>();

        beerRepository.findById(beerId).ifPresentOrElse(foundBeer -> {
            if (StringUtils.hasText(beer.getBeerName())) {
                foundBeer.setBeerName(beer.getBeerName());
            }
            if (beer.getBeerStyle() != null) {
                foundBeer.setBeerStyle(beer.getBeerStyle());
            }
            if (StringUtils.hasText(beer.getUpc())) {
                foundBeer.setBeerName(beer.getUpc());
            }
            if (beer.getPrice() != null) {
                foundBeer.setPrice(beer.getPrice());
            }
            if (beer.getQuantityOnHand() != null) {
                foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
            }

            atomicReference.set(Optional.of(beerMapper.beerToBeerDto(beerRepository.save(foundBeer))));


        },()->{atomicReference.set(Optional.empty());
    });
     return atomicReference.get();
    }

}
