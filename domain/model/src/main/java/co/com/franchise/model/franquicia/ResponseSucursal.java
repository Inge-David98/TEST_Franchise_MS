package co.com.franchise.model.franquicia;

import lombok.Builder;
import lombok.Value;

@Builder(toBuilder = true)
@Value
public class ResponseSucursal {
    Long sucursalId;
    String nombre;
    Long franquiciaId;
}
