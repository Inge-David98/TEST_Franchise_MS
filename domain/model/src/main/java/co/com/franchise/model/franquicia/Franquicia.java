package co.com.franchise.model.franquicia;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
@Value
public class Franquicia {
    String nombre;
    List<Sucursal> sucursales;
}
