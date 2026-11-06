package org.azotiques.sonantv1.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageApi {

    String upload(MultipartFile file);

    String getUrl(String key);

    void delete(String key);

}
