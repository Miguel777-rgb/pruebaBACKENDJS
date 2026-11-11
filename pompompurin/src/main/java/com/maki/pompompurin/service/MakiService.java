package com.maki.pompompurin.service;

import com.maki.pompompurin.model.Maki;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MakiService {

    // "Base de datos" en memoria: se reinicia cada vez que se apaga la app
    private final List<Maki> makis = new ArrayList<>();
    private long nextId = 1;

    public MakiService() {
        create(new Maki(null, "Acevichado", 25.0, 10, "Langostino, palta, salsa acevichada"));
        create(new Maki(null, "Furai", 22.0, 10, "Langostino empanizado, queso crema"));
    }

    public List<Maki> listAll() {
        return makis;
    }

    public Optional<Maki> findById(Long id) {
        for (Maki maki : makis) {
            if (maki.id().equals(id)) {
                return Optional.of(maki);
            }
        }
        return Optional.empty();
    }

    public Maki create(Maki maki) {
        // El cliente no envía el id: lo genera el servidor
        Maki nuevo = new Maki(nextId++, maki.name(), maki.price(), maki.pieces(), maki.ingredients());
        makis.add(nuevo);
        return nuevo;
    }
}
