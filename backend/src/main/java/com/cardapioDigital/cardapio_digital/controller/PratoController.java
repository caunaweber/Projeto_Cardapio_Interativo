package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.service.PratoService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PratoController {

    private PratoService pratoService;

    @PostMapping
    public Prato createPrato(){
        return new Prato();
    }


}
