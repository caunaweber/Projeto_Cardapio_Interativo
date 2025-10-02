package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PratoService {

    @Autowired
    private PratoRepository pratoRepository;

    public Prato createPrato(CreatePratoDto dto) {
        Prato prato = new Prato(dto);
        return pratoRepository.save(prato);
    }
}
