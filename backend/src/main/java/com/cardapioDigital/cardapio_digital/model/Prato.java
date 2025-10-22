package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdatePratoDto;
import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Prato")
@Table(name = "Pratos")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double preco;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    private String imagem;

    private int vendasCounter = 0;

    public Prato(CreatePratoDto dto){
        this.nome = dto.nome();
        this.categoria = dto.categoria();;
        this.preco = dto.preco();
    }

    public void update(UpdatePratoDto dto){
        if(dto.nome() != null){
            this.nome = dto.nome();
        }
        if(dto.categoria() != null){
            this.categoria = dto.categoria();
        }
        if(dto.preco() != null){
            this.preco = dto.preco();
        }
    }
}
