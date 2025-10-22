package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.validation.constraints.Min;

public record UpdatePratoDto(String nome,
                             @Min(value = 0, message = "O preço não pode ser negativo")
                             Double preco,
                             Categoria categoria) {
}
