package co.com.franchise.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "branch_product")
public class BranchProductData {

    @Column("branch_id")
    private Long branch;

    @Column("product_id")
    private Long product;

    @Column("stock")
    private Long stock;
}
