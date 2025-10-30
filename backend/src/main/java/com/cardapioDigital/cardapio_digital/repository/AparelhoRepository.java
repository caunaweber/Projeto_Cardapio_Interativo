package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Aparelho;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AparelhoRepository extends JpaRepository<Aparelho, Long> {
    Optional<Aparelho> findByDeviceId(String deviceId);
    Optional<Aparelho> findByMesaNum(@Min(value = 1, message = "Número da mesa deve ser positivo") Integer integer);

    boolean existsByMesaNum(@NotNull int i);
}
