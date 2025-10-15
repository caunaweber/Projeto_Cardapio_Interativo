package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.repository.AparelhoRepository;
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

    @Transactional
    public Aparelho createAparelho(CreateAparelhoDto dto) {

        if(aparelhoRepository.existsByDeviceId(dto.deviceId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aparelho com id já existente");
        }

        Aparelho aparelho = new Aparelho(dto);
        return aparelhoRepository.save(aparelho);
    }
}
