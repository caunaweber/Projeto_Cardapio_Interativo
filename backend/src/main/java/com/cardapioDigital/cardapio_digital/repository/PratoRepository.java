package com.cardapioDigital.cardapio_digital.repository;

import com.cardapioDigital.cardapio_digital.model.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PratoRepository extends JpaRepository<Prato,Long> {

    @Modifying
    @Query("UPDATE Prato p SET p.vendasCounter = p.vendasCounter + :qtd WHERE p.id = :id")
    void incrementarVendas(@Param("id") Long id, @Param("qtd") int qtd);

}
