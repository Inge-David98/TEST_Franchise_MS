package co.com.franchise.usecase.franchise;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.franquicia.gateways.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private final FranquiciaRepository franquiciaRepository;

    public Mono<ResponseFranquicia> addFranquise(Franquicia franquicia){
        return franquiciaRepository.addFranchise(franquicia);
    }

    public Mono<ResponseSucursal> addBranch(Sucursal Sucursal, String franchiseId){
        return franquiciaRepository.addBranch(Sucursal,franchiseId);
    }
    public Mono<ResponseProducto> addProduct(Producto producto, String branchId){
        return franquiciaRepository.addProduct(producto,branchId);
    }
    public Mono<ResponseProducto> deleteProduct(String branchId, String productId){
        return franquiciaRepository.deleteProduct(branchId,productId);
    }
    public Mono<ResponseMessage> updateStock(String branchId, String productId, Long stock){
        return franquiciaRepository.updateStock(branchId,productId,stock);
    }
}
