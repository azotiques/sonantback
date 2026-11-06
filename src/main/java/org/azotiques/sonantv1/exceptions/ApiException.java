package org.azotiques.sonantv1.exceptions;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;

@StandardException
public abstract class ApiException extends RuntimeException {
    public abstract HttpStatus getStatus();
}
