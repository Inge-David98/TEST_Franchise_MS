package co.com.franchise.model.franquicia.gateways;

import co.com.franchise.model.franquicia.*;
import reactor.core.publisher.Mono;

public interface FranquiciaRepository {
    Mono<ResponseFranquicia> addFranchise(Franquicia franquicia);
    Mono<ResponseSucursal> addBranch(Sucursal sucursal, String franchiseId);
    Mono<ResponseProducto> addProduct(Producto producto, String branchId);
    Mono<ResponseProducto> deleteProduct(String branchId, String productId);
    Mono<ResponseMessage> updateStock(String franchiseId, String productId, Long stock);
    Mono<ResponseProductoSucursal> getProductMostStock(String franchiseId);
    Mono<ResponseMessage> updateNameFranchise(String name,String franchiseId);
    Mono<ResponseMessage> updateNameBranch(String name,String branchId);
    Mono<ResponseMessage> updateNameProduct(String name,String productId);
}
