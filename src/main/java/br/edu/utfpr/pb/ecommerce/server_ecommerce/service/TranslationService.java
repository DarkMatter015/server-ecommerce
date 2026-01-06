package br.edu.utfpr.pb.ecommerce.server_ecommerce.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslationService {

    private final MessageSource messageSource;

//    Para Consumers (Locale explícito)
    public String getMessageLocale(String key, Locale locale, @Nullable Object... args) {
        try {
            return messageSource.getMessage(key, args, locale);
        } catch (NoSuchMessageException e) {
            log.warn("Missing translation key: {}", key);
            return key; // Retorna a chave como fallback seguro
        }
    }

//    Para Controllers/Services Web (Locale implícito via Context)
    public String getMessageContext(String key, @Nullable Object... args) {
        return getMessageLocale(key, LocaleContextHolder.getLocale(), args);
    }
}
