package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.model.Aparelho;

import java.time.LocalDateTime;

public record ResponseAparelhoDto(
        Long id,
        String deviceId,
        LocalDateTime dataRegistry,
        Integer mesaNum
) {
    public ResponseAparelhoDto(Aparelho aparelho) {
        this(
                aparelho.getId(),
                aparelho.getDeviceId(),
                aparelho.getDataRegistry(),
                aparelho.getMesaNum()
        );
    }
}