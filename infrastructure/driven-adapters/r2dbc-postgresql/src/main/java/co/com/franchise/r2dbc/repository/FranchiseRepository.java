package co.com.franchise.r2dbc.repository;

import co.com.franchise.r2dbc.dto.FranchiseData;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface FranchiseRepository extends ReactiveCrudRepository<FranchiseData, Long> {
}
