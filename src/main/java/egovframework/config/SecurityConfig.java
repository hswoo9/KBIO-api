package egovframework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.config.Customizer;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // CORS 설정
            .cors(Customizer.withDefaults())
            // CSRF 보호
            .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .and()
            // 보안 헤더 설정
            .headers()
                // XSS 보호
                .xssProtection()
                    .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                .and()
                // Content Security Policy
                .contentSecurityPolicy(
                    "default-src 'self'; " +
                    "script-src 'self' 'unsafe-inline' 'unsafe-eval' https://www.google-analytics.com https://ssl.google-analytics.com; " +
                    "style-src 'self' 'unsafe-inline'; " +
                    "img-src 'self' data: https: https://www.google-analytics.com https://ssl.google-analytics.com; " +
                    "font-src 'self'; " +
                    "connect-src 'self' https://www.google-analytics.com https://ssl.google-analytics.com"
                )
                .and()
                // Referrer Policy
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                .and()
                // X-Frame-Options
                .frameOptions()
                    .deny()
                .and()
                // X-Content-Type-Options
                .contentTypeOptions()
                .and()
                // HTTP Strict Transport Security
                .httpStrictTransportSecurity()
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                .and()
            .and()
            // HTTP 기본 인증 비활성화
            .httpBasic().disable()
            // URL 기반 보안 설정
            .authorizeRequests()
                .antMatchers("/api/public/**").permitAll()
                .antMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("https://kbio-api.dev-jitsu.com"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public HttpFirewall strictHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // URL에서 세미콜론 허용하지 않음
        firewall.setAllowSemicolon(false);
        // URL 인코딩된 슬래시 허용하지 않음
        firewall.setAllowUrlEncodedSlash(false);
        // 백슬래시 허용하지 않음
        firewall.setAllowBackSlash(false);
        // URL 인코딩된 마침표 허용하지 않음
        firewall.setAllowUrlEncodedPeriod(false);
        // 중복 슬래시 허용하지 않음
        firewall.setAllowUrlEncodedPercent(false);
        return firewall;
    }
}
