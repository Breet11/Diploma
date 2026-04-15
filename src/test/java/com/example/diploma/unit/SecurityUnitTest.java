package com.example.diploma.unit;

import com.example.diploma.security.jwt.JwtAuthenticationFilter;
import com.example.diploma.security.jwt.JwtService;
import com.example.diploma.security.rsa.RsaKeyProvider;
import com.example.diploma.user.model.Role;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class JwtServiceTest {
	private static final String SECRET = "12345678901234567890123456789012";

	@Test
	void shouldGenerateTokenAndValidateIt() {
		JwtService jwtService = new JwtService(SECRET, 60_000L);
		com.example.diploma.user.model.User domainUser = new com.example.diploma.user.model.User(
				null,
				"admin@example.com",
				"admin",
				"hashed",
				Role.ADMIN
		);
		UserDetails userDetails = User.withUsername("admin").password("hashed").roles("ADMIN").build();

		String token = jwtService.generateToken(domainUser);

		assertEquals("admin", jwtService.extractUsername(token));
		assertTrue(jwtService.isTokenValid(token, userDetails));
	}

	@Test
	void shouldRejectExpiredToken() throws InterruptedException {
		JwtService jwtService = new JwtService(SECRET, 1L);
		com.example.diploma.user.model.User domainUser = new com.example.diploma.user.model.User(
				null,
				"user@example.com",
				"user",
				"hashed",
				Role.USER
		);
		UserDetails userDetails = User.withUsername("user").password("hashed").roles("USER").build();
		String token = jwtService.generateToken(domainUser);

		Thread.sleep(10L);

		assertFalse(jwtService.isTokenValid(token, userDetails));
	}

	@Test
	void shouldExposeConfiguredExpiration() {
		JwtService jwtService = new JwtService(SECRET, 12_345L);

		assertEquals(12_345L, jwtService.getJwtExpirationMs());
	}
}

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {
	@Mock
	private JwtService jwtService;
	@Mock
	private UserDetailsService userDetailsService;
	@Mock
	private FilterChain filterChain;
	@InjectMocks
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@AfterEach
	void clearSecurityContext() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void shouldSkipWhenAuthorizationHeaderIsMissing() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		verify(userDetailsService, never()).loadUserByUsername(anyString());
	}

	@Test
	void shouldSkipWhenTokenCannotBeParsed() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer broken-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(jwtService.extractUsername("broken-token")).thenThrow(new JwtException("bad token"));

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		verify(filterChain).doFilter(request, response);
		assertNull(SecurityContextHolder.getContext().getAuthentication());
		verify(userDetailsService, never()).loadUserByUsername(anyString());
	}

	@Test
	void shouldAuthenticateWhenTokenIsValid() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer valid-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		UserDetails userDetails = User.withUsername("john").password("hashed").roles("USER").build();
		when(jwtService.extractUsername("valid-token")).thenReturn("john");
		when(userDetailsService.loadUserByUsername("john")).thenReturn(userDetails);
		when(jwtService.isTokenValid("valid-token", userDetails)).thenReturn(true);

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
		assertEquals("john", SecurityContextHolder.getContext().getAuthentication().getName());
		verify(filterChain).doFilter(request, response);
	}

	@Test
	void shouldNotOverrideExistingAuthentication() throws Exception {
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken("existing", null, List.of())
		);
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer valid-token");
		MockHttpServletResponse response = new MockHttpServletResponse();
		when(jwtService.extractUsername("valid-token")).thenReturn("john");

		jwtAuthenticationFilter.doFilter(request, response, filterChain);

		var authentication = SecurityContextHolder.getContext().getAuthentication();
		assertNotNull(authentication);
		assertEquals("existing", authentication.getName());
		verify(userDetailsService, never()).loadUserByUsername(anyString());
	}
}

class RsaKeyProviderTest {
	@Test
	void shouldLoadPrivateKeyFromPem() throws Exception {
		KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
		String pem = "-----BEGIN PRIVATE KEY-----\\n"
				+ Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded())
				+ "\\n-----END PRIVATE KEY-----";
		RsaKeyProvider rsaKeyProvider = new RsaKeyProvider();
		ReflectionTestUtils.setField(rsaKeyProvider, "privateKeyPem", pem);

		rsaKeyProvider.initializePrivateKey();

		assertNotNull(rsaKeyProvider.getPrivateKey());
		assertEquals("RSA", rsaKeyProvider.getPrivateKey().getAlgorithm());
	}

	@Test
	void shouldFailWhenPrivateKeyPropertyContainsPlaceholder() {
		RsaKeyProvider rsaKeyProvider = new RsaKeyProvider();
		ReflectionTestUtils.setField(
				rsaKeyProvider,
				"privateKeyPem",
				"-----BEGIN PRIVATE KEY-----\\nPASTE_YOUR_PRIVATE_RSA_KEY_HERE\\n-----END PRIVATE KEY-----"
		);

		assertThrows(IllegalStateException.class, rsaKeyProvider::initializePrivateKey);
	}
}

