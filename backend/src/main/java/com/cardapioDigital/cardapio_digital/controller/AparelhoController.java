package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ValidateAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.service.AparelhoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/verify/{deviceId}")
    public ResponseEntity<ResponseAparelhoDto> verifyByDeviceId(@PathVariable String deviceId) {
        return ResponseEntity.ok().body(aparelhoService.verifiyAparelhoByDeviceId(deviceId));
    }

    @PostMapping("/validate/{deviceId}")
    public ResponseEntity<Void> validateAparelhoByDeviceId(@PathVariable String deviceId, @RequestBody ValidateAparelhoDto dto) {
        aparelhoService.validateAparelho(deviceId, dto.key());
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<ResponseAparelhoDto> create(@RequestBody @Valid CreateAparelhoDto dto) {
        Aparelho aparelho = aparelhoService.createOrReturnAparelho(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseAparelhoDto(aparelho));
    }

}
