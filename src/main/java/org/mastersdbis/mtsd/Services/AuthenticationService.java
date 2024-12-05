package org.mastersdbis.mtsd.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.BeanDefinitionDsl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;

    /*
    public AuthenticationResponse register(RegisterRequest request) {
        userService.findByEmail(request.getEmail().ifPresent(user -> {throw new UserAlreadyExistsException();}));
        var user = AppUser.builder().firstname(request.getFirstname()).lastname(request.getLastname()).email((request.getEmail())).password(passwordEncoder.encode(request.getPassword())).role(BeanDefinitionDsl.Role.valueOf(request.getType())).build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).email(user.getEmail()).type(user.getRole()).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow(InvalidEmailException::new);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).email(user.getEmail()).type(user.getRole()).build();
    }
     */
}
