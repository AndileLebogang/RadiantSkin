package ac.za.mycput.service;

import ac.za.mycput.domain.SkinCareProduct;

import java.util.List;

public interface ISkinCareProductService extends IService<SkinCareProduct, Long> {

    List<SkinCareProduct> findByBrand(String brand);

    List<SkinCareProduct> searchByName(String keyword);

    List<SkinCareProduct> findInStock();
}
