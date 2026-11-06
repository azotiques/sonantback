package org.azotiques.sonantv1.user.dtos;

import org.springframework.web.multipart.MultipartFile;

public record EditUserRequest(
        MultipartFile avatar
) {
}
