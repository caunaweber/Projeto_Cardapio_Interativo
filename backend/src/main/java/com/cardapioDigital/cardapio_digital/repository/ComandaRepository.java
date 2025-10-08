package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Comanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    @Query("SELECT COALESCE(MAX(c.comandaNum), 0) FROM Comanda c")
    int findMaxComandaNum();
}
