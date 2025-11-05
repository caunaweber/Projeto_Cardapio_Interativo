package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.*;
import com.cardapioDigital.cardapio_digital.enums.ComandaStatus;
import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.cardapioDigital.cardapio_digital.model.ComandaItens;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.AparelhoRepository;
import com.cardapioDigital.cardapio_digital.repository.ComandaRepository;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ComandaService {

    @Autowired
    private ComandaRepository comandaRepository;
    @Autowired
    private PratoRepository pratoRepository;

    @Autowired
    private AparelhoRepository aparelhoRepository;

    @Transactional
    public ResponseComandaDto createComanda(CreateComandaDto dto){
        int nextComandaNum = Optional.ofNullable(comandaRepository.findMaxComandaNum()).orElse(0) + 1;
        Comanda comanda = Comanda.createComandaFromDto(dto);
        comanda.setComandaNum(nextComandaNum);

        if(!aparelhoRepository.existsByMesaNum(dto.mesaNum())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "A mesa vinculada a esse aparelho não existe ou não está configurada");
        }

        Set<Long> pratoIds = dto.itens().stream()
                .map(CreateComandaItemDto::pratoId)
                        .collect(Collectors.toSet());

        Map<Long, String> pratoNomeMap = getPratoNomeMap(pratoIds);

        comanda.getItens().forEach(itens -> {

            if(!pratoNomeMap.containsKey(itens.getPratoId())){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prato com id "+ itens.getPratoId()+" não existe");
            }

            if(itens.getQtd() <=0 ){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor de quantidade não pode ser menor ou igual a zero");
            }
            pratoRepository.incrementarVendas(itens.getPratoId(), itens.getQtd());
        });
        return ResponseComandaDto.createComandaResponse(comandaRepository.save(comanda), pratoNomeMap);
    }

    @Transactional(readOnly = true)
    public List<ResponseComandaDto> getAllComandas() {
        List<Comanda> comandas = comandaRepository.findAllWithItensOrderByDataCriacaoDesc();

        Map<Long, String> pratoNomeMap = getAllPratoNomesMap();

        return comandas
                .stream()
                .map(comanda -> ResponseComandaDto.createComandaResponse(comanda, pratoNomeMap))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseComandaDto getComandaById(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));

        Set<Long> pratoIds = comanda.getItens().stream()
                .map(ComandaItens::getPratoId)
                .collect(Collectors.toSet());

        Map<Long, String> pratoNomeMap = getPratoNomeMap(pratoIds);

        return ResponseComandaDto.createComandaResponse(comanda, pratoNomeMap);
    }

    @Transactional
    public void deleteComanda(Long id) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda inexistente"));
        comandaRepository.delete(comanda);
    }

    @Transactional
    public ResponseComandaDto atualizarStatus(Long id, UpdateComandaStatusDto dto) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda não encontrada"));

        try {
            comanda.setStatus(ComandaStatus.valueOf(String.valueOf(dto.status()).toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status inválido: " + dto.status());
        }
        comandaRepository.save(comanda);

        Set<Long> pratoIds = comanda.getItens().stream()
                .map(ComandaItens::getPratoId)
                .collect(Collectors.toSet());

        Map<Long, String> pratoNomeMap = getPratoNomeMap(pratoIds);

        return ResponseComandaDto.createComandaResponse(comanda, pratoNomeMap);
    }



    @Transactional
    public ResponseComandaDto updateComanda(Long id, UpdateComandaDto dto) {
        Comanda comanda = comandaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Comanda inexistente"));

        Set<Long> pratoIds = dto.itens().stream()
                .map(CreateComandaItemDto::pratoId)
                .collect(Collectors.toSet());

        Map<Long, String> pratoNomeMap = getPratoNomeMap(pratoIds);

        comanda.getItens().forEach(itens -> {
            pratoRepository.incrementarVendas(itens.getPratoId(), -itens.getQtd());
        });

        comanda.getItens().clear();

        if (dto.itens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A comanda deve conter pelo menos um item");
        }

        dto.itens().forEach(itemDto -> {
            if (!pratoNomeMap.containsKey(itemDto.pratoId())) {
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
        return ResponseComandaDto.createComandaResponse(comandaRepository.save(comanda), pratoNomeMap);
    }

    @Transactional
    public void deleteAllEntregues() {
        List<Comanda> entregues = comandaRepository.findByStatus(ComandaStatus.ENTREGUE);
        if (entregues.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Nenhuma comanda entregue encontrada para exclusão");
        }
        comandaRepository.deleteAll(entregues);
    }


    private Map<Long, String> getPratoNomeMap(Set<Long> pratoIds) {
        return pratoRepository.findAllById(pratoIds)
                .stream()
                .collect(Collectors.toMap(Prato::getId, Prato::getNome));
    }

    private Map<Long, String> getAllPratoNomesMap() {
        return pratoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Prato::getId, Prato::getNome));
    }
}

