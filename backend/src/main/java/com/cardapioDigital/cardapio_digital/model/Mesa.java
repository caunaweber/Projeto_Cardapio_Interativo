package com.cardapioDigital.cardapio_digital.model;

import com.cardapioDigital.cardapio_digital.dto.CreateMesaDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateMesaDto;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Mesa")
@Table(name = "Mesas")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int mesaNum;

    @Column(nullable = false, unique = true)
    private String aparelhoVincId;

    public Mesa(CreateMesaDto dto){
        this.mesaNum = dto.mesaNum();
        this.aparelhoVincId = dto.aparelhoVincId();
    }

    public void update(UpdateMesaDto dto){
        if(dto.mesaNum() != null){
            this.mesaNum = dto.mesaNum();
        }
        if(dto.aparelhoVincId() != null){
            this.aparelhoVincId = dto.aparelhoVincId();
        }
    }
}
