package ru.practicum.shareit.request.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.baseclient.BaseClient;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl,
                             RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> createItemRequest(long userId, ItemRequestCreateDTO itemRequestCreateDTO) {
        String path = "";
        return post(path, userId, null,itemRequestCreateDTO);
    }

    public ResponseEntity<Object> getUserRequests(long userId) {
        String path = "";
        return get(path, userId,null);
    }

    public ResponseEntity<Object> getAllRequestsExceptUser(long userId) {
        String path = "/all";
        return get(path, userId,null);
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> getRequestById(long userId, long requestId) {
        String path = String.format("/%d", requestId);
        return get(path, userId,null);
    }
}
