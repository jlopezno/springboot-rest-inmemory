package com.example.demo.service;

import com.example.demo.model.Usurious;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService {

    private final List<Usurious> usuarios = new ArrayList<>();

    public UsuarioService() {
        usuarios.add(new Usurious(1L, "Juan", "juan@mail.com"));
        usuarios.add(new Usurious(2L, "Ana", "ana@mail.com"));
    }

    public List<Usurious> listar() {
        return usuarios;
    }

    public Usurious guardar(Usurious usuario) {
        usuarios.add(usuario);
        return usuario;
    }
}
