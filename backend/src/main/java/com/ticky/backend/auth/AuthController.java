package com.ticky.backend.auth;

import com.ticky.backend.dto.response.BaseResponse;
import com.ticky.backend.dto.response.Response;
import com.ticky.backend.entity.User;
import com.ticky.backend.exception.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> createNewUser(@RequestBody RegisterRequest registerRequest) {
        try {
            User createdUser = authService.registerCurrentUser(registerRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to create a new user")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response> logInCurrentUser(
            @RequestBody LoginRequest loginRequest
    ) {
        try {
            LoginResponse loginResponse = authService.logInCurrentUser(loginRequest);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Incorrect username or password")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            var errorResponse = BaseResponse.builder()
                    .isSuccess(false)
                    .message("Failed to login user")
                    .build();
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
