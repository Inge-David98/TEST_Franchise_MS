package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.dto.ProductData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveCrudRepository<ProductData, Long> {
    @Transactional
    @Modifying
    @Query("DELETE FROM product p WHERE p.product_id = :productId")
    Mono<Void> deleteByProductId(Long productId);
}
