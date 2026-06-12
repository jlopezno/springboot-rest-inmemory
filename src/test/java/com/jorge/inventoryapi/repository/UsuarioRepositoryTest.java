package com.jorge.inventoryapi.repository;

import com.jorge.inventoryapi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = "spring.sql.init.mode=never")
class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void limpiarBaseDeDatos() {
        usuarioRepository.deleteAll();
    }

    @Test
    @DisplayName("Debe guardar un usuario y consultarlo por id")
    void guardarUsuario() {
        Usuario usuario = new Usuario(null, "Carlos Perez", "carlos@mail.com");

        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Optional<Usuario> resultado = usuarioRepository.findById(usuarioGuardado.getId());
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Carlos Perez");
        assertThat(resultado.get().getEmail()).isEqualTo("carlos@mail.com");
    }

    @Test
    @DisplayName("Debe buscar usuario por email")
    void buscarPorEmail() {
        usuarioRepository.save(new Usuario(null, "Ana Torres", "ana@mail.com"));

        Optional<Usuario> resultado = usuarioRepository.findByEmail("ana@mail.com");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNombre()).isEqualTo("Ana Torres");
    }

    @Test
    @DisplayName("Debe validar existencia por email")
    void existePorEmail() {
        usuarioRepository.save(new Usuario(null, "Luis Gomez", "luis@mail.com"));

        boolean existe = usuarioRepository.existsByEmail("luis@mail.com");
        boolean noExiste = usuarioRepository.existsByEmail("otro@mail.com");

        assertThat(existe).isTrue();
        assertThat(noExiste).isFalse();
    }

    @Test
    @DisplayName("Debe validar email duplicado excluyendo un id")
    void existePorEmailExcluyendoId() {
        Usuario usuario = usuarioRepository.save(new Usuario(null, "Maria Ruiz", "maria@mail.com"));
        Usuario otroUsuario = usuarioRepository.save(new Usuario(null, "Pedro Rojas", "pedro@mail.com"));

        boolean mismoUsuario = usuarioRepository.existsByEmailAndIdNot("maria@mail.com", usuario.getId());
        boolean otroUsuarioConEseEmail = usuarioRepository.existsByEmailAndIdNot("maria@mail.com", otroUsuario.getId());

        assertThat(mismoUsuario).isFalse();
        assertThat(otroUsuarioConEseEmail).isTrue();
    }
}
