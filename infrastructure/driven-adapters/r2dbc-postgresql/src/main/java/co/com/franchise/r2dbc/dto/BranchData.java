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
@Table("branch")
public class BranchData {
    @Id
    @Column("branch_id")
    private Long branchId;

    @Column("name")
    private String name;

    @Column("franchise_id")
    private Long franchiseId;

}
