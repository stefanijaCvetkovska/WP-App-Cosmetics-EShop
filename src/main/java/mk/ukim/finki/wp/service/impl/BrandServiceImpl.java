package mk.ukim.finki.wp.service.impl;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.repository.BrandRepository;
import mk.ukim.finki.wp.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {
        return this.brandRepository.findAll();
    }

    @Override
    public Optional<Brand> findById(Long id) {
        return this.brandRepository.findById(id);
    }

    @Override
    public Optional<Brand> findByName(String name) {
        return this.brandRepository.findByName(name);
    }

    @Override
    public Brand create(String name, String logo) {
        Brand brand = new Brand(name, logo);
        return this.brandRepository.save(brand);
    }

    @Override
    public Brand update(Long id, String name, String logo) {
        Brand brand = this.findById(id).get();
        brand.setName(name);
        brand.setLogo(logo);
        return this.brandRepository.save(brand);
    }

    @Override
    public Brand delete(Long id) {
        Brand brand = this.findById(id).get();
        this.brandRepository.delete(brand);
        return brand;
    }

    @Override
    public Page<Brand> listBrandsByName(int pageNo, int pageSize, String sortField, String sortDirection, String name) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        String nameLike = '%' + name + '%';
        if (name != null) {
            return this.brandRepository.findAllByNameLikeIgnoreCase(nameLike, pageable);
        } else {
            return this.brandRepository.findAll(pageable);
        }
    }

    @Override
    public Page<Brand> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.brandRepository.findAll(pageable);
    }

}
