package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.service.PratoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    @Autowired
    private PratoService pratoService;

    @PostMapping
    @Transactional
    public ResponseEntity createPrato(@RequestBody @Valid CreatePratoDto dto) {
        Prato prato = pratoService.createPrato(dto);
        //System.out.println(prato);
        return ResponseEntity.status(HttpStatus.CREATED).body(prato);
    }


}
