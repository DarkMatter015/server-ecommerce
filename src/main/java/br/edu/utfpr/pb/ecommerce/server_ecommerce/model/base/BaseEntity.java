package br.edu.utfpr.pb.ecommerce.server_ecommerce.model.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE #{#entityName} SET active = false WHERE id = ?")
@FilterDef(name = "activeFilter", parameters = @ParamDef(name = "isActive", type = Boolean.class))
@Filter(name = "activeFilter", condition = "active = :isActive")
public abstract class BaseEntity implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;
}
