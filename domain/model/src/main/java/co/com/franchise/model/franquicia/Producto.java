package co.com.franchise.model.franquicia;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class Producto {
    String nombre;
    Long stock;
    String sucursal;
}
