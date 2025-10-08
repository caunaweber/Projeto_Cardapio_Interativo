package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseComandaDto;
import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.cardapioDigital.cardapio_digital.service.ComandaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comandas")
public class ComandaController {

    @Autowired
    private ComandaService comandaService;

    @PostMapping
    public ResponseEntity<ResponseComandaDto> createComanda(@RequestBody @Valid CreateComandaDto dto){
        Comanda comanda = comandaService.createComanda(dto);
        return ResponseEntity.ok().body(ResponseComandaDto.createComandaResponse(comanda));
    }
}
