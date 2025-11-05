package com.cardapioDigital.cardapio_digital.dto;

import com.cardapioDigital.cardapio_digital.enums.ComandaStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateComandaStatusDto(@NotNull ComandaStatus status) {}
