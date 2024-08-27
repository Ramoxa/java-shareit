package ru.practicum.shareit.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.baseclient.BaseClient;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.dto.UserUpdateDTO;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build()
        );
    }

    public ResponseEntity<Object> createUser(UserCreateDTO user) {
        String path = "";
        return post(path, null, null, user);
    }

    public ResponseEntity<Object> updateUser(Long userId, UserUpdateDTO user) {
        String path = String.format("/%d", userId);
        return patch(path, null, null, user);
    }

    public void deleteUser(Long userId) {
        String path = String.format("/%d", userId);
        delete(path);
    }

    public ResponseEntity<Object> getUser(Long userId) {
        String path = String.format("/%d", userId);
        return get(path, null, null);
    }

    public ResponseEntity<Object> getAllUsers() {
        String path = "";
        return get(path, null, null);
    }
}
