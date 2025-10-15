package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseComandaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponsePratoDto;
import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.cardapioDigital.cardapio_digital.service.ComandaService;
import com.cardapioDigital.cardapio_digital.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {

    @Autowired
    private ComandaService comandaService;

    @Autowired
    private PratoService pratoService;

    @PostMapping
    public ResponseEntity<ResponseComandaDto> createComanda(@RequestBody @Valid CreateComandaDto dto){
        Comanda comanda = comandaService.createComanda(dto);
        return ResponseEntity.ok().body(ResponseComandaDto.createComandaResponse(comanda));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePratoDto>> getPratos() {
        return ResponseEntity.ok().body(pratoService.getAllPratos());
    }
}
