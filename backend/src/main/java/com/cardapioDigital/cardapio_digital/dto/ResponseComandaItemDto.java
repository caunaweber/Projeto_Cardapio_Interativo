package com.cardapioDigital.cardapio_digital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseComandaItemDto(
        @JsonProperty("id") long id,
        @JsonProperty("pratoId") Long pratoId,
        @JsonProperty("pratoNome") String pratoNome,
        @JsonProperty("qtd") int qtd
) {
}
