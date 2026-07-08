package br.dev.mag.drackymapp.application.services;

import br.dev.mag.drackymapp.controllers.dto.LoginRequest;
import br.dev.mag.drackymapp.controllers.dto.LoginResponse;
import br.dev.mag.drackymapp.controllers.dto.RegisterRequest;
import br.dev.mag.drackymapp.domain.enums.AuthProvider;
import br.dev.mag.drackymapp.domain.enums.Role;
import br.dev.mag.drackymapp.domain.model.User;
import br.dev.mag.drackymapp.infrastructure.security.CustomUserDetails;
import br.dev.mag.drackymapp.infrastructure.security.TokenService;
import br.dev.mag.drackymapp.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            TokenService tokenService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void register(RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado");
        }

        var encryptedPassword = passwordEncoder.encode(request.password());

        var user = User.createLocalUser(request.email(), encryptedPassword, Role.USER);

        userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {

        var user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BadCredentialsException("E-mail ou senha inválidos."));

        if (user.getProvider() != AuthProvider.LOCAL) {
            throw new BadCredentialsException("E-mail ou senha inválidos.");
        }

        var authenticationToken = new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        );

        var authentication = authenticationManager.authenticate(authenticationToken);
        var userDetails = (CustomUserDetails) authentication.getPrincipal();

        var token = tokenService.generateToken(userDetails.getUser());

        return new LoginResponse(token);

    }


}
