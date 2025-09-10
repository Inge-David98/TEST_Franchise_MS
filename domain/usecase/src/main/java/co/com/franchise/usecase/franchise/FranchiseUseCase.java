package co.com.franchise.usecase.franchise;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.franquicia.gateways.FranquiciaRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {
    private static final String FRANCHISE_ID = "franchiseId";
    private static final String BRANCH_ID = "branchId";
    private static final String PRODUCT_ID = "productId";
    
    private final FranquiciaRepository franquiciaRepository;

    public Mono<ResponseFranquicia> addFranchise(Franquicia franquicia){
        return validateFranchise(franquicia)
                .then(franquiciaRepository.addFranchise(franquicia));
    }

    public Mono<ResponseSucursal> addBranch(Sucursal sucursal, String franchiseId){
        return validateBranchInput(sucursal, franchiseId)
                .then(franquiciaRepository.addBranch(sucursal, franchiseId));
    }
    
    public Mono<ResponseProducto> addProduct(Producto producto, String branchId){
        return validateProductInput(producto, branchId)
                .then(franquiciaRepository.addProduct(producto, branchId));
    }
    
    public Mono<ResponseProducto> deleteProduct(String branchId, String productId){
        return validateDeleteInput(branchId, productId)
                .then(franquiciaRepository.deleteProduct(branchId, productId));
    }
    
    public Mono<ResponseMessage> updateStock(String branchId, String productId, Long stock){
        return validateStockInput(branchId, productId, stock)
                .then(franquiciaRepository.updateStock(branchId, productId, stock));
    }

    public Mono<ResponseProductoSucursal> getProductMostStock(String franchiseId){
        return validateId(franchiseId, FRANCHISE_ID)
                .then(franquiciaRepository.getProductMostStock(franchiseId));
    }

    public Mono<ResponseMessage> updateNameFranchise(String name, String franchiseId){
        return validateNameUpdate(name, franchiseId, FRANCHISE_ID)
                .then(franquiciaRepository.updateNameFranchise(name, franchiseId));
    }
    
    public Mono<ResponseMessage> updateNameBranch(String name, String branchId){
        return validateNameUpdate(name, branchId, BRANCH_ID)
                .then(franquiciaRepository.updateNameBranch(name, branchId));
    }
    
    public Mono<ResponseMessage> updateNameProduct(String name, String productId){
        return validateNameUpdate(name, productId, PRODUCT_ID)
                .then(franquiciaRepository.updateNameProduct(name, productId));
    }

    private Mono<Void> validateFranchise(Franquicia franquicia) {
        if (franquicia == null || franquicia.getNombre() == null || franquicia.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Franquicia y nombre son requeridos"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateBranchInput(Sucursal sucursal, String franchiseId) {
        if (sucursal == null || sucursal.getNombre() == null || sucursal.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Sucursal y nombre son requeridos"));
        }
        return validateId(franchiseId, FRANCHISE_ID).then();
    }

    private Mono<Void> validateProductInput(Producto producto, String branchId) {
        if (producto == null || producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Producto y nombre son requeridos"));
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            return Mono.error(new IllegalArgumentException("Stock debe ser mayor o igual a 0"));
        }
        return validateId(branchId, BRANCH_ID).then();
    }

    private Mono<Void> validateDeleteInput(String branchId, String productId) {
        return Mono.zip(validateId(branchId, BRANCH_ID), validateId(productId, PRODUCT_ID)).then();
    }

    private Mono<Void> validateStockInput(String branchId, String productId, Long stock) {
        if (stock == null || stock < 0) {
            return Mono.error(new IllegalArgumentException("Stock debe ser mayor o igual a 0"));
        }
        return Mono.zip(validateId(branchId, BRANCH_ID), validateId(productId, PRODUCT_ID)).then();
    }

    private Mono<Void> validateNameUpdate(String name, String id, String fieldName) {
        if (name == null || name.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Nombre es requerido"));
        }
        return validateId(id, fieldName).then();
    }

    private Mono<Long> validateId(String id, String fieldName) {
        if (id == null || id.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException(fieldName + " es requerido"));
        }
        try {
            return Mono.just(Long.valueOf(id));
        } catch (NumberFormatException e) {
            return Mono.error(new IllegalArgumentException(fieldName + " inválido: " + id));
        }
    }
}
