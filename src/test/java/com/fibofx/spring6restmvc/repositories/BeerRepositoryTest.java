package com.fibofx.spring6restmvc.repositories;

import com.fibofx.spring6restmvc.entities.Beer;
import com.fibofx.spring6restmvc.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
class BeerRepositoryTest {


    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
    Beer savedBeer = beerRepository.save(Beer.builder()
            .beerName("Siavash Brewed")
                    .beerStyle(BeerStyle.LAGER)
                    .upc("main upc")
                    .price(new BigDecimal("12.22"))

            .build());

    //to write to database immediately .jpa has a lazy write to database its better to flush. because the
    beerRepository.flush();
    assertThat(savedBeer).isNotNull();
    assertThat(savedBeer.getId()).isNotNull();

    }

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class,()->{
                Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("Siavash Brewed 12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890")
                .beerStyle(BeerStyle.LAGER)
                .upc("main upc")
                .price(new BigDecimal("12.22"))
                .build());
        //to write to database immediately .jpa has a lazy write to database its better to flush. because the
        beerRepository.flush();
        });



    }
}