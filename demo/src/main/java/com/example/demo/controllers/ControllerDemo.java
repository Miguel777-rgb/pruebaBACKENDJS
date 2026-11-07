package com.example.demo.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class ControllerDemo {

    @GetMapping("/producto/{id}")
    public Map<String,Object> consultarProducto(@PathVariable(name="id") int id) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id", id);
        map.put("nombre", "Producto "+id);
        return map;
    }
    @PostMapping("/productos/nuevo")
    public Map<String,Object> registrarProducto(@RequestBody Map<String,Object> nuevo) {
        Date ahora = new Date();
        nuevo.put("id", ahora.getTime());
        return nuevo;
    }
}
