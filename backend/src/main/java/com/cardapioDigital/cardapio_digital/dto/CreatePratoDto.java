package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePratoDto(

        @NotBlank(message = "O nome do prato não pode ser nulo")
        String nome,

        @NotNull(message = "O preço do prato não pode ser nulo")
        @Min(value = 0, message = "O preço deve ser maior ou igual a 0")
        Double preco,

        @NotNull(message = "A categoria do prato não pode ser nula")
        Categoria categoria,

        String imagem
){
}
