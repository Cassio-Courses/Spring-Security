package br.com.cassio.springauth.security;

import static br.com.cassio.springauth.security.enumeration.UserRole.ADMIN;
import static br.com.cassio.springauth.security.enumeration.UserRole.ADMIN_TRAINEE;
import static br.com.cassio.springauth.security.enumeration.UserRole.STUDENT;
import static java.lang.Boolean.TRUE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import br.com.cassio.springauth.security.enumeration.UserPermission;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String PASSWORD = "password";
	private static final String API_V1 = "/api/v1/**";
	private static final String ADMIN_API_V1 = "/admin/api/v1/**";
	private final PasswordEncoder passwordEncoder;

	@Autowired
	public WebSecurityConfig(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		configurePermissions(http);
		configureCors(http);
		configureCsrf(http, false);
//		setBasicAuthentication(http);
		setFormBasedAuthentication(http);

	}

	private void setFormBasedAuthentication(HttpSecurity http) throws Exception {
		http.formLogin();
	}

	@SuppressWarnings("unused")
	private void setBasicAuthentication(HttpSecurity http) throws Exception {
		http.httpBasic();
	}

	protected void configureCsrf(HttpSecurity http, Boolean activated) throws Exception {
		if (TRUE.equals(activated)) {
			http
					.csrf()
					.csrfTokenRepository(new CookieCsrfTokenRepository());
		} else {
			http.csrf().disable();
		}

	}

	protected void configureCors(HttpSecurity http) throws Exception {
		http.cors();
	}

	private void configurePermissions(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/", "index", "/css/*", "/js/*").permitAll()
				.antMatchers(API_V1)
				.hasRole(STUDENT.getRole())
				.antMatchers(HttpMethod.DELETE, ADMIN_API_V1)
				.hasAuthority(UserPermission.COURSE_WRITE.getPermission())
				.antMatchers(HttpMethod.POST, ADMIN_API_V1)
				.hasAuthority(UserPermission.COURSE_WRITE.getPermission())
				.antMatchers(HttpMethod.PUT, ADMIN_API_V1)
				.hasAuthority(UserPermission.COURSE_WRITE.getPermission())
				.antMatchers(HttpMethod.GET, ADMIN_API_V1).hasAnyRole(ADMIN.getRole(), ADMIN_TRAINEE.getRole())
				.anyRequest()
				.authenticated();
	}

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		UserDetails cassio = User.builder()
				.username("Cassio")
				.password(passwordEncoder.encode(PASSWORD))
//				.roles(STUDENT.getRole())
				.authorities(STUDENT.getGrantedauthorities())
				.build();

		UserDetails admin = User.builder()
				.username("Admin")
				.password(passwordEncoder.encode(PASSWORD))
//				.roles(ADMIN.getRole())
				.authorities(ADMIN.getGrantedauthorities())
				.build();

		UserDetails tom = User.builder()
				.username("Tom")
				.password(passwordEncoder.encode(PASSWORD))
//				.roles(ADMIN_TRAINEE.getRole())
				.authorities(ADMIN_TRAINEE.getGrantedauthorities())
				.build();

		return new InMemoryUserDetailsManager(cassio, admin, tom);

	}

}
