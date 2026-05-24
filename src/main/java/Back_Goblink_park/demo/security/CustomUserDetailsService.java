package Back_Goblink_park.demo.security;

import Back_Goblink_park.demo.entity.Usuario;
import Back_Goblink_park.demo.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.*;

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

        Usuario usuario = usuarioRepository
                .findByCorreo(correo)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado"
                        )
                );

        return new User(
                usuario.getCorreo(),
                usuario.getPasswordHash(),
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_" +
                                        usuario.getRol().getNombre()
                        )
                )
        );
    }
}