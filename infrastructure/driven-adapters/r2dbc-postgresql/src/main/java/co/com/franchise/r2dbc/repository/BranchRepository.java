package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.dto.BranchData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface BranchRepository extends ReactiveCrudRepository<BranchData, Long> {
    Flux<BranchData> findByFranchiseId(Long franchiseId);
}
