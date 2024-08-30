package co.com.franchise.r2dbc.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "product")
public class ProductData {
    @Id
    @Column("product_id")
    private Long productId;

    @Column("name")
    private String name;

}
