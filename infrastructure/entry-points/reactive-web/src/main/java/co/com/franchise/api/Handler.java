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

import static net.logstash.logback.argument.StructuredArguments.kv;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler extends GenericHandler{

    private static final String ID_FRANCHISE = "franchiseId";
    private static final String ID_BRANCH = "branchId";
    private static final String ID_PRODUCT = "productId";

    private static final String RQ = "RQ";
    private static final String RS = "RS";
    private final FranchiseUseCase franchiseUseCase;



    public Mono<ServerResponse> addFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Franchise.class)
                .flatMap(franchise -> franchiseUseCase.addFranchise(MapperRequest.MAPPER.requestToFranquicia(franchise))
                        .doOnSubscribe(suscription-> log.info(":: Request addFranchise", kv(RQ, franchise))))
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addFranchise", kv(RS,response));
                    return buildResponse(serverRequest, HttpStatus.OK, response);
                });


    }

    public Mono<ServerResponse> addBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
                    return franchiseUseCase.addBranch(MapperRequest.MAPPER.requestToSucursal(branch),franchiseId)
                            .doOnSubscribe(subscription -> log.info(":: Request addBranch", kv(RQ, branch,franchiseId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addBranch", kv(RS,response));
                    return buildResponse(serverRequest,HttpStatus.OK, response);
                });
    }

    public Mono<ServerResponse> addProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    String branchId = serverRequest.pathVariable(ID_BRANCH);
                    return franchiseUseCase.addProduct(MapperRequest.MAPPER.requestToProduct(product),branchId)
                            .doOnSubscribe(subscription -> log.info(":: Request addProduct", kv(RQ, branchId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response addProduct", kv(RS,response));
                    return buildResponse(serverRequest,HttpStatus.OK, response);
                });

    }

    public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            return franchiseUseCase.deleteProduct(branchId,productId)
                    .doOnSubscribe(subscription -> log.info(":: Request deleteProduct", kv(RQ, branchId,productId)));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response deleteProduct", kv(RS,response));
            return buildResponse(serverRequest,HttpStatus.OK, response);
        }).switchIfEmpty(this.buildInvalidBodyResponse(serverRequest, HttpStatus.OK,"El Producto no se encuentra o ya ha sido eliminado"));
    }

    public Mono<ServerResponse> updateStock(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String branchId = serverRequest.pathVariable(ID_BRANCH);
            String productId = serverRequest.pathVariable(ID_PRODUCT);
            String stock = serverRequest.queryParam("value")
                    .orElseThrow(() -> new IllegalArgumentException("El parámetro 'value' es requerido"));
            return franchiseUseCase.updateStock(branchId,productId,Long.valueOf(stock))
                    .doOnSubscribe(subscription -> log.info(":: Request updateStock", kv(RQ,
                            String.format("(branchId:%s,productId:%s,stock:%s)", branchId, productId, stock))));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response updateStock", kv(RS,response));
            return buildResponse(serverRequest,HttpStatus.OK, response);
        });
    }

    public Mono<ServerResponse> getProductMostStock(ServerRequest serverRequest) {
        return Mono.defer(() -> {
            String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
            return franchiseUseCase.getProductMostStock(franchiseId)
                    .doOnSubscribe(subscription -> log.info(":: Request getProductMostStock", kv(RQ, franchiseId)));
        }).flatMap(res -> {
            var response = GenericResponse.success(res);
            log.info("::Response getProductMostStock", kv(RS,response));
            return buildResponse(serverRequest,HttpStatus.OK, response);
        });
    }

    public Mono<ServerResponse> updateNameFranchise(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Franchise.class)
                .flatMap(franchise -> {
                    String franchiseId = serverRequest.pathVariable(ID_FRANCHISE);
                    return franchiseUseCase.updateNameFranchise(franchise.getFranchiseName(),franchiseId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameFranchise", kv(RQ, franchise,franchiseId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameFranchise", kv(RS,response));
                    return buildResponse(serverRequest,HttpStatus.OK, response);
                });

    }
    public Mono<ServerResponse> updateNameBranch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Branch.class)
                .flatMap(branch -> {
                    String branchId = serverRequest.pathVariable(ID_BRANCH);
                    return franchiseUseCase.updateNameBranch(branch.getBranchName(),branchId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameBranch", kv(RQ, branch,branchId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameBranch", kv(RS,response));
                    return buildResponse(serverRequest,HttpStatus.OK, response);
                });

    }
    public Mono<ServerResponse> updateNameProduct(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Product.class)
                .flatMap(product -> {
                    String productId = serverRequest.pathVariable(ID_PRODUCT);
                    return franchiseUseCase.updateNameProduct(product.getProductName(),productId)
                            .doOnSubscribe(subscription -> log.info(":: Request updateNameProduct", kv(RQ, product,productId)));
                })
                .flatMap(res -> {
                    var response = GenericResponse.success(res);
                    log.info("::Response updateNameProduct", kv(RS,response));
                    return buildResponse(serverRequest,HttpStatus.OK, response);
                });

    }
}
