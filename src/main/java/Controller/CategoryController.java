package Controller;

import DTO.Category.CategoryDTO;
import DTO.Category.CreateCategoryDTO;
import Service.CategoryService;
import entities.Category;
import io.javalin.http.Context;

public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void createCategory(Context ctx) {
        CreateCategoryDTO dto = ctx.bodyAsClass(CreateCategoryDTO.class);
        Category category = categoryService.createCategory(dto);

        CategoryDTO response = new CategoryDTO();
        response.setId(category.getId());
        response.setName(category.getName());

        ctx.json(response);
    }
}
