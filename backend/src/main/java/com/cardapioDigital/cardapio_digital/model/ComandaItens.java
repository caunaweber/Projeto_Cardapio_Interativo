package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaItemDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity(name = "Item")
@Table(name = "ComandaItens")
@EqualsAndHashCode(of = "id")
@Data
public class ComandaItens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private long pratoId;

    @NotNull
    private int qtd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

    public static ComandaItens createComandaItem(CreateComandaItemDto dto){
        ComandaItens item = new ComandaItens();
        item.setPratoId(dto.pratoId());
        item.setQtd(dto.qtd());
        return item;
    }
}

