package br.edu.utfpr.pb.ecommerce.server_ecommerce.security.config;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.JwtProperties;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.filter.JWTAuthenticationFilter;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.filter.JWTAuthorizationFilter;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.security.handler.CustomAuthenticationFailureHandler;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class WebSecurity {
    // Service responsável por buscar um usuário no banco de dados por meio do método loadByUsername()
    private final AuthService authService;
    // Objeto responsável por realizar o tratamento de exceção quando o usuário informar credenciais incorretas ao autenticar-se.
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final ObjectMapper objectMapper;
    private final JwtProperties jwtProperties;
    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    public WebSecurity(AuthService authService, AuthenticationEntryPoint authenticationEntryPoint, CustomAuthenticationFailureHandler customAuthenticationFailureHandler, ObjectMapper objectMapper, JwtProperties jwtProperties, Environment env, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.objectMapper = objectMapper;
        this.jwtProperties = jwtProperties;
        this.env = env;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(authService)
                .passwordEncoder(passwordEncoder);

        // authenticationManager -> responsável por gerenciar a autenticação dos usuários
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        boolean isDev = Arrays.asList(env.getActiveProfiles()).contains("dev");

        if (isDev) {
            http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        }
        System.out.println("isDev: " + isDev + "\nActive Profiles: " + Arrays.toString(env.getActiveProfiles()) + "\n");

        // desabilita o uso de csrf
        http.csrf(AbstractHttpConfigurer::disable);

        // Adiciona configuração de CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        //define o objeto responsável pelo tratamento de exceção ao entrar com credenciais inválidas
        http.exceptionHandling(exceptionHandling ->
                exceptionHandling.authenticationEntryPoint(authenticationEntryPoint));

        // configura a authorização das requisições
        http.authorizeHttpRequests((authorize) -> {
            if (isDev) {
                authorize.requestMatchers("/h2-console/**").permitAll();
            }

            authorize
                    // ROTAS PÚBLICAS (permitAll)
                    .requestMatchers(HttpMethod.POST, "/users", "/shipment/products").permitAll()
                    .requestMatchers(HttpMethod.GET, "/products/**", "/categories/**", "/payments/**", "/cep/validate/", "/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .requestMatchers("/error/**").permitAll()

                    // ROTAS DE ADMIN
                    // Apenas ADMIN pode gerenciar (criar, editar, deletar) produtos, categorias e pagamentos
                    .requestMatchers("/products/**", "/categories/**", "/payments/**").hasAnyAuthority("ADMIN");

                    // ROTAS AUTENTICADAS (USER ou ADMIN)
                    // Qualquer usuário autenticado pode acessar as demais rotas
                    // A lógica de negócio nos services já garante que um usuário só acesse seus próprios dados (pedidos, endereços, etc.)
            authorize.anyRequest().authenticated();
            }
        );
        http.authenticationManager(authenticationManager)
                //Filtro da Autenticação - sobrescreve o método padrão do Spring Security para Autenticação.
                .addFilter(new JWTAuthenticationFilter(authenticationManager, authService, objectMapper, jwtProperties, customAuthenticationFailureHandler))
                //Filtro da Autorização - - sobrescreve o método padrão do Spring Security para Autorização.
                .addFilter(new JWTAuthorizationFilter(authenticationManager, jwtProperties))
                //Como será criada uma API REST e todas as requisições que necessitam de autenticação/autorização serão realizadas com o envio do token JWT do usuário, não será necessário fazer controle de sessão no *back-end*.
                .sessionManagement(
                        sessionManagement -> 
                                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    /*
    O compartilhamento de recursos de origem cruzada (CORS) é um mecanismo para integração de aplicativos. O CORS define uma maneira de os aplicativos Web clientes carregados em um domínio interagirem com recursos em um domínio diferente. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Lista das origens autorizadas, no nosso caso que iremos rodar a aplicação localmente o * poderia ser trocado
        // por: http://localhost:porta, em que :porta será a porta em que a aplicação cliente será executada
        configuration.setAllowedOrigins(List.of("*"));

        // Lista dos métodos HTTP autorizados
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Lista dos Headers autorizados, o Authorization será o header que iremos utilizar para transferir o Token
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin"));

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}