package br.dev.mag.drackymapp.domain.model;

import br.dev.mag.drackymapp.domain.enums.AuthProvider;
import br.dev.mag.drackymapp.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    @Column(name = "provider_id")
    private String providerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;


    public static User createLocalUser(String email, String encryptedPassword, Role role) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email é obrigatório.");
        if (encryptedPassword == null || encryptedPassword.isBlank()) throw new IllegalArgumentException("Senha é obrigatória.");

        User user = new User();
        user.email = email;
        user.password = encryptedPassword;
        user.provider = AuthProvider.LOCAL;
        user.role = role != null ? role : Role.USER;
        return user;
    }

    public static User createSocialUser(String email, AuthProvider provider, String providerId) {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("Email é obrigatório.");
        if (provider == AuthProvider.LOCAL) throw new IllegalArgumentException("Use createLocalUser() para autenticação local.");

        User user = new User();
        user.email = email;
        user.provider = provider;
        user.providerId = providerId;
        user.role = Role.USER;
        return user;
    }


}
