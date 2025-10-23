package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateAparelhoDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Aparelho")
@Table(name = "Aparelhos")
@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class Aparelho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String deviceId;

    @Column(nullable = false, name = "data_registry")
    private LocalDateTime dataRegistry;

    private Integer mesaNum;

    private boolean validated = false;

    public Aparelho(CreateAparelhoDto dto){
        this.deviceId = dto.deviceId();
        this.dataRegistry = LocalDateTime.now();
    }

    public void update(UpdateAparelhoDto dto) {
        this.mesaNum = dto.mesaNum();
    }
}
