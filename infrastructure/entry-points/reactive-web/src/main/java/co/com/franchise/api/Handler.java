package co.com.franchise.api;

import co.com.franchise.api.dto.request.Branch;
import co.com.franchise.api.dto.request.Franchise;
import co.com.franchise.api.dto.request.Product;
import co.com.franchise.api.dto.response.GenericResponse;
import co.com.franchise.api.mapper.MapperRequest;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler extends GenericHandler{

    private static final String ID_FRANCHISE = "franchiseId";
    private static final String ID_BRANCH = "branchId";
    private static final String ID_PRODUCT = "productId";
    private final FranchiseUseCase franchiseUseCase;



    public Mono<ServerResponse> addFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Franchise.class)
                .flatMap(franchise -> franchiseUseCase.addFranquise(MapperRequest.MAPPER.requestToFranquicia(franchise))
                        .doOnSubscribe(suscription-> log.info("Request"+franchise)))
                .flatMap(response -> buildResponse(serverRequest, HttpStatus.OK, GenericResponse.success(response)));

    }

    public Mono<ServerResponse> addBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
                    return franchiseUseCase.addBranch(MapperRequest.MAPPER.requestToSucursal(branch),franchiseId);
                })
                .flatMap(response -> buildResponse(serverRequest,HttpStatus.OK, GenericResponse.success(response)));
    }

    public Mono<ServerResponse> addProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    String branchId = serverRequest.pathVariable(ID_BRANCH);
                    return franchiseUseCase.addProduct(MapperRequest.MAPPER.requestToProduct(product),branchId);
                })
                .flatMap(response -> buildResponse(serverRequest,HttpStatus.OK, GenericResponse.success(response)));

    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            return franchiseUseCase.deleteProduct(branchId,productId);
        }).flatMap(response -> buildResponse(serverRequest,HttpStatus.OK,GenericResponse.success(response)))
                .switchIfEmpty(this.buildInvalidBodyResponse(serverRequest, HttpStatus.OK,"El Producto no se encuentra o ya ha sido eliminado"));
    }

    public Mono<ServerResponse> updateStock(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            String stock = serverRequest.queryParam("value")
                    .orElseThrow(() -> new IllegalArgumentException("El parámetro 'value' es requerido"));
            return franchiseUseCase.updateStock(branchId,productId,Long.valueOf(stock));
        }).flatMap(response -> buildResponse(serverRequest,HttpStatus.OK,GenericResponse.success(response)));
    }

    public Mono<ServerResponse> getProductMostStock(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }
}
