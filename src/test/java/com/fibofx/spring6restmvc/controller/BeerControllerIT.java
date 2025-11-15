package com.fibofx.spring6restmvc.controller;

import com.fibofx.spring6restmvc.model.BeerDTO;
import com.fibofx.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


//this brings the full context for integration test
@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;


    @Test
    void testListBeers() {

        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }





    //added these to annotations to leave database unchanged to not affect other tests result .
    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();
        assertThat(dtos.size()).isEqualTo(0);
    }

}