package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePratoDto(
        @NotBlank
        String nome,
        @NotNull
        Double preco,
        @NotNull
        Categoria categoria
) {}
