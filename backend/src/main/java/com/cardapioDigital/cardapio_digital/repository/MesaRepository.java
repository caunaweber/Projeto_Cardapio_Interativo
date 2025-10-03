package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<Mesa, Long> {
    boolean existsByMesaNum(int mesaNum);
    boolean existsByAparelhoVincId(String aparelhoVincId);

    boolean existsByMesaNumAndIdNot(int mesaNum, Long id);
    boolean existsByAparelhoVincIdAndIdNot(String aparelhoVincId, Long id);
}
