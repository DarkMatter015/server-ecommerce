package br.edu.utfpr.pb.ecommerce.server_ecommerce.util;

import br.edu.utfpr.pb.ecommerce.server_ecommerce.model.interfaces.Identifiable;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public final class ControllerUtils {
    private ControllerUtils() {}

    public static URI createUri(Identifiable<?> source){
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(source.getId())
                .toUri();
    }
}
