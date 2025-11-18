package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import com.cardapioDigital.cardapio_digital.model.Prato;

public record ResponseMetricasDto(
        String nome,
        Categoria categoria,
        int vendas
) {
    public ResponseMetricasDto(Prato prato) {
        this(prato.getNome(), prato.getCategoria(), prato.getVendasCounter());
    }
}