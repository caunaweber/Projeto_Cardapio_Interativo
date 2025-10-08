package com.cardapioDigital.cardapio_digital.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseComandaItemDto(
        @JsonProperty("id") long id,
        @JsonProperty("prato") String prato,
        @JsonProperty("qtd") int qtd                             ) {
}
