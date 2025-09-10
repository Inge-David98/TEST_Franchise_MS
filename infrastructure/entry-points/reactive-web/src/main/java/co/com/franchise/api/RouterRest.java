package co.com.franchise.api;

import co.com.franchise.api.handlers.BranchHandler;
import co.com.franchise.api.handlers.FranchiseHandler;
import co.com.franchise.api.handlers.ProductHandler;
import co.com.franchise.model.utils.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler, 
                                                        BranchHandler branchHandler, 
                                                        ProductHandler productHandler) {
        return route(POST(Operation.ADD_FRANCHISIE.getPath()), franchiseHandler::addFranchise)
                .andRoute(POST(Operation.ADD_BRANCH.getPath()), branchHandler::addBranch)
                .andRoute(POST(Operation.ADD_PRODUCT.getPath()), productHandler::addProduct)
                .andRoute(DELETE(Operation.DELETE_PRODUCT.getPath()), productHandler::deleteProduct)
                .andRoute(PATCH(Operation.UPDATE_STOCK.getPath()), productHandler::updateStock)
                .andRoute(GET(Operation.GET_PRODUCT_MOST_STOCK.getPath()), franchiseHandler::getProductMostStock)
                .andRoute(PATCH(Operation.UPDATE_NAME_FRANCHISIE.getPath()), franchiseHandler::updateNameFranchise)
                .andRoute(PATCH(Operation.UPDATE_NAME_BRANCH.getPath()), branchHandler::updateNameBranch)
                .and(route(PATCH(Operation.UPDATE_NAME_PRODUCT.getPath()), productHandler::updateNameProduct));
    }
}
