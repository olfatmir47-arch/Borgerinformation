package Service;

import DAOs.CategoryDAO;

import DTO.Category.CreateCategoryDTO;
import entities.Category;


public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category createCategory(CreateCategoryDTO dto){
        Category existing = categoryDAO.findByName(dto.getName());
        if (existing != null) {
            throw new IllegalArgumentException("Category already exists");
        }
        Category category = new Category();
        category.setName(dto.getName());
        Category result = categoryDAO.create(category);
        return result;
    }

    public void delete(int id){
        categoryDAO.delete(id);
    }
}
