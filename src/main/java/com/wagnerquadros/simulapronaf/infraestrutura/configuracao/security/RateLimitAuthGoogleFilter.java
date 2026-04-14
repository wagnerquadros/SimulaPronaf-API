package com.wagnerquadros.simulapronaf.infraestrutura.configuracao.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitAuthGoogleFilter extends OncePerRequestFilter {

    private static final int LIMITE_REQUISICOES = 5;
    private static final long JANELA_EM_MILLIS = 60_000;

    private final Map<String, ControleRequisicao> requisicoesPorIp = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!deveAplicarRateLimit(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = extrairIp(request);
        long agora = Instant.now().toEpochMilli();

        ControleRequisicao controle = requisicoesPorIp.compute(ip, (chave, valorAtual) -> {
            if (valorAtual == null || agora - valorAtual.inicioJanela() > JANELA_EM_MILLIS) {
                return new ControleRequisicao(1, agora);
            }

            return new ControleRequisicao(valorAtual.quantidade() + 1, valorAtual.inicioJanela());
        });

        if (controle.quantidade() > LIMITE_REQUISICOES) {
            responderLimiteExcedido(response, request);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean deveAplicarRateLimit(HttpServletRequest request) {
        return HttpMethod.POST.matches(request.getMethod())
                && "/auth/google".equals(request.getRequestURI());
    }

    private String extrairIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private void responderLimiteExcedido(HttpServletResponse response, HttpServletRequest request) throws IOException {
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("timestamp", java.time.OffsetDateTime.now().toString());
        resposta.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
        resposta.put("erro", "Muitas requisições");
        resposta.put("mensagem", "Limite de tentativas excedido. Tente novamente em instantes.");
        resposta.put("caminho", request.getRequestURI());

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(response.getOutputStream(), resposta);
    }

    private record ControleRequisicao(int quantidade, long inicioJanela) {
    }
}