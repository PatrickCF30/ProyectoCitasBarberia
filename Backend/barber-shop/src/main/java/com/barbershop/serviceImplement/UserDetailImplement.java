package com.barbershop.serviceImplement;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.barbershop.model.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserDetailImplement implements UserDetails {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final User usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Adaptar el rol del usuario en GrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(usuario.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Método para obtener el objeto User completo
    public User getUsuario() {
        return this.usuario;
    }

    public String getUser() {
        return usuario.getUsername();
    }

    public String getEmail() {
        return usuario.getEmail();
    }

    // Obtener el id del usuario
    public Long getId() {
        return usuario.getId();
    }
}

