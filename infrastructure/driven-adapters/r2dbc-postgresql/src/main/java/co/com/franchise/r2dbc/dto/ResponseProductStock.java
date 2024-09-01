package co.com.franchise.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@lombok.Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ResponseProductStock {
    List<ProductStock> products;
}
