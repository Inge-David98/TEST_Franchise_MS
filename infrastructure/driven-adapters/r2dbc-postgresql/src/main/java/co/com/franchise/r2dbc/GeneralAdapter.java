package co.com.franchise.r2dbc;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.franquicia.gateways.FranquiciaRepository;
import co.com.franchise.r2dbc.dto.BranchData;
import co.com.franchise.r2dbc.dto.BranchProductData;
import co.com.franchise.r2dbc.dto.ProductStock;
import co.com.franchise.r2dbc.dto.ResponseProductStock;
import co.com.franchise.r2dbc.helper.R2dbcOperation;
import co.com.franchise.r2dbc.mapper.MapperAdapter;
import co.com.franchise.r2dbc.repository.BranchRepository;
import co.com.franchise.r2dbc.repository.FranchiseRepository;
import co.com.franchise.r2dbc.repository.ProductBranchRepository;
import co.com.franchise.r2dbc.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final R2dbcOperation r2dbcOperation;


    @Override
    public Mono<ResponseFranquicia> addFranchise(Franquicia franquicia) {
        return franchiseRepository.save(MapperAdapter.MAPPER.requestToFranchiseData(franquicia))
                .map(MapperAdapter.MAPPER::requestToFranquicia)
                .onErrorResume(e -> Mono.error(new RuntimeException("Error creando Franquicia", e)));
    }

    @Override
    public Mono<ResponseSucursal> addBranch(Sucursal sucursal, String franchiseId) {
        return franchiseRepository.findById(Long.valueOf(franchiseId))
                .switchIfEmpty(Mono.error(new RuntimeException("Franquicia no encontrada")))
                .flatMap(franchiseData -> {
                    BranchData branchData = MapperAdapter.MAPPER.requestToBranchData(sucursal);
                    branchData.setFranchiseId(franchiseData.getFranchiseId());
                    return branchRepository.save(branchData).map(MapperAdapter.MAPPER::responseToSucursal);
                }).onErrorResume(e -> Mono.error(new RuntimeException("Error creando Sucursal", e)));
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
                }).onErrorResume(e -> Mono.error(new RuntimeException("Error Agregando el producto", e)));
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
                ).onErrorResume(e -> Mono.error(new RuntimeException("Error eliminando el producto", e)));
    }

    @Override
    public Mono<ResponseMessage> updateStock(String branchId, String productId, Long stock) {
        return r2dbcOperation.operationUpdateStock(branchId,productId,stock);
    }

    @Override
    public Mono<ResponseProductoSucursal> getProductMostStock(String franchiseId) {
        return franchiseRepository.findById(Long.valueOf(franchiseId))
                .flatMapMany(franchise -> branchRepository.findByFranchiseId(franchise.getFranchiseId()))
                .flatMap(branch -> productBranchRepository.findByBranchProduct(branch.getBranchId())
                        .sort((bp1, bp2) -> Integer.compare(bp2.getStock().intValue(), bp1.getStock().intValue()))
                        .next()
                        .flatMap(branchProduct -> productRepository.findById(branchProduct.getProduct())
                                .map(product -> ProductStock.builder()
                                        .nombre(product.getName())
                                        .sucursal(branch.getName())
                                        .stock(branchProduct.getStock())
                                        .build())))
                .collectList().map(response -> MapperAdapter.MAPPER.responseToProductoStock(ResponseProductStock.builder()
                                .products(response)
                        .build()));

    }

    @Override
    public Mono<ResponseMessage> updateNameFranchise(String name, String franchiseId) {
        return r2dbcOperation.operationUpdateNameFranchise(name,franchiseId);
    }

    @Override
    public Mono<ResponseMessage> updateNameBranch(String name, String branchId) {
        return r2dbcOperation.operationUpdateNameBranch(name,branchId);
    }

    @Override
    public Mono<ResponseMessage> updateNameProduct(String name, String productId) {
       return r2dbcOperation.operationUpdateNameProduct(name,productId);
    }




}
