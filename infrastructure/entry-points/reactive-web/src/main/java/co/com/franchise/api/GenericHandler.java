package co.com.franchise.api;

import co.com.franchise.api.dto.response.ErrorResponse;
import co.com.franchise.api.dto.response.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class GenericHandler {
    public Mono<ServerResponse> buildResponse(ServerRequest serverRequest, HttpStatus httpStatus, GenericResponse<?> genericResponse) {
        return ServerResponse.status(httpStatus)
                .header("messageId", serverRequest.headers().firstHeader("messageId"))
                .header("date", LocalDateTime.now().toString())
                .bodyValue(genericResponse);
    }
    public Mono<ServerResponse> buildInvalidBodyResponse(ServerRequest serverRequest, HttpStatus httpStatus, String message) {
        return Mono.defer(() -> this.buildServerErrorResponse(serverRequest, httpStatus, this.buildError(message)));
    }

    private ErrorResponse buildError(String message) {
        return ErrorResponse.builder()
                .message(message)
                .build();
    }

    private Mono<ServerResponse> buildServerErrorResponse(ServerRequest serverRequest, HttpStatus httpStatus, ErrorResponse errorResponse) {
        return ServerResponse.status(httpStatus)
                .header("messageId", serverRequest.headers().firstHeader("messageId"))
                .header("date", LocalDateTime.now().toString())
                .bodyValue(errorResponse);
    }
}
