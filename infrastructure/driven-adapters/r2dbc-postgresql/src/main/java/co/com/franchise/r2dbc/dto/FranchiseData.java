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
@Table(name = "franchise")
public class FranchiseData {
    @Id
    @Column("franchise_id")
    private Long franchiseId;

    @Column("name")
    private String name;

}
