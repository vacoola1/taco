package com.akkodis.codingchallenge.taco.web;

import com.akkodis.codingchallenge.taco.model.Topping;
import com.akkodis.codingchallenge.taco.service.ToppingService;
import com.akkodis.codingchallenge.taco.web.api.ToppingController;
import com.akkodis.codingchallenge.taco.web.dto.response.ToppingDto;
import com.akkodis.codingchallenge.taco.web.dto.assembler.ToppingDtoAssembler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ToppingController.class)
@Import(GlobalExceptionHandler.class)
class ToppingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToppingService mockToppingService;

    @MockBean
    private PagedResourcesAssembler<Topping> mockPagedResourcesAssembler;

    @MockBean
    private ToppingDtoAssembler mockToppingDtoAssembler;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockToppingService, mockToppingDtoAssembler, mockPagedResourcesAssembler);
    }

    @Test
    void testToppingsDefaultPage() throws Exception {

        Topping topping1 = new Topping();
        topping1.setId(1L);
        topping1.setName("Cheese");
        topping1.setCost(0.50);

        Topping topping2 = new Topping();
        topping2.setId(2L);
        topping2.setName("Salsa");
        topping2.setCost(0.75);

        ToppingDto toppingDto1 = ToppingDto.builder()
                .id(topping1.getId())
                .name(topping1.getName())
                .cost(topping1.getCost())
                .build();

        ToppingDto toppingDto2 = ToppingDto.builder()
                .id(topping2.getId())
                .name(topping2.getName())
                .cost(topping2.getCost())
                .build();

        Page<Topping> toppingPage = new PageImpl<>(List.of(topping1, topping2), PageRequest.of(0, 10), 2);
        PagedModel<ToppingDto> toppingDtoPagedModel = PagedModel.of(List.of(toppingDto1, toppingDto2),
                new PagedModel.PageMetadata(10, 0, 2, 1),
                Link.of("https://example.com/api/v1/toppings"));

        when(mockToppingService.findAllToppings(PageRequest.of(0, 10))).thenReturn(toppingPage);
        when(mockPagedResourcesAssembler.toModel(toppingPage, mockToppingDtoAssembler)).thenReturn(toppingDtoPagedModel);

        String expectedResponse = """
                {
                  "_embedded": {
                    "toppingDtoList": [
                      {
                        "id": 1,
                        "name": "Cheese",
                        "cost": 0.5
                      },
                      {
                        "id": 2,
                        "name": "Salsa",
                        "cost": 0.75
                      }
                    ]
                  },
                  "_links": {
                    "self": {
                      "href": "https://example.com/api/v1/toppings"
                    }
                  },
                  "page": {
                    "size": 10,
                    "totalElements": 2,
                    "totalPages": 1,
                    "number": 0
                  }
                }
                """;

        mockMvc.perform(get("/api/v1/toppings"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(content().json(expectedResponse));

        verify(mockToppingService).findAllToppings(PageRequest.of(0, 10));
        verify(mockPagedResourcesAssembler).toModel(toppingPage, mockToppingDtoAssembler);
    }

    @Test
    void testToppingsPages() throws Exception {

        Topping topping1 = new Topping();
        topping1.setId(1L);
        topping1.setName("Cheese");
        topping1.setCost(0.50);

        Topping topping2 = new Topping();
        topping2.setId(2L);
        topping2.setName("Salsa");
        topping2.setCost(0.75);

        ToppingDto toppingDto1 = ToppingDto.builder()
                .id(topping1.getId())
                .name(topping1.getName())
                .cost(topping1.getCost())
                .build();

        ToppingDto toppingDto2 = ToppingDto.builder()
                .id(topping2.getId())
                .name(topping2.getName())
                .cost(topping2.getCost())
                .build();

        Page<Topping> toppingPage = new PageImpl<>(List.of(topping1, topping2), PageRequest.of(1, 2), 10);
        PagedModel<ToppingDto> toppingDtoPagedModel = PagedModel.of(List.of(toppingDto1, toppingDto2),
                new PagedModel.PageMetadata(2, 1, 10, 5),
                Link.of("https://example.com/api/v1/toppings"));

        when(mockToppingService.findAllToppings(PageRequest.of(1, 2))).thenReturn(toppingPage);
        when(mockPagedResourcesAssembler.toModel(toppingPage, mockToppingDtoAssembler)).thenReturn(toppingDtoPagedModel);

        String expectedResponse = """
                {
                  "_embedded": {
                    "toppingDtoList": [
                      {
                        "id": 1,
                        "name": "Cheese",
                        "cost": 0.5
                      },
                      {
                        "id": 2,
                        "name": "Salsa",
                        "cost": 0.75
                      }
                    ]
                  },
                  "_links": {
                    "self": {
                      "href": "https://example.com/api/v1/toppings"
                    }
                  },
                  "page": {
                    "size": 2,
                    "totalElements": 10,
                    "totalPages": 5,
                    "number": 1
                  }
                }
                """;

        mockMvc.perform(get("/api/v1/toppings?page=1&size=2"))
                //.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(content().json(expectedResponse))
                .andExpect(content().contentType("application/hal+json"))
        ;

        verify(mockToppingService).findAllToppings(PageRequest.of(1, 2));
        verify(mockPagedResourcesAssembler).toModel(toppingPage, mockToppingDtoAssembler);
    }
}
