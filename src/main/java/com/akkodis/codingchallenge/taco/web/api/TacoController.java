package com.akkodis.codingchallenge.taco.web.api;

import com.akkodis.codingchallenge.taco.model.Taco;
import com.akkodis.codingchallenge.taco.web.dto.assembler.TacoDtoAssembler;
import com.akkodis.codingchallenge.taco.web.dto.response.TacoDto;
import com.akkodis.codingchallenge.taco.service.TacoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/tacos")
@RequiredArgsConstructor
public class TacoController {

    private final TacoService tacoService;
    private final PagedResourcesAssembler<Taco> pagedResourcesAssembler;
    private final TacoDtoAssembler dtoAssembler;

    @GetMapping
    public PagedModel<TacoDto> findAllTacos(@RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size) {
        Page<Taco> currentPage = tacoService.findAllTacos(PageRequest.of(page, size));
        return pagedResourcesAssembler.toModel(currentPage, dtoAssembler);
    }
}