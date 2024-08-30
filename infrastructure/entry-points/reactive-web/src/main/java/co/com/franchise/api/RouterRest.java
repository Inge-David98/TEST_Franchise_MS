package co.com.franchise.api;

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
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(Operation.ADD_FRANCHISIE.getPath()), handler::addFranchise)
                .andRoute(POST(Operation.ADD_BRANCH.getPath()), handler::addBranch)
                .andRoute(POST(Operation.ADD_PRODUCT.getPath()), handler::addProduct)
                .andRoute(DELETE(Operation.DELETE_PRODUCT.getPath()), handler::deleteProduct)
                .andRoute(PATCH(Operation.UPDATE_STOCK.getPath()), handler::updateStock)
                .and(route(GET(Operation.GET_PRODUCT_MOST_STOCK.getPath()), handler::getProductMostStock));
    }
}
