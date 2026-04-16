package com.librarymanagement.security;

import com.librarymanagement.entity.Roles;
import com.librarymanagement.entity.Users;
import com.librarymanagement.repository.RolesRepository;
import com.librarymanagement.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class CustomDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RolesRepository rolesRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Roles role = rolesRepository.findById(user.getRoleId())
                .orElseThrow(() -> new UsernameNotFoundException("Role not found"));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());

        return new User(
                user.getUserName(),
                user.getPassword(),
                Set.of(authority)
        );

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
