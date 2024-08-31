package co.com.franchise.api.mapper;

import co.com.franchise.api.dto.request.Branch;
import co.com.franchise.api.dto.request.Franchise;
import co.com.franchise.api.dto.request.Product;
import co.com.franchise.model.franquicia.Franquicia;
import co.com.franchise.model.franquicia.Producto;
import co.com.franchise.model.franquicia.Sucursal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperRequest {
    MapperRequest MAPPER= Mappers.getMapper(MapperRequest.class);
    @Mapping(target = "nombre", source = "franchiseName")
    Franquicia requestToFranquicia(Franchise franchise);

    @Mapping(target = "nombre", source = "branchName")
    Sucursal requestToSucursal(Branch branch);

    @Mapping(target = "nombre", source = "productName")
    @Mapping(target = "stock", source = "stock")
    Producto requestToProduct(Product product);
}
