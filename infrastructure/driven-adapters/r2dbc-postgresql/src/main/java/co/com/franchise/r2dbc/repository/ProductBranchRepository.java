package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.dto.BranchProductData;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

public interface ProductBranchRepository extends ReactiveCrudRepository<BranchProductData, Void> {
    @Transactional
    @Modifying
    @Query("DELETE FROM branch_product bp WHERE bp.product_id = :productId AND bp.branch_id = :branchId")
    Mono<Void> deleteByProductIdAndBranchId(Long productId,Long branchId);

    @Transactional
    @Modifying
    @Query("UPDATE branch_product bp SET bp.branch_id= :branchId, bp.product_id= :productId, bp.stock= :stock WHERE product_id = :productId AND branch_id = :branchId")
    Mono<Void> updateStockBranchProduct(Long branchId,Long productId,Long stock);

    @Query("SELECT * FROM branch_product bp WHERE bp.product_id = :productId AND bp.branch_id = :branchId")
    Mono<BranchProductData> findByProductIdAndBranchId(Long productId,Long branchId);
}
