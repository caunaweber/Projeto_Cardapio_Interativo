package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.Min;

public record UpdateAparelhoDto(
        @Min(value = 1, message = "Número da mesa deve ser positivo")
        Integer mesaNum
) {}