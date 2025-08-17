package com.akkodis.codingchallenge.taco.web.api;

import com.akkodis.codingchallenge.taco.web.dto.assembler.ToppingDtoAssembler;
import com.akkodis.codingchallenge.taco.web.dto.response.ToppingDto;
import com.akkodis.codingchallenge.taco.model.Topping;
import com.akkodis.codingchallenge.taco.service.ToppingService;
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
@RequestMapping("/api/v1/toppings")
@RequiredArgsConstructor
public class ToppingController {

    private final ToppingService toppingService;
    private final PagedResourcesAssembler<Topping> pagedResourcesAssembler;
    private final ToppingDtoAssembler dtoAssembler;

    @GetMapping
    public PagedModel<ToppingDto> findAllAdditionalToppings(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Page<Topping> currentPage = toppingService.findAllToppings(PageRequest.of(page, size));
        return pagedResourcesAssembler.toModel(currentPage, dtoAssembler);
    }
}