package co.com.franchise.api.handlers;

import co.com.franchise.api.GenericHandler;
import co.com.franchise.api.dto.request.Product;
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
public class ProductHandler extends GenericHandler {

    private static final String ID_BRANCH = "branchId";
    private static final String ID_PRODUCT = "productId";
    private static final String RQ = "RQ";
    private static final String RS = "RS";
    private static final String FALLBACK_METHOD_NAME = "fallback";
    
    private final FranchiseUseCase franchiseUseCase;

    @CircuitBreaker(name = "addProduct", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> addProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    String branchId = serverRequest.pathVariable(ID_BRANCH);
                    return franchiseUseCase.addProduct(MapperRequest.MAPPER.requestToProduct(product), branchId)
                            .doOnSubscribe(subscription -> log.info(":: Request addProduct", kv(RQ, branchId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addProduct", kv(RS, response));
                    return buildResponse(serverRequest, HttpStatus.OK, response);
                });
    }

    @CircuitBreaker(name = "deleteProduct", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            return franchiseUseCase.deleteProduct(branchId, productId)
                    .doOnSubscribe(subscription -> log.info(":: Request deleteProduct", kv(RQ, branchId, productId)));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response deleteProduct", kv(RS, response));
            return buildResponse(serverRequest, HttpStatus.OK, response);
        }).switchIfEmpty(this.buildInvalidBodyResponse(serverRequest, HttpStatus.OK, "El Producto no se encuentra o ya ha sido eliminado"));
    }

    @CircuitBreaker(name = "updateStock", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> updateStock(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            String stock = serverRequest.queryParam("value")
                    .orElseThrow(() -> new IllegalArgumentException("El parámetro 'value' es requerido"));
            return franchiseUseCase.updateStock(branchId, productId, Long.valueOf(stock))
                    .doOnSubscribe(subscription -> log.info(":: Request updateStock", kv(RQ,
                            String.format("(branchId:%s,productId:%s,stock:%s)", branchId, productId, stock))));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response updateStock", kv(RS, response));
            return buildResponse(serverRequest, HttpStatus.OK, response);
        });
    }

    @CircuitBreaker(name = "updateNameProduct", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> updateNameProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    String productId = serverRequest.pathVariable(ID_PRODUCT);
                    return franchiseUseCase.updateNameProduct(product.getProductName(), productId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameProduct", kv(RQ, product, productId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameProduct", kv(RS, response));
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