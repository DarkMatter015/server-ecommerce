package br.edu.utfpr.pb.ecommerce.server_ecommerce.exception.base;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // Generic
    ENTITY_NOT_FOUND("entity.not.found", 404),
    ENTITY_NOT_FOUND_WITH("entity.not.found.with", 404),
    SECURITY_PERMISSION_DENIED("security.permission.update.denied", 403),

    // User
    USER_PERMISSION_MODIFY_DENIED("user.permission.modify.denied", 403),
    USER_ROLE_REQUIRED("user.role.required", 400),
    USER_PERMISSION_ROLES_UPDATE_DENIED("user.permission.roles.update.denied", 403),
    USER_CPF_IN_USE("user.cpf.in.use", 400),
    USER_PERMISSION_CREATE_ROLES_DENIED("user.permission.create.roles.denied", 403),
    USER_PASSWORD_CURRENT_INCORRECT("user.password.current.incorrect", 400),
    USER_PASSWORD_CONFIRM_MISMATCH("user.password.confirm.mismatch", 400),
    PASSWORD_SAME("error.password.same", 400),
    PASSWORD_MISMATCH("password.mismatch", 400),
    TOKEN_INVALID("token.invalid", 400),
    TOKEN_EXPIRED("token.expired", 400),

    // Address
    ADDRESS_PERMISSION_MODIFY_DENIED("address.permission.modify.denied", 403),
    ADDRESS_USER_ACTIVATE_REQUIRED("address.user.activate.required", 400),
    CEP_NOT_FOUND_OR_INVALID("cep.not.found.or.invalid", 400),

    // Product
    PRODUCT_NOT_ACTIVE("product.not.active", 400),
    PRODUCT_QUANTITY_INVALID("product.quantity.invalid", 400),
    CATEGORY_ACTIVATE_REQUIRED("category.activate.required", 400),
    PRODUCT_DISCREPANCY("product.discrepancy", 400),

    // Order
    ORDER_PERMISSION_MODIFY_DENIED("order.permission.modify.denied", 403),
    SHIPMENT_DATA_INVALID("shipment.data.invalid", 400),
    SHIPMENT_INVALID("shipment.invalid", 400),


    // Alert Product
    ALERT_PRODUCT_ALREADY_SENT("alert.product.already.sent", 400),
    ALERT_PRODUCT_FAILED("alert.product.failed", 400),
    ALERT_PRODUCT_PENDING_EXISTS("alert.product.pending.exists", 400),

    // Email
    EMAIL_ERROR_SEND("email.error.send", 500),

    // Auth
    AUTHENTICATED_USER_NOT_FOUND("authenticated.user.not.found", 404);

    private final String messageKey;
    private final int status;

    ErrorCode(String messageKey, int status) {
        this.messageKey = messageKey;
        this.status = status;
    }
}
