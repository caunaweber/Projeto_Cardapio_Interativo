package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponsePratoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PratoService {

    @Autowired
    private PratoRepository pratoRepository;

    @Transactional(readOnly = true)
    public List<ResponsePratoDto> getAllPratos() {
        return pratoRepository.findAll()
                .stream()
                .map(prato -> new ResponsePratoDto(prato))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ResponsePratoDto getPratoById(long id) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
                return new ResponsePratoDto(prato);
    }

    @Transactional
    public Prato createPrato(CreatePratoDto dto, MultipartFile imagem) {
        Prato prato = new Prato(dto);

        if (imagem != null && !imagem.isEmpty()) {
            try{
                String pastaUpload = "uploads/";
                String nomeArquivo = UUID.randomUUID() + "_" + imagem.getOriginalFilename();
                Path path = Paths.get(pastaUpload, nomeArquivo);

                Files.createDirectories(path.getParent());
                Files.copy(imagem.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                prato.setImagem(nomeArquivo);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar prato");
            }
        }
        return pratoRepository.save(prato);
    }

    @Transactional
    public void deletePrato(long id) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));

        if (prato.getImagem() != null && !prato.getImagem().isEmpty()) {
            Path caminhoImagem = Paths.get("uploads", prato.getImagem());
            try {
                Files.deleteIfExists(caminhoImagem);
            } catch (IOException e) {
                System.err.println("⚠️ Erro ao excluir imagem: " + e.getMessage());
            }
        }

        pratoRepository.deleteById(id);
    }

    @Transactional
    public Prato updatePrato(long id, UpdatePratoDto dto) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
        prato.update(dto);
        return prato;
    }

    @Transactional
    public void registrarVenda(Long id, int qtd){
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));
        pratoRepository.incrementarVendas(id, qtd);
    }
}
