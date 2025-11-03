package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import com.cardapioDigital.cardapio_digital.model.Prato;

public record   ResponsePratoDto(long id,
                               String nome,
                               Double preco,
                               Categoria categoria,
                               String imagem,
                               int vendas) {

    public ResponsePratoDto(Prato prato){
        this(prato.getId(), prato.getNome(), prato.getPreco(), prato.getCategoria(), prato.getImagem(), prato.getVendasCounter());
    }
}
