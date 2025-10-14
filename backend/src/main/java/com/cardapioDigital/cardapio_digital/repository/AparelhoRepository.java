package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Aparelho;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AparelhoRepository extends JpaRepository<Aparelho, Long> {
    boolean existsByDeviceId(String deviceId);
}
