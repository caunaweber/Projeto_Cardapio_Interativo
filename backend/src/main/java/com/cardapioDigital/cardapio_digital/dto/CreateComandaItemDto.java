package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateComandaItemDto(
        @NotBlank String prato,
        @NotNull  int qtd
) {
}
