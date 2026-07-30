package ac.za.mycput.service;

import ac.za.mycput.domain.HairCareProduct;

import java.util.List;

public interface IHairCareProductService extends IService<HairCareProduct, Long> {

    List<HairCareProduct> findByBrand(String brand);

    List<HairCareProduct> searchByName(String keyword);

    List<HairCareProduct> findInStock();
}
