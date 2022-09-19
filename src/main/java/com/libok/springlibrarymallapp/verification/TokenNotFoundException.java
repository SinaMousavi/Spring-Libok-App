package com.libok.springlibrarymallapp.verification;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Brak takiego tokenu")
public class TokenNotFoundException extends RuntimeException {
}
