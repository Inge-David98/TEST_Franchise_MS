package co.com.franchise.r2dbc;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.r2dbc.dto.*;
import co.com.franchise.r2dbc.helper.R2dbcOperation;
import co.com.franchise.r2dbc.mapper.MapperAdapter;
import co.com.franchise.r2dbc.repository.BranchRepository;
import co.com.franchise.r2dbc.repository.FranchiseRepository;
import co.com.franchise.r2dbc.repository.ProductBranchRepository;
import co.com.franchise.r2dbc.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.FetchSpec;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GeneralAdapterTest {

    @Mock
    private FranchiseRepository franchiseRepository;
    @Mock
    private BranchRepository branchRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductBranchRepository productBranchRepository;
    @Mock
    private R2dbcOperation r2dbcOperation;


    private MapperAdapter mapper;

    private GeneralAdapter generalAdapter;

    @BeforeEach
    public void setUp() {
        mapper = mapper.MAPPER;
        generalAdapter = new GeneralAdapter(franchiseRepository,branchRepository,
                productRepository,productBranchRepository,r2dbcOperation);
    }

    @Test
    void addFranchise() {
        when(franchiseRepository.save(any(FranchiseData.class)))
                .thenReturn(Mono.just(FranchiseData.builder().build()));

        Mono<ResponseFranquicia> responseFranquicia= generalAdapter.addFranchise(Franquicia.builder().nombre("FR01").build());
        StepVerifier.create(responseFranquicia).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void addFranchiseError() {
        when(franchiseRepository.save(any(FranchiseData.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ResponseFranquicia> responseFranquicia= generalAdapter.addFranchise(Franquicia.builder().nombre("FR01").build());
        StepVerifier.create(responseFranquicia)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Error creando Franquicia: Database error"))
                .verify();
    }

    @Test
    void addBranch() {
        when(franchiseRepository.findById(anyLong()))
                .thenReturn(Mono.just(FranchiseData.builder().build()));
        when(branchRepository.save(any(BranchData.class)))
                .thenReturn(Mono.just(BranchData.builder().build()));
        Mono<ResponseSucursal> responseBranch= generalAdapter.addBranch(Sucursal.builder().nombre("FR01").build(),"1");
        StepVerifier.create(responseBranch).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();

    }

    @Test
    void addBranchError() {
        when(franchiseRepository.findById(anyLong()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));
        Mono<ResponseSucursal> responseBranch= generalAdapter.addBranch(Sucursal.builder().nombre("FR01").build(),"1");
        StepVerifier.create(responseBranch)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Error creando Sucursal: Database error"))
                .verify();

    }

    @Test
    void addProduct() {
        when(productRepository.save(any(ProductData.class)))
                .thenReturn(Mono.just(ProductData.builder().build()));
        when(productBranchRepository.save(any(BranchProductData.class)))
                .thenReturn(Mono.just(BranchProductData.builder().build()));

        Mono<ResponseProducto> responseProduct= generalAdapter.addProduct(Producto.builder().nombre("FR01").stock(8L).build(),"1");
        StepVerifier.create(responseProduct).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void addProductError() {
        when(productRepository.save(any(ProductData.class)))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        Mono<ResponseProducto> responseProduct= generalAdapter.addProduct(Producto.builder().nombre("FR01").stock(8L).build(),"1");
        StepVerifier.create(responseProduct)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Error Agregando el producto: Database error"))
                .verify();


    }

    @Test
    void deleteProduct() {
        when(productBranchRepository.findByProductIdAndBranchId(anyLong(),anyLong()))
                .thenReturn(Mono.just(BranchProductData.builder().build()));
        when(productBranchRepository.deleteByProductIdAndBranchId(anyLong(),anyLong())).thenReturn(Mono.empty());
        when(productRepository.findById(anyLong()))
                .thenReturn(Mono.just(ProductData.builder().productId(1L).build()));
        when(productRepository.deleteByProductId(anyLong())).thenReturn(Mono.empty());

        Mono<ResponseProducto> responseProduct= generalAdapter.deleteProduct("1","1");
        StepVerifier.create(responseProduct).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void deleteProductError() {
        when(productBranchRepository.findByProductIdAndBranchId(anyLong(),anyLong()))
                .thenReturn(Mono.error(new RuntimeException("Database error")));
        Mono<ResponseProducto> responseProduct= generalAdapter.deleteProduct("1","1");
        StepVerifier.create(responseProduct)
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Error eliminando el producto: Database error"))
                .verify();
    }

    @Test
    void updateStock() {
        when(r2dbcOperation.operationUpdateStock(anyString(),anyString(),anyLong()))
                .thenReturn(Mono.just(ResponseMessage.builder().message("Stock actualizado correctamente").build()));

        Mono<ResponseMessage> responseMessage= generalAdapter.updateStock("1","1",2L);
        StepVerifier.create(responseMessage)
                .expectNextMatches(response -> response.getMessage().equals("Stock actualizado correctamente"))
                .verifyComplete();
    }

    @Test
    void getProductMostStock() {

        BranchData branchData = BranchData.builder().branchId(1L).name("SC01").build();

        when(franchiseRepository.findById(anyLong()))
                .thenReturn(Mono.just(FranchiseData.builder().franchiseId(1L).build()));
        when(branchRepository.findByFranchiseId(anyLong()))
                .thenReturn(Flux.just(branchData));
        when(productBranchRepository.findByBranchProduct(anyLong()))
                .thenReturn(Flux.just(BranchProductData.builder().product(1L).branch(2L).stock(3L).build()));
        when(productRepository.findById(anyLong()))
                .thenReturn(Mono.just(ProductData.builder().name("PR01").build()));

        ResponseProductoSucursal expectedResponse = ResponseProductoSucursal.builder()
                .productos(List.of(Producto.builder()
                        .nombre("PR01")
                        .sucursal("SC01")
                        .stock(3L)
                        .build()))
                .build();

        Mono<ResponseProductoSucursal> responseProductoSucursal= generalAdapter.getProductMostStock("1");
        StepVerifier.create(responseProductoSucursal)
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void updateNameFranchise() {
        when(r2dbcOperation.operationUpdateNameFranchise(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().message("actualizado").build()));

        Mono<ResponseMessage> responseMessage= generalAdapter.updateNameFranchise("FR01","1");
        StepVerifier.create(responseMessage)
                .expectNextMatches(response -> response.getMessage().equals("actualizado"))
                .verifyComplete();
    }

    @Test
    void updateNameBranch() {
        when(r2dbcOperation.operationUpdateNameBranch(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().message("actualizado").build()));

        Mono<ResponseMessage> responseMessage= generalAdapter.updateNameBranch("SC01","1");
        StepVerifier.create(responseMessage)
                .expectNextMatches(response -> response.getMessage().equals("actualizado"))
                .verifyComplete();
    }

    @Test
    void updateNameProduct() {
        when(r2dbcOperation.operationuUdateNameProduct(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().message("actualizado").build()));

        Mono<ResponseMessage> responseMessage= generalAdapter.updateNameProduct("FR01","1");
        StepVerifier.create(responseMessage)
                .expectNextMatches(response -> response.getMessage().equals("actualizado"))
                .verifyComplete();
    }
}