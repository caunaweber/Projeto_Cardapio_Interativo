package com.cardapioDigital.cardapio_digital.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAparelhoDto(
        @NotBlank
        String deviceId
) {
}
