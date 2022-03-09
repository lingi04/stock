package com.jh.stock.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/quote")
@RestController
public class QuoteController {
    @GetMapping("/{ticker}")
    public void sadf(@PathVariable String ticker) {

    }
}
