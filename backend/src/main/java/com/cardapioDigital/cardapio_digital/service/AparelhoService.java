package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateAparelhoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseAparelhoDto;
import com.cardapioDigital.cardapio_digital.model.Aparelho;
import com.cardapioDigital.cardapio_digital.repository.AparelhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.secret.key}")
    private String secretKey;

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
    public Aparelho createOrReturnAparelho(CreateAparelhoDto dto) {
        return aparelhoRepository.findByDeviceId(dto.deviceId())
                .orElseGet(() -> aparelhoRepository.save(new Aparelho(dto)));
    }

    @Transactional(readOnly = true)
    public ResponseAparelhoDto verifiyAparelhoByDeviceId(String deviceId) {
        Aparelho aparelho = aparelhoRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontado"));

        if (!aparelho.isValidated()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Aparelho não autorizado");
        }
        return new ResponseAparelhoDto(aparelho);
    }

    @Transactional
    public void validateAparelho(String deviceId, String providedKey) {
        Aparelho aparelho = aparelhoRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Aparelho não encontrado"));

        if (!providedKey.equals(secretKey)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Chave de acesso inválida");
        }

        aparelho.setValidated(true);
        aparelhoRepository.save(aparelho);
    }

}
