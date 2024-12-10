package com.ticky.backend.auth;

import com.ticky.backend.config.CustomUserDetails;
import com.ticky.backend.config.JwtService;
import com.ticky.backend.entity.Token;
import com.ticky.backend.entity.User;
import com.ticky.backend.enums.TokenType;
import com.ticky.backend.exception.UserAlreadyExistsException;
import com.ticky.backend.repository.TokenRepository;
import com.ticky.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;

    public User registerCurrentUser(RegisterRequest registerRequest) {
        boolean isUserExists = userRepository.findByEmail(registerRequest.getEmail()).isPresent();

        if (isUserExists) {
            throw new UserAlreadyExistsException("User already exists");
        }

        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .userRole(registerRequest.getRole())
                .contactNumber(registerRequest.getContactNumber())
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    public LoginResponse logInCurrentUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        var user = userRepository.findByEmail(loginRequest.getUsername());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Incorrect username or password");
        }

        User foundUser = user.get();
        String jwtToken = jwtService.generateToken(new CustomUserDetails(foundUser));

        var token = Token.builder()
                .token(jwtToken)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .user(foundUser)
                .build();

        revokeAllUserTokens(foundUser);
        tokenRepository.save(token);
        return new LoginResponse(jwtToken, "Login successful");
    }

    public void revokeAllUserTokens(User user) {
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (tokens.isEmpty()) {
            return;
        }

        tokens.forEach(token -> {
            token.setRevoked(true);
            token.setExpired(true);
        });

        tokenRepository.saveAll(tokens);
    }
}
