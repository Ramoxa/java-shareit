package ru.practicum.shareit.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.baseclient.BaseClient;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> createItem(long userId, ItemCreateDTO itemCreateDTO) {
        String path = "";
        return post(path, userId, null,itemCreateDTO);
    }

    public ResponseEntity<Object> updateItem(long userId, long itemId, ItemUpdateDTO itemUpdateDTO) {
        String path = String.format("/%d", itemId);
        return patch(path, userId, null,itemUpdateDTO);
    }

    public ResponseEntity<Object> getItem(long userId, long itemId) {
        String path = String.format("/%d", itemId);
        return get(path, userId, null);
    }

    public ResponseEntity<Object> getAllItems(long userId) {
        String path = "";
        return get(path, userId,null);
    }

    public ResponseEntity<Object> searchItems(String text) {
        String path = "/search";
        return get(path,null, null);
    }

    public ResponseEntity<Object> addComment(long userId, long itemId, CommentCreateDTO comment) {
        String path = String.format("/%d/comment", itemId);
        return post(path, userId, null, comment);
    }
}
