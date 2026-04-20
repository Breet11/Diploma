package com.example.diploma.unit;

import com.example.diploma.security.jwt.JwtService;
import com.example.diploma.security.rsa.RsaKeyProvider;
import com.example.diploma.user.dto.CreateUserRequestDto;
import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import com.example.diploma.user.model.Role;
import com.example.diploma.user.model.User;
import com.example.diploma.user.repository.UserRepository;
import com.example.diploma.user.service.AuthServiceCustom;
import com.example.diploma.user.service.CreateUserServiceCustom;
import com.example.diploma.user.service.CustomUserDetailsService;
import com.example.diploma.user.service.DecryptAuthPasswordServiceCustom;
import com.example.diploma.user.service.GetProfileServiceCustom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceCustomTest {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private com.example.diploma.user.service.DecryptAuthPasswordService decryptAuthPasswordService;
    @InjectMocks
    private AuthServiceCustom authService;

    @Test
    void loginShouldAuthenticateAndReturnJwtResponse() {
        LoginRequestDto request = new LoginRequestDto("john", "encrypted");
        User user = new User(UUID.randomUUID(), "john@example.com", "john", "hashed", Role.ADMIN);
        when(decryptAuthPasswordService.decrypt("encrypted")).thenReturn("plainPassword");
        when(userRepository.findByLogin("john")).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.getJwtExpirationMs()).thenReturn(604800000L);

        var response = authService.login(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken> tokenCaptor = ArgumentCaptor.forClass(UsernamePasswordAuthenticationToken.class);
        verify(authenticationManager).authenticate(tokenCaptor.capture());
        assertEquals("john", tokenCaptor.getValue().getPrincipal());
        assertEquals("plainPassword", tokenCaptor.getValue().getCredentials());
        assertEquals("jwt-token", response.accessToken());
        assertEquals("Bearer", response.tokenType());
        assertEquals(604800000L, response.expiresIn());
        assertEquals("ADMIN", response.role());
    }

    @Test
    void loginShouldThrowWhenUserIsMissingAfterAuthentication() {
        when(decryptAuthPasswordService.decrypt("encrypted")).thenReturn("plainPassword");
        when(userRepository.findByLogin("john")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.login(new LoginRequestDto("john", "encrypted"))
        );

        assertEquals("Invalid login or password", exception.getMessage());
    }

    @Test
    void registerShouldCreateRegularUserWithEncodedDecryptedPassword() {
        RegisterRequestDto request = new RegisterRequestDto(
                "user@example.com",
                "user",
                "John",
                "Doe",
                "+10000000001",
                "encrypted",
                "encrypted-confirm"
        );
        when(userRepository.findByLogin("user")).thenReturn(Optional.empty());
        when(decryptAuthPasswordService.decrypt("encrypted")).thenReturn("plainPassword");
        when(decryptAuthPasswordService.decrypt("encrypted-confirm")).thenReturn("plainPassword");
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");

        var response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User saved = userCaptor.getValue();
        assertEquals("user@example.com", saved.getEmail());
        assertEquals("user", saved.getLogin());
        assertEquals("John", saved.getFirstName());
        assertEquals("Doe", saved.getLastName());
        assertEquals("+10000000001", saved.getPhone());
        assertEquals("encodedPassword", saved.getHashedPassword());
        assertEquals(Role.USER, saved.getRole());
        assertEquals("User registered successfully", response.message());
    }

    @Test
    void registerShouldFailWhenLoginAlreadyExists() {
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(new RegisterRequestDto(
                        "user@example.com",
                        "user",
                        "John",
                        "Doe",
                        "+10000000001",
                        "encrypted",
                        "encrypted-confirm"
                ))
        );

        assertEquals("User with login user already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void registerShouldFailWhenPasswordsDoNotMatch() {
        RegisterRequestDto request = new RegisterRequestDto(
                "user@example.com",
                "user",
                "John",
                "Doe",
                "+10000000001",
                "encrypted",
                "encrypted-confirm"
        );
        when(userRepository.findByLogin("user")).thenReturn(Optional.empty());
        when(decryptAuthPasswordService.decrypt("encrypted")).thenReturn("plainPassword");
        when(decryptAuthPasswordService.decrypt("encrypted-confirm")).thenReturn("differentPassword");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> authService.register(request)
        );

        assertEquals("Password and confirmation password do not match", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void shouldLoadUserDetailsWithRoleAuthority() {
        User user = new User(UUID.randomUUID(), "admin@example.com", "admin", "hashed", Role.ADMIN);
        when(userRepository.findByLogin("admin")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("admin");

        assertEquals("admin", result.getUsername());
        assertEquals("hashed", result.getPassword());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> "ROLE_ADMIN".equals(a.getAuthority())));
    }

    @Test
    void shouldThrowWhenUserIsNotFound() {
        when(userRepository.findByLogin("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername("missing"));
    }
}

@ExtendWith(MockitoExtension.class)
class CreateUserServiceCustomTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CreateUserServiceCustom createUserService;

    @Test
    void shouldCreateUserWithDefaultRole() {
        CreateUserRequestDto request = new CreateUserRequestDto("user@example.com", "user", "John", "Doe", "+10000000001", "password", null);
        when(userRepository.findByLogin("user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        createUserService.createUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(Role.USER, userCaptor.getValue().getRole());
        assertEquals("encodedPassword", userCaptor.getValue().getHashedPassword());
    }

    @Test
    void shouldCreateUserWithExplicitRole() {
        CreateUserRequestDto request = new CreateUserRequestDto("admin@example.com", "admin", "John", "Doe", "+10000000001", "password", Role.ADMIN);
        when(userRepository.findByLogin("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        createUserService.createUser(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals(Role.ADMIN, userCaptor.getValue().getRole());
    }

    @Test
    void shouldThrowWhenCreatingDuplicateUser() {
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createUserService.createUser(new CreateUserRequestDto("user@example.com", "user", "John", "Doe", "+10000000001", "password", null))
        );

        assertEquals("User with login user already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}

@ExtendWith(MockitoExtension.class)
class GetProfileServiceCustomTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private GetProfileServiceCustom getProfileService;

    @Test
    void shouldReturnProfileDto() {
        User user = new User(UUID.randomUUID(), "user@example.com", "user", "hashed", Role.USER);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPhone("+10000000001");
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));

        var response = getProfileService.getProfile("user");

        assertEquals("user@example.com", response.email());
        assertEquals("user", response.login());
        assertEquals("John", response.firstName());
        assertEquals("Doe", response.lastName());
        assertEquals("+10000000001", response.phone());
        assertEquals("USER", response.role());
    }

    @Test
    void shouldThrowWhenProfileUserMissing() {
        when(userRepository.findByLogin("ghost")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> getProfileService.getProfile("ghost"));

        assertEquals("User with login ghost not found", exception.getMessage());
    }
}

@ExtendWith(MockitoExtension.class)
class DecryptAuthPasswordServiceCustomTest {
    @Mock
    private RsaKeyProvider rsaKeyProvider;
    @InjectMocks
    private DecryptAuthPasswordServiceCustom decryptAuthPasswordService;

    @Test
    void shouldDecryptEncryptedPassword() throws Exception {
        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        when(rsaKeyProvider.getPrivateKey()).thenReturn(keyPair.getPrivate());
        String encryptedPassword = encryptWithPublicKey(keyPair, "testpassword");

        String decrypted = decryptAuthPasswordService.decrypt(encryptedPassword);

        assertEquals("testpassword", decrypted);
    }

    @Test
    void shouldThrowWhenCipherTextIsInvalid() {
        KeyPair keyPair;
        try {
            keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
        when(rsaKeyProvider.getPrivateKey()).thenReturn(keyPair.getPrivate());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> decryptAuthPasswordService.decrypt("not-valid-base64%%")
        );

        assertEquals("Password decryption failed", exception.getMessage());
    }

    private String encryptWithPublicKey(KeyPair keyPair, String value) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(
                Cipher.ENCRYPT_MODE,
                keyPair.getPublic(),
                new OAEPParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT)
        );
        byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
}

