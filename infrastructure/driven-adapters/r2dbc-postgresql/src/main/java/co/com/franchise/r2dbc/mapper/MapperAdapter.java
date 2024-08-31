package co.com.franchise.r2dbc.mapper;

import co.com.franchise.model.franquicia.*;
import co.com.franchise.r2dbc.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MapperAdapter {
    MapperAdapter MAPPER= Mappers.getMapper(MapperAdapter.class);
    @Mapping(target = "name", source = "nombre")
    FranchiseData requestToFranchiseData(Franquicia franquicia);

    @Mapping(target = "franquiciaId", source = "franchiseId")
    @Mapping(target = "nombre", source = "name")
    ResponseFranquicia requestToFranquicia(FranchiseData franchiseData);


    @Mapping(target = "name", source = "nombre")
    BranchData requestToBranchData(Sucursal sucursal);

    @Mapping(target = "sucursalId", source = "branchId")
    @Mapping(target = "nombre", source = "name")
    @Mapping(target = "franquiciaId", source = "franchiseId")
    ResponseSucursal responseToSucursal(BranchData branchData);

    @Mapping(target = "name", source = "nombre")
    ProductData requestToProductData(Producto producto);

    @Mapping(target = "productoId", source = "branchProductData.product")
    @Mapping(target = "nombre", source = "name")
    @Mapping(target = "stock", source = "branchProductData.stock")
    ResponseProducto responseToProducto(BranchProductData branchProductData, String name);


    @Mapping(target = "productos", source = "responseProductStock.products")
    ResponseProductoSucursal responseToProductoStock(ResponseProductStock responseProductStock);
}


