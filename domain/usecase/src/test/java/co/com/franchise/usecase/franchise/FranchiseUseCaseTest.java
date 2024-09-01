package co.com.franchise.usecase.franchise;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.franquicia.gateways.FranquiciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class FranchiseUseCaseTest {
    @Mock
    private FranquiciaRepository franquiciaRepository;

    @InjectMocks
    private FranchiseUseCase franchiseUseCase;

    @Test
    void addFranchise() {
        Mockito.when(franquiciaRepository.addFranchise(any(Franquicia.class)))
                .thenReturn(Mono.just(ResponseFranquicia.builder().build()));
        Mono<ResponseFranquicia> responseFranquicia = franchiseUseCase.addFranchise(Franquicia.builder().nombre("FR01").build());
        StepVerifier.create(responseFranquicia).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void addBranch() {
        Mockito.when(franquiciaRepository.addBranch(any(Sucursal.class),anyString()))
                .thenReturn(Mono.just(ResponseSucursal.builder().build()));
        Mono<ResponseSucursal> responseSucursal= franchiseUseCase.addBranch(Sucursal.builder().nombre("SC01").build(),"1");
        StepVerifier.create(responseSucursal).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void addProduct() {
        Mockito.when(franquiciaRepository.addProduct(any(Producto.class), anyString()))
                .thenReturn(Mono.just(ResponseProducto.builder().build()));
        Mono<ResponseProducto> responseProducto= franchiseUseCase.addProduct(Producto.builder().nombre("PR01").stock(4L).build(),"1");
        StepVerifier.create(responseProducto).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void deleteProduct() {
        Mockito.when(franquiciaRepository.deleteProduct(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseProducto.builder().build()));
        Mono<ResponseProducto> responseProducto= franchiseUseCase.deleteProduct("1","1");
        StepVerifier.create(responseProducto).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void updateStock() {
        Mockito.when(franquiciaRepository.updateStock(anyString(),anyString(),anyLong()))
                .thenReturn(Mono.just(ResponseMessage.builder().build()));
        Mono<ResponseMessage> responseMessage= franchiseUseCase.updateStock("1","1", 4L);
        StepVerifier.create(responseMessage).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void getProductMostStock() {
        Mockito.when(franquiciaRepository.getProductMostStock(ArgumentMatchers.anyString()))
                .thenReturn(Mono.just(ResponseProductoSucursal.builder().build()));
        Mono<ResponseProductoSucursal> responseProductoSucursal= franchiseUseCase.getProductMostStock("1");
        StepVerifier.create(responseProductoSucursal).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void updateNameFranchise() {
        Mockito.when(franquiciaRepository.updateNameFranchise(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().build()));
        Mono<ResponseMessage> responseMessage= franchiseUseCase.updateNameFranchise("FR02","1");
        StepVerifier.create(responseMessage).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void updateNameBranch() {
        Mockito.when(franquiciaRepository.updateNameBranch(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().build()));
        Mono<ResponseMessage> responseMessage= franchiseUseCase.updateNameBranch("SC02","1");
        StepVerifier.create(responseMessage).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }

    @Test
    void updateNameProduct() {
        Mockito.when(franquiciaRepository.updateNameProduct(anyString(),anyString()))
                .thenReturn(Mono.just(ResponseMessage.builder().build()));
        Mono<ResponseMessage> responseMessage= franchiseUseCase.updateNameProduct("PR02","1");
        StepVerifier.create(responseMessage).expectNextMatches(response -> {
            assertNotNull(response);
            return true;
        }).verifyComplete();
    }
}