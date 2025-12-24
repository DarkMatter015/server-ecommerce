package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.category.ICategory;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category.CategoryUpdateDTO;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.Category;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD.ICRUD.IBaseSoftDeleteRequestService;

public interface ICategoryRequestService extends IBaseSoftDeleteRequestService<Category, CategoryUpdateDTO, Long> {
}
