package ac.za.mycput.service;

import ac.za.mycput.domain.BodyCareProduct;

import java.util.List;

public interface IBodyCareProductService extends IService<BodyCareProduct, Long> {

    List<BodyCareProduct> findByBrand(String brand);

    List<BodyCareProduct> searchByName(String keyword);

    List<BodyCareProduct> findInStock();
}
