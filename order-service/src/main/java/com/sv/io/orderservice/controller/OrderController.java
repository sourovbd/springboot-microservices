package com.sv.io.orderservice.controller;

import com.sv.io.orderservice.model.OrderRequest;
import com.sv.io.orderservice.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest) {

        long orderId = orderService.placeOrder(orderRequest);

        log.info("Order id: " + orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }

}



