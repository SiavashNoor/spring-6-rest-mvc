package com.fibofx.spring6restmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fibofx.spring6restmvc.entities.Beer;
import com.fibofx.spring6restmvc.mappers.BeerMapper;
import com.fibofx.spring6restmvc.model.BeerDTO;
import com.fibofx.spring6restmvc.model.BeerStyle;
import com.fibofx.spring6restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


//this brings the full context for integration test
@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    //“Inject the entire Spring Web Application context.”
    @Autowired
    WebApplicationContext wac;


    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        /**
         * means:
         *
         * “Build a MockMvc object using the full Spring WebApplicationContext.”
         *
         * So instead of the automatically autowired mockMvc, you manually build a new one that uses the entire application context.
         *
         * This setup is useful when:
         *
         * ✔ You want full controller testing (controller + filters + interceptors)
         * ✔ You need all beans loaded
         * ✔ You want to test the application almost like it's running normally
         */

        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }



    @Test
    void testPatchBeerBadName() throws Exception {
        Beer beer = beerRepository.findAll().getFirst();

        Map<String ,Object> beerMap = new HashMap<>();
        beerMap.put("beerName","New Name29847239487239Name2984WEWRWRWFSDFSDFSDFSDFsd7239487239Name29847239487239Name29847239487239Name29847239487239Name29847239487239Name29847239487239");

        MvcResult mvcResult = mockMvc.perform(patch(BeerController.BEER_PATH+"/"+beer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest()).andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            beerController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {

        Beer beer = beerRepository.findAll().getFirst();
        ResponseEntity responseEntity = beerController.deleteById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();

    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class,() ->{
           beerController.updateById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingBeer() {

        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerMapper.beerToBeerDto(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        final String beerName = "Updatad beer name";
        beerDTO.setBeerName(beerName);

        ResponseEntity responseEntity = beerController.updateById(beer.getId(),beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Beer updatedBeer = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBeer.getBeerName()).isEqualTo(beerName);

    }

    //this test changes the state of database and we need to add these two annotations to prevent the effect on the other tests.
    @Rollback
    @Transactional
    @Test
    void saveNewBeerTest() {
        BeerDTO beerDTO = BeerDTO.builder()
                .beerName("new beer ")
                .price(new BigDecimal("12.3"))
                .upc("aliio")
                .quantityOnHand(12)
                .beerStyle(BeerStyle.IPA)
                .build();
        ResponseEntity responseEntity = beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID  = UUID.fromString(locationUUID[4]);

        Beer beer = beerRepository.findById(savedUUID).get();
        assertThat(beer).isNotNull();
    }

    @Test
    void testBeerIdNotFound() {
        assertThrows(NotFoundException.class,()->{
            beerController.getBeerById(UUID.randomUUID());
        });

    }

    @Test
    void testGetById() {

        Beer beer = beerRepository.findAll().getFirst();

        BeerDTO dto = beerController.getBeerById(beer.getId());
        assertThat(dto).isNotNull();

    }

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