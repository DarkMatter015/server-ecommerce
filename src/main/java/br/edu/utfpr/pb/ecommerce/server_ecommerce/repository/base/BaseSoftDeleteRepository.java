package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository.base;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseSoftDeleteEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseSoftDeleteRepository<T extends BaseSoftDeleteEntity, ID extends Serializable> extends BaseRepository<T, ID> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} e SET e.deletedAt = CURRENT_TIMESTAMP WHERE e.id = :id")
    void softDeleteById(@Param("id") ID id);
}
