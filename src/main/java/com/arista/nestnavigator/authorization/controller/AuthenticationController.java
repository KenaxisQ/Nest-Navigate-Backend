package com.arista.nestnavigator.authorization.controller;

import com.arista.nestnavigator.authorization.config.CustomLogoutHandler;
import com.arista.nestnavigator.authorization.Entity.AuthenticationResponse;
import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.authorization.repository.TokenRepository;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.authorization.service.AuthenticationService;
import com.arista.nestnavigator.authorization.service.JwtService;
import com.arista.nestnavigator.custom_exceptions.ApiException;

import com.arista.nestnavigator.user.utils.ApiResponse;
import com.arista.nestnavigator.user.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public AuthenticationController(AuthenticationService authService,
                                    CustomLogoutHandler customLogoutHandler) {
        this.authService = authService;
        this.customLogoutHandler = customLogoutHandler;
    }
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(
                @RequestBody User request
    ) {
        AuthenticationResponse response = authService.register(request);
        return ResponseEntity.ok(ResponseBuilder.success(response));
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
    public ResponseEntity<ApiResponse<String>> logout(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try{
            customLogoutHandler.logoutUser(request, response);
            return ResponseEntity.ok(ResponseBuilder.success("Logout successful!!"));
        }
        catch (ApiException knownEx){
            throw knownEx;
        }
        catch (Exception unknownEx){
            throw new ApiException("INVALID_TOKEN",unknownEx.getMessage(),HttpStatus.UNAUTHORIZED);
        }
    }
}
