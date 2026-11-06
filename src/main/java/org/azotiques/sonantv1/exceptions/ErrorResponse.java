package org.azotiques.sonantv1.exceptions;

import java.time.Instant;

public record ErrorResponse(
        int status,
        String error,
        String message,
        String path,
        Instant timestamp
) {
}
