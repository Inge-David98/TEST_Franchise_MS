package co.com.franchise.api;

import co.com.franchise.api.dto.request.Branch;
import co.com.franchise.api.dto.request.Franchise;
import co.com.franchise.api.dto.request.Product;
import co.com.franchise.model.franquicia.*;
import co.com.franchise.model.utils.Operation;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(SpringExtension.class)
@WebFluxTest
@ContextConfiguration(classes = {RouterRest.class, Handler.class})
class HandlerTest {
    @Autowired
    private ApplicationContext context;

    private WebTestClient webTestClient;

    @MockBean
    FranchiseUseCase franchiseUseCase;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context)
                .configureClient().build();
    }

    @Test
    void userHandlerTestSuccess() {
        given(franchiseUseCase.addFranchise(any(Franquicia.class))).willReturn(
                Mono.just(ResponseFranquicia.builder().build()));

        given(franchiseUseCase.addBranch(any(Sucursal.class),anyString())).willReturn(
                Mono.just(ResponseSucursal.builder().build()));

        given(franchiseUseCase.addProduct(any(Producto.class),ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseProducto.builder().build()));

        given(franchiseUseCase.deleteProduct(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseProducto.builder().build()));

        given(franchiseUseCase.updateStock(ArgumentMatchers.anyString()
                ,ArgumentMatchers.anyString(),anyLong())).willReturn(
                Mono.just(ResponseMessage.builder().build()));

        given(franchiseUseCase.getProductMostStock(ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseProductoSucursal.builder().build()));

        given(franchiseUseCase.updateNameFranchise(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseMessage.builder().build()));

        given(franchiseUseCase.updateNameBranch(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseMessage.builder().build()));

        given(franchiseUseCase.updateNameProduct(ArgumentMatchers.anyString(),ArgumentMatchers.anyString())).willReturn(
                Mono.just(ResponseMessage.builder().build()));

        webTestClient.post().uri(Operation.ADD_FRANCHISIE.getPath())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Franchise.builder().franchiseName("franchiseName").build())
                .exchange().expectStatus()
                .isOk().expectBody(Franchise.class)
                .value(org.junit.jupiter.api.Assertions::assertNotNull);

        webTestClient.post().uri(Operation.ADD_BRANCH.getPath(),"franchiseId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Branch.builder().branchName("branchName").build())
                .exchange().expectStatus()
                .isOk().expectBody(Branch.class)
                .value(org.junit.jupiter.api.Assertions::assertNotNull);

        webTestClient.post()
                .uri(Operation.ADD_PRODUCT.getPath(), "branchId")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(Product.builder().productName("productName").stock(3).build())
                .exchange()
                .expectStatus().isOk().expectBody(Product.class)
                .value(org.junit.jupiter.api.Assertions::assertNotNull);

        webTestClient.patch()
                .uri(Operation.UPDATE_STOCK.getPath()+"?value=8", "branchId","productId","value")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();



        webTestClient.delete().uri(Operation.DELETE_PRODUCT.getPath(), "branchId","productId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus()
                .isOk().expectBody(Product.class)
                .value(org.junit.jupiter.api.Assertions::assertNotNull);

        webTestClient.get()
                .uri(Operation.GET_PRODUCT_MOST_STOCK.getPath(), "franchiseId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange();

        webTestClient.patch()
                .uri(Operation.UPDATE_NAME_FRANCHISIE.getPath(), "franchiseId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Franchise.builder().franchiseName("franchiseName").build())
                .exchange();


        webTestClient.patch()
                .uri(Operation.UPDATE_NAME_BRANCH.getPath(), "branchId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Branch.builder().branchName("branchName").build())
                .exchange();

        webTestClient.patch()
                .uri(Operation.UPDATE_NAME_PRODUCT.getPath(), "productId")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Product.builder().productName("productName").stock(2).build())
                .exchange();
    }

}