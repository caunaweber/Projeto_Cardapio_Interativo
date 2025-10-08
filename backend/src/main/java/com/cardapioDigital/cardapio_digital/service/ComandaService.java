package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseComandaDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateComandaDto;
import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.cardapioDigital.cardapio_digital.repository.ComandaRepository;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComandaService {

    @Autowired
    private ComandaRepository comandaRepository;
    @Autowired
    private PratoRepository pratoRepository;

    @Transactional
    public Comanda createComanda(CreateComandaDto dto){
        int nextComandaNum = comandaRepository.findMaxComandaNum() + 1;
        Comanda comanda = Comanda.createComandaFromDto(dto);
        comanda.setComandaNum(nextComandaNum);

        comanda.getItens().forEach(itens -> {
            boolean pratoExistente = pratoRepository.existsById(itens.getPratoId());
            if (!pratoExistente) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prato com id "+ itens.getPratoId()+" não existe");
            }
            if(itens.getQtd() <=0 ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor de quantidade não pode ser menor ou igual a zero");
            }
            pratoRepository.incrementarVendas(itens.getPratoId(), itens.getQtd());
        });
        return comandaRepository.save(comanda);
    }

    @Transactional(readOnly = true)
    public List<ResponseComandaDto> getAllComandas() {
        List<Comanda> comandas = comandaRepository.findAllWithItensOrderByDataCriacaoDesc();
        return comandas
                .stream()
                .map(ResponseComandaDto::createComandaResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseComandaDto getComandaById(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));
        return ResponseComandaDto.createComandaResponse(comanda);
    }

    @Transactional
    public void deleteComanda(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda inexistente"));
        comandaRepository.delete(comanda);
    }

    @Transactional
    public ResponseComandaDto updateComanda(Long id, UpdateComandaDto dto) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda inexistente"));

        comanda.getItens().forEach(itens -> {
            pratoRepository.incrementarVendas(itens.getPratoId(), -itens.getQtd());
        });

        comanda.getItens().clear();

        dto.itens().forEach(itemDto -> {
            if (!pratoRepository.existsById(itemDto.pratoId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prato com id " + itemDto.pratoId() + " não existe");
            }
            if (itemDto.qtd() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade deve ser maior que zero");
            }
            pratoRepository.incrementarVendas(itemDto.pratoId(), itemDto.qtd());
            comanda.adicionarItem(
                    com.cardapioDigital.cardapio_digital.model.ComandaItens.createComandaItem(itemDto)
            );
        });
        Comanda comandaAtualizada = comandaRepository.save(comanda);
        return ResponseComandaDto.createComandaResponse(comandaAtualizada);
    }
}

