package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponsePratoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PratoService {

    @Autowired
    private PratoRepository pratoRepository;

    @Transactional(readOnly = true)
    public List<ResponsePratoDto> listAll(){
        return pratoRepository.findAll()
                .stream()
                .map(prato -> new ResponsePratoDto(prato))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponsePratoDto findById(long id) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
                return new ResponsePratoDto(prato);
    }

    @Transactional
    public Prato createPrato(CreatePratoDto dto) {
        Prato prato = new Prato(dto);
        return pratoRepository.save(prato);
    }

    @Transactional
    public void deletePrato(long id) {
        if(!pratoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado");
        }
        pratoRepository.deleteById(id);
    }

    @Transactional
    public Prato updatePrato(long id, UpdatePratoDto dto) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
        prato.update(dto);
        return prato;
    }

    @Transactional
    public void registrarVenda(Long id, int qtd){
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
        pratoRepository.incrementarVendas(id, qtd);
    }
}
