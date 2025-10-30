package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateComandaDto(
        @NotNull
        @Min(value = 0, message ="o número da mesa não pode ser negativo")
        int mesaNum,
        @NotNull
        List<CreateComandaItemDto> itens
) {
}
