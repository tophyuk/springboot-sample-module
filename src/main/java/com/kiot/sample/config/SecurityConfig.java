package com.kiot.sample.config;


import com.kiot.sample.handler.auth.OAuth2FailureHandler;
import com.kiot.sample.handler.auth.OAuth2SuccessHandler;
import com.kiot.sample.handler.jwt.JwtAccessDeniedHandler;
import com.kiot.sample.handler.jwt.JwtAuthenticationEntryPoint;
import com.kiot.sample.jwt.JwtFilter;
import com.kiot.sample.jwt.JwtTokenProvider;
import com.kiot.sample.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .addFilter(corsFilter)
                .formLogin().disable()
                .authorizeHttpRequests((request) -> request
                        .requestMatchers( "/user", "/auth/**", "/board/**", "/login", "/main").permitAll()
                        .anyRequest().authenticated()
                )


                /** 401, 403 Exception Handler **/
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                /**세션 사용하지 않음*/
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)



                /**JwtSecurityConfig 적용 */
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

                .oauth2Login()  // OAuth2 설정 시작
                .loginPage("/login") // 로그인 페이지
                .defaultSuccessUrl("/main")
                .successHandler(oAuth2SuccessHandler)
                .failureHandler(oAuth2FailureHandler)
                .userInfoEndpoint().userService(customOAuth2UserService); // customOAuthUserService 에서 처리

        http.addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
