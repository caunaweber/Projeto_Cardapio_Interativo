package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreateMesaDto;
import com.cardapioDigital.cardapio_digital.dto.ResponseMesaDto;
import com.cardapioDigital.cardapio_digital.dto.UpdateMesaDto;
import com.cardapioDigital.cardapio_digital.model.Mesa;
import com.cardapioDigital.cardapio_digital.repository.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MesaService {

    @Autowired
    MesaRepository mesaRepository;

    @Transactional(readOnly = true)
    public List<ResponseMesaDto> getAllMesas(){
        return mesaRepository
                .findAll()
                .stream()
                .map(mesa -> new ResponseMesaDto(mesa))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponseMesaDto getMesaById(long id){
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrada"));
        return new ResponseMesaDto(mesa);
    }

    @Transactional
    public Mesa createMesa(CreateMesaDto dto){
        if (mesaRepository.existsByMesaNum(dto.mesaNum())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número da mesa já cadastrado");
        }
        if (mesaRepository.existsByAparelhoVincId(dto.aparelhoVincId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aparelho já vinculado a outra mesa");
        }

        Mesa mesa = new Mesa(dto);
        return mesaRepository.save(mesa);
    }

    @Transactional
    public void deleteMesa(long id){
        if(!mesaRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrada");
        }
        mesaRepository.deleteById(id);
    }

    @Transactional
    public Mesa updateMesa(Long id, UpdateMesaDto dto){
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mesa não encontrada"));
        if (mesaRepository.existsByMesaNumAndIdNot(dto.mesaNum(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Número da mesa já cadastrado em outra mesa");
        }
        if (mesaRepository.existsByAparelhoVincIdAndIdNot(dto.aparelhoVincId(), id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Aparelho já vinculado a outra mesa");
        }
        mesa.update(dto);
        return mesa;
    }
}
