package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.ResponseComandaDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateComandaDto;
import com.cardapioDigital.cardapio_digital.service.ComandaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comandas")
public class ComandaController {

    @Autowired
    private ComandaService comandaService;

    @GetMapping
    public ResponseEntity<List<ResponseComandaDto>> getAllComandas() {
        List<ResponseComandaDto> lista = comandaService.getAllComandas();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseComandaDto> getComandaById(@PathVariable Long id) {
        ResponseComandaDto dto = comandaService.getComandaById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteComandaById(@PathVariable Long id) {
        comandaService.deleteComanda(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseComandaDto> updateComandaById(@PathVariable Long id, @RequestBody UpdateComandaDto dto) {
        ResponseComandaDto updated = comandaService.updateComanda(id, dto);
        return ResponseEntity.ok(updated);
    }
}
