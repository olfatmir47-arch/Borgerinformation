package Service;

import DAOs.CategoryDAO;
import DTO.Category.CategoryDTO;
import DTO.Category.CreateCategoryDTO;
import entities.Category;
import entities.User;

public class CategoryService {
    private CategoryDAO categoryDAO;

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
