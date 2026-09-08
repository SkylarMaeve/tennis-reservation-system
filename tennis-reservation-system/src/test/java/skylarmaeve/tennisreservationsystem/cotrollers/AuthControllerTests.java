package skylarmaeve.tennisreservationsystem.cotrollers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;
import skylarmaeve.tennisreservationsystem.controller.AuthController;
import skylarmaeve.tennisreservationsystem.service.JwtService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTests {

    private static final String TEST_SECRET = "84da17da1a2a5c89ae6139702f16b5acf02d521a1559ee652df0437b1ee5633a!";
    private static final long TEST_EXPIRATION = 3600000;
    @Mock
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    private AuthController authController;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();

        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", TEST_EXPIRATION);

        authController = new AuthController(authenticationManager, jwtService, userDetailsService);
    }

    private String createBasicAuthHeader(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }


    @Test
    void correctLoginTest() {
        String username = "user";
        String password = "user";

        UserDetails userDetails = new User(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

        String header = createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        String authHeader = response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        assertNotNull(authHeader);
        assertTrue(authHeader.startsWith("Bearer "));

        String token = authHeader.substring(7);

        assertEquals(3, token.split("\\.").length);
        assertEquals(username, jwtService.extractUsername(token));
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService).loadUserByUsername(username);
    }

    @Test
    void errorHeaderLoginTest() {
        String username = "user";
        String password = "user";

        String header = "Error" + createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void invalidLoginTest() {
        String username = "user";
        String password = "user";

        UserDetails userDetails = new User(
                username,
                password,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        when(userDetailsService.loadUserByUsername(username)).thenThrow(new UsernameNotFoundException("Error"));

        String header = createBasicAuthHeader(username, password);
        ResponseEntity<Void> response = authController.login(header);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void loginWrongTokenTest() {
        ResponseEntity<Void> response = authController.login("Basic @@@not-base-64@@@");

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verifyNoInteractions(authenticationManager, userDetailsService);
    }
}
