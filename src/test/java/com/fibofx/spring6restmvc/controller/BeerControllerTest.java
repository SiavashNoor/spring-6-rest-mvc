package com.fibofx.spring6restmvc.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fibofx.spring6restmvc.model.Beer;
import com.fibofx.spring6restmvc.services.BeerService;
import com.fibofx.spring6restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;







//this is for mock mvc and mockito test.This annotation tells Spring Boot to start a minimal web application context for testing only the web layer — specifically the BeerController class.
@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
   MockMvc mockMvc;
    @MockitoBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;


    BeerServiceImpl beerServiceImpl ;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;
    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp(){
        beerServiceImpl = new BeerServiceImpl();
    }


    @Test
    void testPatchBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().getFirst();

        Map<String ,Object> beerMap = new HashMap<>();
        beerMap.put("beerName","New Name");
        mockMvc.perform(patch(BeerController.BEER_PATH+"/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isNoContent());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(),beerArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());

        assertThat((beerMap.get("beerName"))).isEqualTo(beerArgumentCaptor.getValue().getBeerName());


    }

    @Test
    void testDeleteBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().getFirst();

        mockMvc.perform(delete(BeerController.BEER_PATH+"/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                ).andExpect(status().isNoContent());




        verify(beerService).deleteById(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());


    }

    @Test
    void testUpdateBeer() throws Exception {
        Beer beer = beerServiceImpl.listBeers().getFirst();


        mockMvc.perform(put(BeerController.BEER_PATH+"/"+beer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isNoContent());
        // Verify the service was called with the correct arguments
        verify(beerService).updateBeerById(any(UUID.class),any(Beer.class));
    }

    @Test
    void testCreateNewBeer() throws Exception {

        Beer beer = beerServiceImpl.listBeers().getFirst();
       beer.setVersion(null);
       beer.setId(null);

       given(beerService.saveNewBeer(any(Beer.class))).willReturn(beerServiceImpl.listBeers().get(1));

       mockMvc.perform(post(BeerController.BEER_PATH)
               .accept(MediaType.APPLICATION_JSON)
               .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(beer)))
               .andExpect(status().isCreated())
               .andExpect(header().exists("Location"));

    }

    @Test
    void getBeerById() throws Exception {
       Beer testBeer = beerServiceImpl.listBeers().getFirst();

       //is a Mockito stub definition, written in BDD style (Behavior-Driven Development).
        //It tells Mockito how the mock object (beerService) should behave when a specific method is called during the test.
       given(beerService.getBeerById(testBeer.getId())).willReturn(testBeer);
        mockMvc.perform(get(BeerController.BEER_PATH+"/"+testBeer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName",is(testBeer.getBeerName())));
    }

    @Test
    void testListBeers() throws Exception {
        given(beerService.listBeers()).willReturn(beerServiceImpl.listBeers());

        mockMvc.perform(get(BeerController.BEER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));

    }



}