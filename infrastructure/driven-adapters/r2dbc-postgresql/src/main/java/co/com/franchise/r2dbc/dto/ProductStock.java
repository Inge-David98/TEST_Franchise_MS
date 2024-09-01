package co.com.franchise.r2dbc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@lombok.Data
@Builder(toBuilder = true)
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ProductStock {
    String nombre;
    String sucursal;
    Long stock;
}
