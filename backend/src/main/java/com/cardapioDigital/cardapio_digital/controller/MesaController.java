package com.cardapioDigital.cardapio_digital.controller;

import com.cardapioDigital.cardapio_digital.dto.CreateMesaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseMesaDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateMesaDto;
import com.cardapioDigital.cardapio_digital.model.Mesa;
import com.cardapioDigital.cardapio_digital.service.MesaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mesas")
public class MesaController {

    @Autowired
    MesaService mesaService;

    @PostMapping
    public ResponseEntity<ResponseMesaDto> createMesa(@RequestBody @Valid CreateMesaDto dto){
        Mesa mesa = mesaService.createMesa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseMesaDto(mesa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteMesa(@PathVariable Long id){
        mesaService.deleteMesa(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMesaDto> updateMesa(@PathVariable Long id, @RequestBody @Valid UpdateMesaDto dto){
        Mesa mesa = mesaService.updateMesa(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMesaDto(mesa));
    }
}
