package com.jorge.inventoryapi.service;

import com.jorge.inventoryapi.dto.UsuarioRequest;
import com.jorge.inventoryapi.dto.UsuarioResponse;
import com.jorge.inventoryapi.exception.EmailDuplicadoException;
import com.jorge.inventoryapi.exception.UsuarioNoEncontradoException;
import com.jorge.inventoryapi.model.Usuario;
import com.jorge.inventoryapi.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Debe crear un usuario cuando el email no existe")
    void crearUsuario() {
        UsuarioRequest request = crearUsuarioRequest("Carlos Perez", "carlos@mail.com");
        when(usuarioRepository.existsByEmail("carlos@mail.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(new Usuario(1L, "Carlos Perez", "carlos@mail.com"));

        UsuarioResponse response = usuarioService.guardar(request);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombre()).isEqualTo("Carlos Perez");
        assertThat(response.getEmail()).isEqualTo("carlos@mail.com");
        verify(usuarioRepository).existsByEmail("carlos@mail.com");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el email ya existe")
    void emailDuplicado() {
        UsuarioRequest request = crearUsuarioRequest("Carlos Perez", "carlos@mail.com");
        when(usuarioRepository.existsByEmail("carlos@mail.com")).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.guardar(request))
                .isInstanceOf(EmailDuplicadoException.class)
                .hasMessageContaining("carlos@mail.com");

        verify(usuarioRepository).existsByEmail("carlos@mail.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe buscar usuario por email")
    void buscarPorEmail() {
        when(usuarioRepository.findByEmail("ana@mail.com"))
                .thenReturn(Optional.of(new Usuario(1L, "Ana Torres", "ana@mail.com")));

        UsuarioResponse response = usuarioService.buscarPorEmail("ana@mail.com");

        assertThat(response.getNombre()).isEqualTo("Ana Torres");
        assertThat(response.getEmail()).isEqualTo("ana@mail.com");
        verify(usuarioRepository).findByEmail("ana@mail.com");
    }

    @Test
    @DisplayName("Debe lanzar excepcion cuando el usuario no existe")
    void usuarioInexistente() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarPorId(99L))
                .isInstanceOf(UsuarioNoEncontradoException.class)
                .hasMessageContaining("99");

        verify(usuarioRepository).findById(99L);
    }

    private UsuarioRequest crearUsuarioRequest(String nombre, String email) {
        UsuarioRequest request = new UsuarioRequest();
        request.setNombre(nombre);
        request.setEmail(email);
        return request;
    }
}
