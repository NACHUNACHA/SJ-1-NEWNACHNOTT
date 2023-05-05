package sit.int221.sas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sit.int221.sas.entities.Category;
import sit.int221.sas.repositories.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    private static final String DEFAULT_CATEGORY_NAME = "ทั่วไป";
    @Autowired
    private CategoryRepository repository;

    public List<Category> findAll() { return repository.findAll(); }
    public Category findDefaultCategory() {
        return repository.findByCategoryName(DEFAULT_CATEGORY_NAME);
    }

}
