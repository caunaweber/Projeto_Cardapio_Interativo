package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.enums.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Prato")
@Table(name = "Pratos")
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Prato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Double preco;

    @Enumerated(EnumType.STRING)
    Categoria categoria;

    private String imagem;
}
