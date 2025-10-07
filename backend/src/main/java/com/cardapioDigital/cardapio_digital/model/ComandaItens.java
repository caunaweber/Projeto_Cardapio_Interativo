package com.cardapioDigital.cardapio_digital.model;

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
    private String prato;

    @NotNull
    private int qtd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comanda_id")
    private Comanda comanda;

}

