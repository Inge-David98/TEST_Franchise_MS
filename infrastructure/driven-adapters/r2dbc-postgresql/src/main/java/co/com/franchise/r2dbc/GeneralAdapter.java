package co.com.franchise.r2dbc;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.franquicia.gateways.FranquiciaRepository;
import co.com.franchise.r2dbc.dto.BranchData;
import co.com.franchise.r2dbc.dto.BranchProductData;
import co.com.franchise.r2dbc.mapper.MapperAdapter;
import co.com.franchise.r2dbc.repository.BranchRepository;
import co.com.franchise.r2dbc.repository.FranchiseRepository;
import co.com.franchise.r2dbc.repository.ProductBranchRepository;
import co.com.franchise.r2dbc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class GeneralAdapter implements FranquiciaRepository {

    private final FranchiseRepository franchiseRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final ProductBranchRepository productBranchRepository;
    private final R2dbcEntityOperations r2dbcEntityOperations;

    @Override
    public Mono<ResponseFranquicia> addFranchise(Franquicia franquicia) {
        return franchiseRepository.save(MapperAdapter.MAPPER.requestToFranchiseData(franquicia))
                .map(MapperAdapter.MAPPER::requestToFranquicia)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error creando Franquicia: " + e.getMessage())));
    }

    @Override
    public Mono<ResponseSucursal> addBranch(Sucursal sucursal, String franchiseId) {
        return franchiseRepository.findById(Long.valueOf(franchiseId)).flatMap(franchiseData -> {
            BranchData branchData = MapperAdapter.MAPPER.requestToBranchData(sucursal);
            branchData.setFranchiseId(Long.valueOf(franchiseId));
            return branchRepository.save(branchData).map(MapperAdapter.MAPPER::responseToSucursal);
        }).onErrorResume(e -> Mono.error(new RuntimeException("Error creando Sucursal: " + e.getMessage())));
    }

    @Override
    public Mono<ResponseProducto> addProduct(Producto producto, String branchId) {
        return productRepository.save(MapperAdapter.MAPPER.requestToProductData(producto))
                .flatMap(productData -> {
                    BranchProductData branchProductData = BranchProductData.builder().branch(Long.valueOf(branchId))
                            .product(productData.getProductId()).stock(producto.getStock())
                            .build();
                    return productBranchRepository.save(branchProductData)
                            .map(response -> MapperAdapter.MAPPER.responseToProducto(response, productData.getName()));
                }).onErrorResume(e -> Mono.error(new RuntimeException("Error Agregando el producto: " + e.getMessage())));
    }

    @Override
    public Mono<ResponseProducto> deleteProduct(String branchId, String productId) {
        return productBranchRepository.findByProductIdAndBranchId(Long.valueOf(productId), Long.valueOf(branchId))
                .flatMap(branchProductData ->
                        productBranchRepository.deleteByProductIdAndBranchId(Long.valueOf(productId), Long.valueOf(branchId))
                                .then(productRepository.findById(Long.valueOf(productId)))
                                .flatMap(productData ->
                                        productRepository.deleteByProductId(productData.getProductId())
                                                .thenReturn(MapperAdapter.MAPPER.responseToProducto(branchProductData, productData.getName()))
                                )
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error eliminando el producto: " + e.getMessage())));
    }

    @Override
    public Mono<ResponseMessage> updateStock(String branchId, String productId, Long stock) {
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


}
