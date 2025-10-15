package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.model.Mesa;
import com.cardapioDigital.cardapio_digital.repository.AparelhoRepository;
import com.cardapioDigital.cardapio_digital.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AparelhoService {

    @Autowired
    private AparelhoRepository aparelhoRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Transactional(readOnly = true)
    public List<ResponseAparelhoDto> getAllAparelhos() {
        return aparelhoRepository.findAll()
                .stream()
                .map(aparelho -> new ResponseAparelhoDto(aparelho))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseAparelhoDto getAparelhoById(Long id) {
        Aparelho aparelho = aparelhoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontado"));

        return new ResponseAparelhoDto(aparelho);
    }

    @Transactional(readOnly = true)
    public ResponseAparelhoDto getAparelhoByDeviceId(String deviceId) {
        Aparelho aparelho = aparelhoRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontrado"));
        return new ResponseAparelhoDto(aparelho);
    }

    @Transactional
    public Aparelho createOrReturnAparelho(CreateAparelhoDto dto) {
        return aparelhoRepository.findByDeviceId(dto.deviceId())
                .orElseGet(() -> aparelhoRepository.save(new Aparelho(dto)));
    }

    @Transactional
    public ResponseAparelhoDto vincularAparelho(Long aparelhoId, Long mesaId) {
        Aparelho aparelho = aparelhoRepository.findById(aparelhoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontrado"));
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrada"));

        aparelho.setMesaVinculada(mesa);
        return new ResponseAparelhoDto(aparelhoRepository.save(aparelho));
    }

    @Transactional
    public ResponseAparelhoDto desvincularAparelho(Long aparelhoId) {
        Aparelho aparelho = aparelhoRepository.findById(aparelhoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontrado"));
        aparelho.setMesaVinculada(null);
        Aparelho salvo = aparelhoRepository.save(aparelho);
        return new ResponseAparelhoDto(salvo);
    }
}
