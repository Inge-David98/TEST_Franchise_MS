package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.dto.BranchData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface BranchRepository extends ReactiveCrudRepository<BranchData, Long> {
}
