package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.service.AparelhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aparelhos")
public class AparelhoController {

    @Autowired
    private AparelhoService aparelhoService;

    @GetMapping
    public ResponseEntity<List<ResponseAparelhoDto>> getAllAparelhos() {
        return ResponseEntity.ok().body(aparelhoService.getAllAparelhos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAparelhoDto> getAparelhosById(@PathVariable Long id) {
        return ResponseEntity.ok().body(aparelhoService.getAparelhoById(id));
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<ResponseAparelhoDto> getByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok(aparelhoService.getAparelhoByDeviceId(deviceId));
    }

    @PostMapping
    public ResponseEntity<ResponseAparelhoDto> create(@RequestBody @Valid CreateAparelhoDto dto) {
        Aparelho aparelho = aparelhoService.createOrReturnAparelho(dto);
        ResponseAparelhoDto response = new ResponseAparelhoDto(aparelho);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/vincular")
    public ResponseEntity<ResponseAparelhoDto> vincular(@PathVariable Long id, @RequestParam Integer mesaNum) {
        return ResponseEntity.ok(aparelhoService.vincularAparelho(id, mesaNum));
    }

    @PutMapping("/{id}/desvincular")
    public ResponseEntity<ResponseAparelhoDto> desvincular(@PathVariable Long id) {
        return ResponseEntity.ok(aparelhoService.desvincularAparelho(id));
    }
}
