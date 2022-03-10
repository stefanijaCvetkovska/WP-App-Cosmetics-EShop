package mk.ukim.finki.wp.repository;

import mk.ukim.finki.wp.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Page<Brand> findAllByNameLikeIgnoreCase(String name, Pageable pageable);
}
