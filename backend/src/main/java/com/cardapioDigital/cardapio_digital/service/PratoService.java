package com.cardapioDigital.cardapio_digital.service;

import com.cardapioDigital.cardapio_digital.dto.CreatePratoDto;
import com.cardapioDigital.cardapio_digital.dto.ResponsePratoDto;
import com.cardapioDigital.cardapio_digital.dto.UpdatePratoDto;
import com.cardapioDigital.cardapio_digital.model.Prato;
import com.cardapioDigital.cardapio_digital.repository.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
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

    @Value("${file.upload-dir}")
    private String pastaUpload;

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
    public ResponsePratoDto createPrato(CreatePratoDto dto, MultipartFile imagem) {
        Prato prato = new Prato(dto);

        if (imagem != null && !imagem.isEmpty()) {
            try{
                ImageManager(imagem, prato);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao criar prato");
            }
        }

        return new ResponsePratoDto(pratoRepository.save(prato));
    }
    @Transactional
    public ResponsePratoDto updatePrato(long id, UpdatePratoDto dto, MultipartFile imagem)  {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));

        if (imagem != null && !imagem.isEmpty()) {
            try {
                if (prato.getImagem() != null && !prato.getImagem().isEmpty()) {
                    imageDeleter(prato.getImagem());
                }
                ImageManager(imagem, prato);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar nova imagem");
            }
        }

        prato.update(dto);
        return new ResponsePratoDto(prato);
    }

    @Transactional
    public void deletePrato(long id) {
        Prato prato = pratoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado"));

        if (prato.getImagem() != null && !prato.getImagem().isEmpty()) {
            imageDeleter(prato.getImagem());
        }

        pratoRepository.deleteById(id);
    }

    private void imageDeleter(String image){
        Path imagePath = Paths.get(pastaUpload, image);
        try {
            Files.deleteIfExists(imagePath);
        } catch (ResponseStatusException | IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao deletar imagem do prato"+e.getMessage());
        }
    }

    private void ImageManager(MultipartFile imagem, Prato prato) throws IOException {
        String originalFilename = StringUtils.cleanPath(imagem.getOriginalFilename());
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IOException("Nome do arquivo inválido");
        }
        String nomeArquivo = UUID.randomUUID() + "_" + originalFilename;
        Path path = Paths.get(pastaUpload, nomeArquivo);

        Files.createDirectories(path.getParent());
        Files.copy(imagem.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);


        prato.setImagem(nomeArquivo);
    }
}
