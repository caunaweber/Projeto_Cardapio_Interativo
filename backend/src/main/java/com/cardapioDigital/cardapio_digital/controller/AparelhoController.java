package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.service.AparelhoService;
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

    @PostMapping
        public ResponseEntity<ResponseAparelhoDto> createAparelho(@RequestBody CreateAparelhoDto dto){
            Aparelho aparelho = aparelhoService.createAparelho(dto);
            return ResponseEntity.status(201).body(new ResponseAparelhoDto(aparelho));
    }

}
