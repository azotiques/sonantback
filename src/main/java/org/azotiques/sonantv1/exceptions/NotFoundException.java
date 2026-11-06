package org.azotiques.sonantv1.exceptions;

import lombok.experimental.StandardException;
import org.springframework.http.HttpStatus;

@StandardException
public abstract class NotFoundException extends ApiException {
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
