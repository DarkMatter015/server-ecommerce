package br.edu.utfpr.pb.ecommerce.server_ecommerce.interceptor;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.FilterManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class ActiveFilterInterceptor implements HandlerInterceptor {

    private final FilterManager filterManager;

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request,@Nullable HttpServletResponse response,@Nullable Object handler) throws Exception {
        // Ativa o filtro por padrão para buscar apenas registros ativos
        filterManager.enableActiveFilter(true);
        return true;
    }
}
