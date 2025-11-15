package com.fibofx.spring6restmvc.services;

import com.fibofx.spring6restmvc.model.BeerDTO;
import com.fibofx.spring6restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO>beerMap;
    public BeerServiceImpl(){


        this.beerMap = new HashMap<>();

        BeerDTO beer1 =  BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("Soltanieh brewed ")
                .beerStyle(BeerStyle.LAGER)
                .upc("abdcdfl")
                .price(new BigDecimal("12.44"))
                .quantityOnHand(122)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();


        BeerDTO beer2 =  BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(2)
                .beerName("Zanjan brewed ")
                .beerStyle(BeerStyle.ALE)
                .upc("44342")
                .price(new BigDecimal("10.44"))
                .quantityOnHand(12)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();


        BeerDTO beer3 =  BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(3)
                .beerName("Berlin brewed ")
                .beerStyle(BeerStyle.LAGER)
                .upc("aufgehabt")
                .price(new BigDecimal("18.99"))
                .quantityOnHand(50)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        beerMap.put(beer1.getId(),beer1);
        beerMap.put(beer2.getId(),beer2);
        beerMap.put(beer3.getId(),beer3);


    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.of(beerMap.get(id));
    }

    @Override
    public List<BeerDTO> listBeers(){

        return new ArrayList<>(beerMap.values());


}

    @Override
    public BeerDTO saveNewBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .beerName(beer.getBeerName())
                .beerStyle(beer.getBeerStyle())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .build();

        beerMap.put(savedBeer.getId(),savedBeer);
        return savedBeer;
    }

    @Override
    public void updateBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);
        existing.setBeerName(beer.getBeerName());
        existing.setPrice(beer.getPrice());
        existing.setUpc(beer.getUpc());
        existing.setVersion(beer.getVersion());
        existing.setQuantityOnHand(beer.getQuantityOnHand());
        existing.setBeerStyle(beer.getBeerStyle());

        beerMap.put(existing.getId(),existing);

    }

    @Override
    public void deleteById(UUID beerId) {
        beerMap.remove(beerId);
    }

    @Override
    public void patchBeerById(UUID beerId, BeerDTO beer) {
        BeerDTO existing = beerMap.get(beerId);

        if(StringUtils.hasText(beer.getBeerName())){
            existing.setBeerName(beer.getBeerName());
        }
        if(beer.getBeerStyle()!=null){
            existing.setBeerStyle(beer.getBeerStyle());
        }
        if(beer.getPrice()!=null){
            existing.setPrice(beer.getPrice());
        }
        if(beer.getQuantityOnHand() !=null){
            existing.setQuantityOnHand(beer.getQuantityOnHand());
        }
        if(StringUtils.hasText(beer.getUpc())){
            existing.setUpc(beer.getUpc());
        }



    }
}
