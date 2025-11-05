package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateComandaItemDto(
        @NotNull Long pratoId,
        @NotNull @Min(1) int qtd
) {
}
