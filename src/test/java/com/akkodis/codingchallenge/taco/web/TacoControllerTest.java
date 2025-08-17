package com.akkodis.codingchallenge.taco.web;

import com.akkodis.codingchallenge.taco.model.Taco;
import com.akkodis.codingchallenge.taco.service.TacoService;
import com.akkodis.codingchallenge.taco.web.api.TacoController;
import com.akkodis.codingchallenge.taco.web.dto.response.TacoDto;
import com.akkodis.codingchallenge.taco.web.dto.assembler.TacoDtoAssembler;
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

@WebMvcTest(TacoController.class)
@Import(GlobalExceptionHandler.class)
class TacoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TacoService mockTacoService;

    @MockBean
    private PagedResourcesAssembler<Taco> mockPagedResourcesAssembler;

    @MockBean
    private TacoDtoAssembler mockTacoDtoAssembler;

    @AfterEach
    void tearDown() {
        verifyNoMoreInteractions(mockTacoService, mockTacoDtoAssembler, mockPagedResourcesAssembler);
    }

    @Test
    void testTacosDefaultPage() throws Exception {

        Taco taco1 = new Taco();
        taco1.setId(1L);
        taco1.setName("Sample Taco");
        taco1.setCost(5.50);
        taco1.setNumberAvailable(10);

        Taco taco2 = new Taco();
        taco2.setId(2L);
        taco2.setName("Spicy Taco");
        taco2.setCost(8.50);
        taco2.setNumberAvailable(20);

        TacoDto tacoDto1 = TacoDto.builder()
                .id(taco1.getId())
                .name(taco1.getName())
                .cost(taco1.getCost())
                .numberAvailable(taco1.getNumberAvailable())
                .build();

        TacoDto tacoDto2 = TacoDto.builder()
                .id(taco1.getId())
                .name(taco1.getName())
                .cost(taco1.getCost())
                .numberAvailable(taco1.getNumberAvailable())
                .build();

        Page<Taco> tacoPage = new PageImpl<>(List.of(taco1, taco2), PageRequest.of(0, 10), 2);
        PagedModel<TacoDto> tacoDtoPagedModel = PagedModel.of(List.of(tacoDto1, tacoDto2),
                new PagedModel.PageMetadata(10, 0, 2, 1),
                Link.of("https://example.com/api/v1/tacos"));

        when(mockTacoService.findAllTacos(PageRequest.of(0, 10))).thenReturn(tacoPage);
        when(mockPagedResourcesAssembler.toModel(tacoPage, mockTacoDtoAssembler)).thenReturn(tacoDtoPagedModel);

        String expectedResponse = """
                {
                  "_embedded": {
                    "tacoDtoList": [
                      {
                        "id": 1,
                        "name": "Sample Taco",
                        "cost": 5.5,
                        "numberAvailable": 10
                      },
                      {
                        "id": 1,
                        "name": "Sample Taco",
                        "cost": 5.5,
                        "numberAvailable": 10
                      }
                    ]
                  },
                  "_links": {
                    "self": {
                      "href": "https://example.com/api/v1/tacos"
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

        mockMvc.perform(get("/api/v1/tacos"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(content().json(expectedResponse));

        verify(mockTacoService).findAllTacos(PageRequest.of(0, 10));
        verify(mockPagedResourcesAssembler).toModel(tacoPage, mockTacoDtoAssembler);
    }

    @Test
    void testTacosPages() throws Exception {

        Taco taco1 = new Taco();
        taco1.setId(1L);
        taco1.setName("Sample Taco");
        taco1.setCost(5.50);
        taco1.setNumberAvailable(10);

        Taco taco2 = new Taco();
        taco2.setId(2L);
        taco2.setName("Spicy Taco");
        taco2.setCost(8.50);
        taco2.setNumberAvailable(20);

        TacoDto tacoDto1 = TacoDto.builder()
                .id(taco1.getId())
                .name(taco1.getName())
                .cost(taco1.getCost())
                .numberAvailable(taco1.getNumberAvailable())
                .build();

        TacoDto tacoDto2 = TacoDto.builder()
                .id(taco1.getId())
                .name(taco1.getName())
                .cost(taco1.getCost())
                .numberAvailable(taco1.getNumberAvailable())
                .build();

        Page<Taco> tacoPage = new PageImpl<>(List.of(taco1, taco2), PageRequest.of(0, 2), 10);
        PagedModel<TacoDto> tacoDtoPagedModel = PagedModel.of(List.of(tacoDto1, tacoDto2),
                new PagedModel.PageMetadata(2, 0, 1, 5),
                Link.of("https://example.com/api/v1/tacos"));

        when(mockTacoService.findAllTacos(PageRequest.of(5, 2))).thenReturn(tacoPage);
        when(mockPagedResourcesAssembler.toModel(tacoPage, mockTacoDtoAssembler)).thenReturn(tacoDtoPagedModel);

        String expectedResponse = """
                {
                  "_embedded": {
                    "tacoDtoList": [
                      {
                        "id": 1,
                        "name": "Sample Taco",
                        "cost": 5.5,
                        "numberAvailable": 10
                      },
                      {
                        "id": 1,
                        "name": "Sample Taco",
                        "cost": 5.5,
                        "numberAvailable": 10
                      }
                    ]
                  },
                  "_links": {
                    "self": {
                      "href": "https://example.com/api/v1/tacos"
                    }
                  },
                  "page": {
                    "size": 2,
                    "totalElements": 1,
                    "totalPages": 5,
                    "number": 0
                  }
                }
                """;

        mockMvc.perform(get("/api/v1/tacos?page=5&size=2"))
                //.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(content().json(expectedResponse));

        verify(mockTacoService).findAllTacos(PageRequest.of(5, 2));
        verify(mockPagedResourcesAssembler).toModel(tacoPage, mockTacoDtoAssembler);
    }
}
