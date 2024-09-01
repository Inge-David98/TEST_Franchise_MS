package co.com.franchise.r2dbc.helper;

import co.com.franchise.model.franquicia.ResponseMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class R2dbcOperation {
    private final R2dbcEntityOperations r2dbcEntityOperations;

    public Mono<ResponseMessage> operationUpdateStock(String branchId, String productId, Long stock){
        String sql = "UPDATE branch_product SET stock = $1 WHERE branch_id = $2 AND product_id = $3";
        return r2dbcEntityOperations.getDatabaseClient()
                .sql(sql)
                .bind(0, stock)
                .bind(1, Long.valueOf(branchId))
                .bind(2, Long.valueOf(productId))
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(ResponseMessage.builder().message("Stock actualizado correctamente").build());
                    } else {
                        return Mono.just(ResponseMessage.builder().message("No se encontró el producto o sucursal para actualizar").build());
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseMessage.builder().message("Error actualizando el stock: " + e.getMessage()).build()));
    }

    public Mono<ResponseMessage> operationUpdateNameFranchise(String name, String franchiseId){
        String sql = "UPDATE franchise SET name = $1 WHERE franchise_id = $2";
        return r2dbcEntityOperations.getDatabaseClient()
                .sql(sql)
                .bind(0, name)
                .bind(1, Long.valueOf(franchiseId))
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(ResponseMessage.builder().message("Nombre de Franquicia actualizado correctamente").build());
                    } else {
                        return Mono.just(ResponseMessage.builder().message("No se encontró la franquicia para actualizar").build());
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseMessage.builder().message("Error actualizando el nombre de la franquicia: " + e.getMessage()).build()));
    }

    public Mono<ResponseMessage> operationUpdateNameBranch(String name, String branchId){
        String sql = "UPDATE branch SET name = $1 WHERE branch_id = $2";
        return r2dbcEntityOperations.getDatabaseClient()
                .sql(sql)
                .bind(0, name)
                .bind(1, Long.valueOf(branchId))
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(ResponseMessage.builder().message("Nombre de la Sucursal actualizado correctamente").build());
                    } else {
                        return Mono.just(ResponseMessage.builder().message("No se encontró la Sucursal para actualizar").build());
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseMessage.builder().message("Error actualizando el nombre de la Sucursal: " + e.getMessage()).build()));
    }

    public Mono<ResponseMessage> operationuUdateNameProduct(String name, String productId){
        String sql = "UPDATE product SET name = $1 WHERE product_id = $2";
        return r2dbcEntityOperations.getDatabaseClient()
                .sql(sql)
                .bind(0, name)
                .bind(1, Long.valueOf(productId))
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(ResponseMessage.builder().message("Nombre del Producto actualizado correctamente").build());
                    } else {
                        return Mono.just(ResponseMessage.builder().message("No se encontró el Producto para actualizar").build());
                    }
                })
                .onErrorResume(e -> Mono.just(ResponseMessage.builder().message("Error actualizando el nombre del Producto: " + e.getMessage()).build()));
    }
}
