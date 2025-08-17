package com.akkodis.codingchallenge.taco.web.api;

import com.akkodis.codingchallenge.taco.model.Bill;
import com.akkodis.codingchallenge.taco.web.dto.assembler.BillDtoAssembler;
import com.akkodis.codingchallenge.taco.web.dto.response.BillDto;
import com.akkodis.codingchallenge.taco.service.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final BillDtoAssembler dtoAssembler;
    @PostMapping("/{orderId}")
    public ResponseEntity<BillDto> calculateBill(@PathVariable Long orderId) {
        Bill bill = billService.calculateBill(orderId);
        return ResponseEntity.ok(dtoAssembler.toModel(bill));
    }
}