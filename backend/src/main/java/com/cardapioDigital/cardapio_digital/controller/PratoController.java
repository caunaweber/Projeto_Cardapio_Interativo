package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponsePratoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.service.PratoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pratos")
public class PratoController {

    @Autowired
    private PratoService pratoService;

    @GetMapping
    public ResponseEntity<List<ResponsePratoDto>> getAllPratos() {
        return ResponseEntity.ok().body(pratoService.listAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponsePratoDto> getPrato(@PathVariable Long id) {
        return ResponseEntity.ok().body(pratoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ResponsePratoDto> createPrato(@RequestBody @Valid CreatePratoDto dto) {
        Prato prato = pratoService.createPrato(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponsePratoDto(prato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePrato(@PathVariable Long id) {
        pratoService.deletePrato(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponsePratoDto> updatePrato(@PathVariable Long id, @RequestBody @Valid UpdatePratoDto dto) {
        Prato prato = pratoService.UpdatePrato(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponsePratoDto(prato));
    }
}

