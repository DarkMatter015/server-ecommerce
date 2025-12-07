package br.edu.utfpr.pb.ecommerce.server_ecommerce.repository;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * Reativa uma entidade, definindo seu campo 'active' para true.
     */
    @Modifying
    @Query("UPDATE #{#entityName} e SET e.active = true WHERE e.id = :id")
    void activate(@Param("id") ID id);
}
