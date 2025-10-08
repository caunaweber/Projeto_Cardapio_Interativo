package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.model.Comanda;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ResponseComandaDto(
        @JsonProperty("id") long id,
        @JsonProperty("comandaNum") int comandaNum,
        @JsonProperty("mesaNum") int mesaNum,
        @JsonProperty("status") String status,
        @JsonProperty("dataCriacao") LocalDateTime dataCriacao,
        @JsonProperty("itens") List<ResponseComandaItemDto> itens
)  {
    public static ResponseComandaDto createComandaResponse(Comanda comanda){
        List<ResponseComandaItemDto> itens = comanda.getItens()
                .stream()
                .map(item -> new ResponseComandaItemDto(
                        item.getId(),
                        item.getPrato(),
                        item.getQtd()
                )).collect(Collectors.toList());
        return new ResponseComandaDto(
                comanda.getId(),
                comanda.getComandaNum(),
                comanda.getMesaNum(),
                comanda.getStatus().name(),
                comanda.getDataCriacao(),
                itens
        );
    }
}
