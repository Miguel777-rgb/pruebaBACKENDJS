package com.maki.pompompurin.controller;

import com.maki.pompompurin.model.Maki;
import com.maki.pompompurin.service.MakiService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/makis")
public class MakiController {

    private final MakiService makiService;

    // Inyección de dependencias por constructor
    public MakiController(MakiService makiService) {
        this.makiService = makiService;
    }

    // READ - GET http://localhost:8081/api/makis -> 200 OK (todos los makis)
    @GetMapping
    public List<Maki> listAll() {
        return makiService.listAll();
    }

    // READ - GET http://localhost:8081/api/makis/{id} -> 200 OK o 404 Not Found
    @GetMapping("/{id}")
    public ResponseEntity<Maki> findById(@PathVariable("id") Long id) {
        return ResponseEntity.of(makiService.findById(id));
    }

    // CREATE - POST http://localhost:8081/api/makis -> 201 Created
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Maki create(@RequestBody Maki maki) {
        return makiService.create(maki);
    }

    // UPDATE - PUT http://localhost:8081/api/makis/{id} -> 200 OK o 404 Not Found
    @PutMapping("/{id}")
    public ResponseEntity<Maki> update(@PathVariable("id") Long id, @RequestBody Maki maki) {
        return ResponseEntity.of(makiService.update(id, maki));
    }

    // DELETE - DELETE http://localhost:8081/api/makis/{id} -> 204 No Content o 404 Not Found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (makiService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
