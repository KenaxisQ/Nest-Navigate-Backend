package com.arista.nestnavigator.user.config;

import com.arista.nestnavigator.user.entity.Token;
import com.arista.nestnavigator.user.repository.TokenRepository;
import com.arista.nestnavigator.user.repository.UserRepository;
import com.arista.nestnavigator.user.service.JwtService;
import com.arista.nestnavigator.user.utils.ApiException;
import com.arista.nestnavigator.user.utils.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.http.HttpStatus;

@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public CustomLogoutHandler(TokenRepository tokenRepository, JwtService jwtService, UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ApiException(ErrorCodes.EMPTY_TOKEN);
            }

            String token = authHeader.substring(7);
            Token storedToken = tokenRepository.findByAccessToken(token).orElse(null);

            if (storedToken == null || !jwtService.isValid(token, userRepository.findByUsername(jwtService.extractUsername(token)))) {
                throw new ApiException(ErrorCodes.INVALID_TOKEN);
            }

            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);

    }

    public void logoutUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
        try{logout(request, response, null);}
        catch (ApiException knownEx){
            throw knownEx;
        }
        catch (Exception unknownEx){
            throw new ApiException("INVALID_TOKEN",unknownEx.getMessage(),HttpStatus.UNAUTHORIZED);
        }

    }
}