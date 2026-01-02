package br.edu.utfpr.pb.ecommerce.server_ecommerce.service.impl.CRUD;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base.BaseRepository;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.service.AuthService;
import br.edu.utfpr.pb.ecommerce.server_ecommerce.specification.BaseSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;

public abstract class BaseSoftDeleteResponseServiceImpl<T extends BaseSoftDeleteEntity, ID extends Serializable>
        extends BaseResponseServiceImpl<T, ID> {

    public BaseSoftDeleteResponseServiceImpl(BaseRepository<T, ID> repository,
                                             AuthService authService) {
        super(repository, authService);
    }

    @Override
    protected Specification<T> getDefaultVisibilitySpecification() {
        // Implementação específica: Esconde deletados para público geral
        return BaseSpecification.isNotDeleted();
    }
}
