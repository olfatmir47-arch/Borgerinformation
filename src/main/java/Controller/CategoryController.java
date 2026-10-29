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

        CategoryDTO response = new CategoryDTO(category);

        ctx.json(response);
    }

    public void deleteCategory(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        categoryService.delete(id);
        ctx.status(204);
    }
}
