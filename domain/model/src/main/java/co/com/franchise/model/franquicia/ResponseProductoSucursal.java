package co.com.franchise.model.franquicia;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class ResponseProductoSucursal {
    List<Producto> productos;
}
