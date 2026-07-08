package br.dev.mag.drackymapp.controllers;

import br.dev.mag.drackymapp.application.services.AuthenticationService;
import br.dev.mag.drackymapp.controllers.dto.LoginRequest;
import br.dev.mag.drackymapp.controllers.dto.LoginResponse;
import br.dev.mag.drackymapp.controllers.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterRequest request) {
        authenticationService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return authenticationService.login(request);
    }

}
