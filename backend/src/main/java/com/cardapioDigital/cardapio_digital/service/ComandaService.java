package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaDto;
import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.cardapioDigital.cardapio_digital.repository.ComandaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ComandaService {

    @Autowired
    ComandaRepository comandaRepository;

    @Transactional
    public Comanda createComanda(CreateComandaDto dto){
        int nextComandaNum = comandaRepository.findMaxComandaNum() + 1;
        Comanda comanda = Comanda.createComandaFromDto(dto);
        comanda.setComandaNum(nextComandaNum);
        return comandaRepository.save(comanda);
    }
}

