package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.model.Aparelho;

import java.time.LocalDateTime;

public record ResponseAparelhoDto(
        Long id,
        String deviceId,
        LocalDateTime dataRegistro,
        Long mesaVinculadaId
) {
    public ResponseAparelhoDto(Aparelho aparelho) {
        this(
                aparelho.getId(),
                aparelho.getDeviceId(),
                aparelho.getDataRegistro(),
                aparelho.getMesaVinculada() != null ? aparelho.getMesaVinculada().getId() : null
        );
    }
}