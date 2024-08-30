package co.com.franchise.model.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Operation {
    ADD_FRANCHISIE("/api/franchises"),
    ADD_BRANCH("/api/franchises/{franchiseId}/branches"),
    ADD_PRODUCT("/api/franchises/branches/{branchId}/products"),
    DELETE_PRODUCT("/api/franchises/branches/{branchId}/products/{productId}"),
    UPDATE_STOCK("/api/franchises/branches/{branchId}/products/{productId}/stock"),
    GET_PRODUCT_MOST_STOCK("/api/franchises/{franchiseId}/branches/products/most-stock");
    private final String path;
}
