package com.maki.pompompurin.service;

import com.maki.pompompurin.model.Maki;
import com.maki.pompompurin.repository.MakiRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MakiService {

    // Los datos ahora viven en PostgreSQL (ya no en una lista en memoria)
    private final MakiRepository makiRepository;

    public MakiService(MakiRepository makiRepository) {
        this.makiRepository = makiRepository;
    }

    public List<Maki> listAll() {
        return makiRepository.findAll();
    }

    public Optional<Maki> findById(Long id) {
        return makiRepository.findById(id);
    }

    public Maki create(Maki maki) {
        // El cliente no envía el id: lo genera la base de datos
        maki.setId(null);
        return makiRepository.save(maki);
    }

    public Optional<Maki> update(Long id, Maki datos) {
        // Si existe, copiamos los nuevos valores y guardamos; si no, Optional vacío (404)
        return makiRepository.findById(id).map(maki -> {
            maki.setName(datos.getName());
            maki.setPrice(datos.getPrice());
            maki.setPieces(datos.getPieces());
            maki.setIngredients(datos.getIngredients());
            maki.setCosto(datos.getCosto());
            return makiRepository.save(maki);
        });
    }

    public boolean delete(Long id) {
        if (!makiRepository.existsById(id)) {
            return false;
        }
        makiRepository.deleteById(id);
        return true;
    }
}
