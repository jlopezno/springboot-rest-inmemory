package com.jorge.inventoryapi.service;

import com.jorge.inventoryapi.dto.UsuarioRequest;
import com.jorge.inventoryapi.dto.UsuarioResponse;
import com.jorge.inventoryapi.exception.EmailDuplicadoException;
import com.jorge.inventoryapi.exception.UsuarioNoEncontradoException;
import com.jorge.inventoryapi.model.Usuario;
import com.jorge.inventoryapi.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioResponse> listar() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    public UsuarioResponse guardar(UsuarioRequest usuarioRequest) {
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            throw new EmailDuplicadoException(usuarioRequest.getEmail());
        }

        Usuario usuario = convertirAModelo(usuarioRequest);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return convertirAResponse(usuarioGuardado);
    }

    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        return convertirAResponse(usuario);
    }

    public UsuarioResponse buscarPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNoEncontradoException(email));
        return convertirAResponse(usuario);
    }

    public UsuarioResponse actualizar(Long id, UsuarioRequest usuarioActualizado) {
        if (usuarioRepository.existsByEmailAndIdNot(usuarioActualizado.getEmail(), id)) {
            throw new EmailDuplicadoException(usuarioActualizado.getEmail());
        }

        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        return convertirAResponse(usuarioGuardado);
    }

    public void eliminar(Long id) {
        obtenerUsuarioPorId(id);
        usuarioRepository.deleteById(id);
    }

    private Usuario convertirAModelo(UsuarioRequest usuarioRequest) {
        return new Usuario(
                null,
                usuarioRequest.getNombre(),
                usuarioRequest.getEmail()
        );
    }

    private UsuarioResponse convertirAResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }

    private Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNoEncontradoException(id));
    }
}

