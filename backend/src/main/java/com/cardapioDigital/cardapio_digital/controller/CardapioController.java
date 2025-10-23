package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.*;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.service.AparelhoService;
import com.cardapioDigital.cardapio_digital.service.ComandaService;
import com.cardapioDigital.cardapio_digital.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @Autowired
    AparelhoService aparelhoService;

    @PostMapping
    public ResponseEntity<ResponseComandaDto> createComanda(@RequestBody @Valid CreateComandaDto dto){
        return ResponseEntity.ok().body(comandaService.createComanda(dto));
    }

    @GetMapping
    public ResponseEntity<List<ResponsePratoDto>> getPratos() {
        return ResponseEntity.ok().body(pratoService.getAllPratos());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseAparelhoDto> create(@RequestBody @Valid CreateAparelhoDto dto) {
        Aparelho aparelho = aparelhoService.createOrReturnAparelho(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAparelhoDto(aparelho));
    }

    @GetMapping("/verify/{deviceId}")
    public ResponseEntity<ResponseAparelhoDto> verifyByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok().body(aparelhoService.verifiyAparelhoByDeviceId(deviceId));
    }
}
