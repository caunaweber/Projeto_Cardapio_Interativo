package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.dto.CreateComandaDto;
import com.cardapioDigital.cardapio_digital.dto.CreateComandaItemDto;
import com.cardapioDigital.cardapio_digital.enums.ComandaStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Comanda")
@Table(name = "Comandas")
@Data
@EqualsAndHashCode(of = "id")
public class Comanda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int comandaNum;

    @NotNull
    private int mesaNum;

    @Enumerated(EnumType.STRING)
    @NotNull
    ComandaStatus status = ComandaStatus.A_ENTREGAR;

    @NotNull
    private LocalDateTime dataCriacao = LocalDateTime.now();

    @OneToMany(mappedBy = "comanda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ComandaItens> itens = new ArrayList<>();

    public void adicionarItem(ComandaItens item) {
        item.setComanda(this);
        this.itens.add(item);
    }

    public static Comanda createComandaFromDto(CreateComandaDto dto){
        Comanda comanda = new Comanda();
        comanda.setStatus(ComandaStatus.A_ENTREGAR);
        comanda.setMesaNum(dto.mesaNum());

        for(CreateComandaItemDto itensDto : dto.itens()){
            comanda.adicionarItem(ComandaItens.createComandaItem(itensDto));
        }

        return comanda;
    }
}
