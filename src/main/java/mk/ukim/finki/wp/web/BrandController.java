package mk.ukim.finki.wp.web;

import mk.ukim.finki.wp.model.Brand;
import mk.ukim.finki.wp.service.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/admin/brands")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String getBrands(Model model) {
        return findPaginated(1, "id", "asc", "", model);
    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                @RequestParam(defaultValue = "id") String sortField,
                                @RequestParam(defaultValue = "asc") String sortDir,
                                @RequestParam(required = false) String searchName,
                                Model model) {
        int pageSize = 5;
        Page<Brand> page = brandService.listBrandsByName(pageNo, pageSize, sortField, sortDir, searchName);
        List<Brand> brands = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());

        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("searchName", searchName);

        model.addAttribute("brands", brands);
        model.addAttribute("bodyContent", "brands");
        return "master-template";
    }

    @GetMapping("/add-brands")
    public String getAddBrands(Model model) {
        model.addAttribute("bodyContent", "brands-form");
        return "master-template";
    }

    @GetMapping("/edit-brands/{id}")
    public String getEditBrands(@PathVariable Long id, Model model) {
        Brand brand = this.brandService.findById(id).get();
        model.addAttribute("brand", brand);
        model.addAttribute("bodyContent", "brands-form");
        return "master-template";
    }

    @PostMapping("/add")
    public String create(@RequestParam String brandName,
                         @RequestParam String brandLogo) {
        this.brandService.create(brandName, brandLogo);
        return "redirect:/admin/brands";
    }

    @PostMapping("/add/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String brandName,
                         @RequestParam String brandLogo) {
        this.brandService.update(id, brandName, brandLogo);
        return "redirect:/admin/brands";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.brandService.delete(id);
        return "redirect:/admin/brands";
    }
}
