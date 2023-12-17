package com.example.apigatewayservice.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {
    /*
    * - 사전 작업에 필요한 필터
    * - 사후 작업에 필요한 필터
    * : 필터의 목적에 따라 설정하면 된다.
    * */

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        // Custom Pre Filter
        return (exchange, chain) -> {
            // 요청 및 응답 객체에 접근
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter: request id -> {}", request.getId());

            // Custom Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                // Mono : 비동기 방식에서 단일 값을 전달할 때 사용
                // 비동기 처리를 위해 Mono 사용, 응답 코드 로깅
                log.info("Custom PRE filter: response code -> {}", response.getStatusCode());
            }));
        };
    }

    public static class Config {
        // Put the configuration properties
    }
}

/*
 * 이 클래스는 Spring Cloud Gateway용 사용자 정의 필터를 나타냅니다.
 * 게이트웨이에서 필터는 HTTP 요청과 응답이 게이트웨이를 통과하는 동안 조작하고 처리하는 데 필수적입니다.
 * 필터는 보안 강화, 요청/응답 내용 수정 및 여러 전/후 처리 작업에서 중요한 역할을 합니다.
 *
 * 필터의 목적:
 * - Pre-operation 필터: 요청이 목적지에 도달하기 전에 적용되는 필터입니다.
 *   일반적인 사용 사례로는 인증, 로깅 및 요청 변환이 있습니다.
 *
 * - Post-production 필터: 응답이 목적지에서 수신된 후에 적용되는 필터입니다.
 *   로깅 응답 세부 정보, 응답 수정 또는 오류 처리와 같은 작업에 유용합니다.
 *
 * 게이트웨이의 목적에 따라 필터를 설정하는 것이 중요합니다.
 * - 인증 및 권한 부여: 요청이 인증된 및 권한이 부여된 사용자에게서 온 것임을 확인합니다.
 * - 로깅: 모니터링 및 디버깅 목적으로 관련 정보를 캡처합니다.
 * - 요청/응답 수정: 요청 및 응답의 내용이나 구조를 조정합니다.
 * - 오류 처리: 오류를 정상적으로 처리하고 의미 있는 응답을 제공합니다.
 *
 * CustomFilter와 같은 필터를 구현하고 구성함으로써 게이트웨이는 기본 마이크로서비스 아키텍처의 특정 요구 사항을 충족시키도록 사용자 정의될 수 있습니다.
 * */
