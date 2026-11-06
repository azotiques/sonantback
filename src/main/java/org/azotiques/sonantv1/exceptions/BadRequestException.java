package org.azotiques.sonantv1.exceptions;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;

@StandardException
public abstract class BadRequestException extends ApiException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
