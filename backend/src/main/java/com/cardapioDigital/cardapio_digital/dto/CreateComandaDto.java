package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateComandaDto(
        @NotNull int mesaNum,
        @NotNull List<CreateComandaItemDto> itens
) {
}
