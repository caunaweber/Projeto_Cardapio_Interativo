package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;

public record UpdatePratoDto(String nome,
                             Double preco,
                             Categoria categoria,
                             String imagem) {
}
