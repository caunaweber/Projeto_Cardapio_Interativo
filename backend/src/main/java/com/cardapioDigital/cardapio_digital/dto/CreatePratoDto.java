package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePratoDto(
        @NotBlank(message = "O nome não pode ser vazio")
        String nome,

        @NotNull(message = "A categoria não pode ser nula")
        Categoria categoria,

        @NotNull(message = "O preço não pode ser nulo")
        @Min(value = 0, message = "O preço não pode ser negativo")
        Double preco
) {}
