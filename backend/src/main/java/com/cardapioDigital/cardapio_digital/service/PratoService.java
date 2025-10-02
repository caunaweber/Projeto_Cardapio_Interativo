package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.stereotype.Service;

@Service
public class PratoService {

    private PratoRepository pratoRepository;

    public Prato createPrato(Prato prato) {
        return pratoRepository.save(prato);
    }
}
