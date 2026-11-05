package org.azotiques.sonantv1.messaging;

public interface MessagingApi {
    SendMessageResponse sendMessage(SendMessageRequest messageRequest);
}
