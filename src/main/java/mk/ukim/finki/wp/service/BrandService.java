package mk.ukim.finki.wp.service;

import mk.ukim.finki.wp.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> listAll();

    Optional<Brand> findById(Long id);

    Optional<Brand> findByName(String name);

    Brand create(String name, String logo);

    Brand update(Long id, String name, String logo);

    Brand delete(Long id);

    Page<Brand> listBrandsByName(int pageNo, int pageSize, String sortField, String sortDirection, String name);

    Page<Brand> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
