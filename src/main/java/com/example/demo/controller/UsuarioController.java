package com.example.demo.controller;

import com.example.demo.model.Usurious;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usurious> listar() {
        return usuarioService.listar();
    }

    @PostMapping
    public Usurious guardar(@RequestBody Usurious usuario) {
        return usuarioService.guardar(usuario);
    }
}
