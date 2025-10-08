package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Comanda;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ComandaRepository extends JpaRepository<Comanda, Long> {

    @Query("SELECT COALESCE(MAX(c.comandaNum), 0) FROM Comanda c")
    int findMaxComandaNum();

    @EntityGraph(attributePaths = {"itens"})
    @Query("select distinct c from Comanda c order by c.dataCriacao desc")
    List<Comanda> findAllWithItensOrderByDataCriacaoDesc();

    @EntityGraph(attributePaths = {"itens"})
    Optional<Comanda> findById(Long id);
}
