package Back_Goblink_park.demo.security;

import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(
            String correo
    ) throws UsernameNotFoundException {

        // =========================================
        // BUSCAR USUARIO POR CORREO
        // =========================================

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        // =========================================
        // AUTHORITY DEL ROL
        // =========================================

        String authority =
                usuario.getRol()
                        .getNombre()
                        .trim()
                        .toUpperCase();

        // =========================================
        // RETORNAR USER DETAILS
        // =========================================

        return new org.springframework.security.core.userdetails.User(

                usuario.getCorreo(),

                usuario.getPasswordHash(),

                usuario.getEstado(),

                true,

                true,

                true,

                List.of(
                        new SimpleGrantedAuthority(
                                authority
                        )
                )
        );
    }
}