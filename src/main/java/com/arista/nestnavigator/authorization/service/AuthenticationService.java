package com.arista.nestnavigator.authorization.service;

import com.arista.nestnavigator.authorization.Entity.AuthenticationResponse;
import com.arista.nestnavigator.authorization.Entity.Token;
import com.arista.nestnavigator.user.entity.User;
import com.arista.nestnavigator.authorization.repository.TokenRepository;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.custom_exceptions.ApiException;
import com.arista.nestnavigator.custom_exceptions.ErrorCodes;
import com.arista.nestnavigator.user.utils.ResponseBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    public AuthenticationService(UserRepository repository,
                                 PasswordEncoder passwordEncoder,
                                 @Lazy JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(User user) {

        long startTime = System.currentTimeMillis();
        List<String> missingFields = new ArrayList<>();
        if (user.getFirstname() == null) missingFields.add("firstname");
        if (user.getLastname() == null) missingFields.add("lastname");
        if (user.getEmail() == null) missingFields.add("email");
        if (user.getPhone() == null) missingFields.add("phone");
        if (user.getUsername() == null) missingFields.add("username");
        if (user.getPassword() == null) missingFields.add("password");

        if (missingFields.size() == 6) {
            String missingFieldsMessage = "Please provide the necessary user information : " + String.join(", ", missingFields);
            throw new ApiException("USER_INFO_MISSING", missingFieldsMessage, HttpStatus.BAD_REQUEST);
        }

        if (!missingFields.isEmpty()) {
            String missingFieldsMessage = "Missing mandatory fields: " + String.join(", ", missingFields);
            throw new ApiException("MANDATORY_FIELD_MISSING", missingFieldsMessage, HttpStatus.BAD_REQUEST);
        }

        // Check for duplicate properties
        List<String> duplicateFields = new ArrayList<>();
        if (repository.getUserByUsername(user.getUsername()) != null) duplicateFields.add("username");
        if (repository.getUserByEmail(user.getEmail()) != null) duplicateFields.add("email");
        if (repository.getUserByPhone(user.getPhone()) != null) duplicateFields.add("phone");

        if (!duplicateFields.isEmpty()) {
            String duplicateFieldsMessage = "Duplicate properties: " + String.join(", ", duplicateFields);
            throw new ApiException("DUPLICATE_PROPERTIES", duplicateFieldsMessage, HttpStatus.CONFLICT);
        }

        try {
            if(user.getRole()!= null) user.setRole(user.getRole());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user = repository.save(user);
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            LocalDateTime tokenCreatedAt = LocalDateTime.now();
            saveUserToken(accessToken, refreshToken, user);
            return new AuthenticationResponse(accessToken, refreshToken,
                    String.format("User registration was successful, Token Expires at: %s, Refresh Token Expires at: %s",
                            tokenCreatedAt.plus(5, ChronoUnit.MINUTES),
                            tokenCreatedAt.plus(10, ChronoUnit.MINUTES)));
        } catch (Exception ex) {
            logger.error("Error While Creating User !!");
            throw new ApiException("UNKNOWN_EXCEPTION", ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    public AuthenticationResponse authenticate(User request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = repository.findByUsername(request.getUsername());
        if(user != null) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);
            LocalDateTime tokenCreatedAt = LocalDateTime.now();
            return new AuthenticationResponse(accessToken, refreshToken,
                    String.format("User registration was successful, Token Expires at: %s, Refresh Token Expires at: %s",
                            tokenCreatedAt.plus(5, ChronoUnit.MINUTES),
                            tokenCreatedAt.plus(10, ChronoUnit.MINUTES)));
        }
        else
            throw new ApiException(ErrorCodes.USER_NOT_FOUND);

    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }
    private void saveUserToken(String accessToken, String refreshToken, User user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    public ResponseEntity refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) {
        // extract the token from authorization header
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // extract username from token
        String username = jwtService.extractUsername(token);

        // check if the user exist in database
        User user = repository.findByUsername(username);
        if(user == null) throw new ApiException(ErrorCodes.USER_NOT_FOUND);

        // check if the token is valid
        if(jwtService.isValidRefreshToken(token, user)) {
            // generate access token
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);
            LocalDateTime tokenCreatedAt = LocalDateTime.now();
            return new ResponseEntity(new AuthenticationResponse(accessToken, refreshToken,
                    String.format("User registration was successful, Token Expires at: %s, Refresh Token Expires at: %s",
                            tokenCreatedAt.plus(5, ChronoUnit.MINUTES),
                            tokenCreatedAt.plus(10, ChronoUnit.MINUTES))), HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.UNAUTHORIZED);

    }
}
