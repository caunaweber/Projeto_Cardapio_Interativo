package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.NotNull;

public record CreateMesaDto(
        @NotNull int mesaNum,
        @NotNull String aparelhoVincId
) {
}
