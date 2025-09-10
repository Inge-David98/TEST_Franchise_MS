package co.com.franchise.api.handlers;

import co.com.franchise.api.GenericHandler;
import co.com.franchise.api.dto.request.Branch;
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
public class BranchHandler extends GenericHandler {

    private static final String ID_FRANCHISE = "franchiseId";
    private static final String ID_BRANCH = "branchId";
    private static final String RQ = "RQ";
    private static final String RS = "RS";
    private static final String FALLBACK_METHOD_NAME = "fallback";
    
    private final FranchiseUseCase franchiseUseCase;

    @CircuitBreaker(name = "addBranch", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> addBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
                    return franchiseUseCase.addBranch(MapperRequest.MAPPER.requestToSucursal(branch), franchiseId)
                            .doOnSubscribe(subscription -> log.info(":: Request addBranch", kv(RQ, branch, franchiseId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addBranch", kv(RS, response));
                    return buildResponse(serverRequest, HttpStatus.OK, response);
                });
    }

    @CircuitBreaker(name = "updateNameBranch", fallbackMethod = FALLBACK_METHOD_NAME)
    public Mono<ServerResponse> updateNameBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    String branchId = serverRequest.pathVariable(ID_BRANCH);
                    return franchiseUseCase.updateNameBranch(branch.getBranchName(), branchId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameBranch", kv(RQ, branch, branchId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameBranch", kv(RS, response));
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