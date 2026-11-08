package org.azotiques.sonantv1.realtime.dtos;

import java.util.List;

public record ChatMessage(
        String message,
        List<String> attachments
) {
}