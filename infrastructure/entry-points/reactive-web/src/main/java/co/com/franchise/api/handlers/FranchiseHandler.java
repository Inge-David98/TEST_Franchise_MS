package co.com.franchise.api.handlers;

import co.com.franchise.api.GenericHandler;
import co.com.franchise.api.dto.request.Franchise;
import co.com.franchise.api.dto.response.GenericResponse;
import co.com.franchise.api.mapper.MapperRequest;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@RequiredArgsConstructor
@Slf4j
public class FranchiseHandler extends GenericHandler {

    private static final String ID_FRANCHISE = "franchiseId";
    private static final String RQ = "RQ";
    private static final String RS = "RS";
    private static final String FALLBACK_METHOD_NAME = "fallback";
    
    private final FranchiseUseCase franchiseUseCase;

    @CircuitBreaker(name = "addFranchise", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> addFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Franchise.class)
                .flatMap(franchise -> franchiseUseCase.addFranchise(MapperRequest.MAPPER.requestToFranquicia(franchise))
                        .doOnSubscribe(subscription -> log.info(":: Request addFranchise", kv(RQ, franchise))))
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addFranchise", kv(RS, response));
                    return buildResponse(serverRequest, HttpStatus.OK, response);
                });
    }

    @CircuitBreaker(name = "updateNameFranchise", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> updateNameFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Franchise.class)
                .flatMap(franchise -> {
                    String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
                    return franchiseUseCase.updateNameFranchise(franchise.getFranchiseName(), franchiseId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameFranchise", kv(RQ, franchise, franchiseId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameFranchise", kv(RS, response));
                    return buildResponse(serverRequest, HttpStatus.OK, response);
                });
    }

    @CircuitBreaker(name = "getProductMostStock", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> getProductMostStock(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
            return franchiseUseCase.getProductMostStock(franchiseId)
                    .doOnSubscribe(subscription -> log.info(":: Request getProductMostStock", kv(RQ, franchiseId)));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response getProductMostStock", kv(RS, response));
            return buildResponse(serverRequest, HttpStatus.OK, response);
        });
    }

    public Mono<ServerResponse> fallback(ServerRequest serverRequest, Exception ex) {
        var response = GenericResponse.error(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage());
        return buildResponse(serverRequest, HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    public Mono<ServerResponse> fallback(ServerRequest serverRequest,  CallNotPermittedException callNotPermittedException) {
        log.error("Circuit breaker fallback activated for FranchiseHandler", callNotPermittedException);
        var response = GenericResponse.error(String.valueOf(HttpStatus.SERVICE_UNAVAILABLE.value()), callNotPermittedException.getMessage());
        return buildResponse(serverRequest, HttpStatus.SERVICE_UNAVAILABLE, response);
    }
}