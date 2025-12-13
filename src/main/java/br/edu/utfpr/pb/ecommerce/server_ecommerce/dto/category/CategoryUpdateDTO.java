package br.edu.utfpr.pb.ecommerce.server_ecommerce.dto.category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateDTO {

    @Size(min = 4, max = 25)
    private String name;
}
