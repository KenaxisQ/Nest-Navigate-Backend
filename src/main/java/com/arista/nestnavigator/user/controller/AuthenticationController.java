package com.arista.nestnavigator.user.controller;

import com.arista.nestnavigator.user.config.CustomLogoutHandler;
import com.arista.nestnavigator.user.entity.AuthenticationResponse;
import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.user.repository.TokenRepository;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.user.service.AuthenticationService;
import com.arista.nestnavigator.user.service.JwtService;
import com.arista.nestnavigator.user.utils.ApiException;

import com.arista.nestnavigator.user.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AuthenticationController {

    private final AuthenticationService authService;
    private final CustomLogoutHandler customLogoutHandler;
    public AuthenticationController(AuthenticationService authService,
                                    JwtService jwtService,
                                    UserRepository userRepository,
                                    TokenRepository tokenRepository,
                                    CustomLogoutHandler customLogoutHandler) {
        this.authService = authService;
        this.customLogoutHandler = customLogoutHandler;
    }


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
                @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody User request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }

    @PostMapping("/sign_out")
    public ResponseEntity logout(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try{
            customLogoutHandler.logoutUser(request, response);
            return ResponseEntity.ok(ResponseBuilder.success("Logout successful!"));
        }
        catch (ApiException knownEx){
            throw knownEx;
        }
        catch (Exception unknownEx){
            throw new ApiException("INVALID_TOKEN",unknownEx.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
