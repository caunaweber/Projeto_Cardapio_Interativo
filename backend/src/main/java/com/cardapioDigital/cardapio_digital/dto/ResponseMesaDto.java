package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.model.Mesa;
import org.jetbrains.annotations.NotNull;

public record ResponseMesaDto(Long id, int mesaNum, String aparelhoVincId) {

    public ResponseMesaDto(@NotNull Mesa mesa) {
        this(mesa.getId(), mesa.getMesaNum(), mesa.getAparelhoVincId());
    }
}
