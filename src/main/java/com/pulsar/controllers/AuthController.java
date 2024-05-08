package com.pulsar.controllers;

import com.pulsar.models.*;
import com.pulsar.payload.request.LoginRequest;
import com.pulsar.payload.request.PreLoginRequest;
import com.pulsar.payload.request.SignupRequest;
import com.pulsar.payload.response.JwtResponse;
import com.pulsar.payload.response.MessageResponse;
import com.pulsar.payload.response.RegistrationResponse;
import com.pulsar.repository.UserRepository;
import com.pulsar.repository.VerificationRepository;
import com.pulsar.security.jwt.JwtUtils;
import com.pulsar.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private VerificationRepository verificationRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/code")
    public ResponseEntity<?> code(@Valid @RequestBody PreLoginRequest preLoginRequest) {

        Optional<User> userOptional = userRepository.findByUsername(preLoginRequest.getUsername());
        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid request", 5));
        }

        User user = userOptional.get();
        user.setPassword(encoder.encode("0000"));
        userRepository.save(user);
        //TODO send the code through the SMS or EMAIL
        UserDetailsImpl impl = UserDetailsImpl.build(userOptional.get());
        String jwt = jwtUtils.generateJwtToken(new PreAuthenticatedAuthenticationToken(impl, null));
        return ResponseEntity.ok(new JwtResponse(jwt,
                impl.getId(),
                impl.getUsername(),
                impl.getEmail(),
                null));
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!", 5));
        }

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already in use!", 5));
        }

        if (!isValidateUserType(signUpRequest)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Invalid Request", 5));
        }

        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getMobileNumber());

        user.getRoles().add(signUpRequest.getUserType() == UserType.PARENT.getType() ? UserRole.ROLE_PARENT : UserRole.ROLE_LSA);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 12);
        //TODO generate code and share with user for further verification
        Verification verification = new Verification(VerificationType.EMAIL, 0000, cal.getTime());
        verificationRepository.save(verification);
        user.getVerification().add(verification);

        user.setPassword("0000");
        userRepository.save(user);
        UserDetailsImpl impl = UserDetailsImpl.build(user);
        String jwt = jwtUtils.generateJwtToken(new PreAuthenticatedAuthenticationToken(impl, null));

        //TODO verification code
        return ResponseEntity.ok(new RegistrationResponse(
                new MessageResponse("User registered successfully! but verification required", -1),
                new JwtResponse(jwt,
                        impl.getId(),
                        impl.getUsername(),
                        impl.getEmail(),
                        null)));
    }

    private boolean isValidateUserType(SignupRequest signUpRequest) {
        return signUpRequest.getUserType() != UserType.COUNCILLOR.getType()
                && signUpRequest.getUserType() != UserType.ADMIN.getType()
                && UserType.getUserType(signUpRequest.getUserType()) != null;
    }
}
