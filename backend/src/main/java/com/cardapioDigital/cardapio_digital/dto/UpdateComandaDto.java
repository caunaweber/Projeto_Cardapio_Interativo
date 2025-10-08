package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateComandaDto(
        @NotNull List<CreateComandaItemDto> itens
) {
}
