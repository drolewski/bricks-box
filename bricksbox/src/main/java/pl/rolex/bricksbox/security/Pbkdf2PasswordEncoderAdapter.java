package pl.rolex.bricksbox.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Pbkdf2PasswordEncoderAdapter {

    @Value("${pbkdf2.password.encoder.secret}")
    private String secret;

    @Value("${pbkdf2.password.encoder.iteration}")
    private Integer iteration;

    @Value("${pbkdf2.password.encoder.hashWidth}")
    private Integer hashWidth;

    private Pbkdf2PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        this.encoder = new Pbkdf2PasswordEncoder(secret, iteration, hashWidth);
        this.encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA512);
    }

    public String encode(final CharSequence rawPassword) {
        return this.encoder.encode(rawPassword);
    }

    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        return this.encoder.matches(rawPassword, encodedPassword);
    }
}
