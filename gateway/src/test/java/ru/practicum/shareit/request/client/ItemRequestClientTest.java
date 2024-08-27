package ru.practicum.shareit.request.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ItemRequestClientTest {

    @Spy
    private ItemRequestClient itemRequestClient =
            new ItemRequestClient("http://localhost:9090", new RestTemplateBuilder());

    @Test
    void createItemRequest() {
        String path = "";
        long userId = 1231L;
        String description = "bla-bla";
        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO();
        assertThrows(Throwable.class, () -> itemRequestClient.createItemRequest(userId, itemRequestCreateDTO));
        assertThrows(Throwable.class, () -> itemRequestClient.post(path, userId, null, itemRequestCreateDTO));
    }

    @Test
    void getUserRequests() {
        long userId = 1231L;
        String path = "";
        assertThrows(Throwable.class, () -> itemRequestClient.getUserRequests(userId));
        assertThrows(Throwable.class, () -> itemRequestClient.get(path, userId, null));
    }

    @Test
    void getAllRequestsExceptUser() {
        long userId = 1231L;
        String path = "/all";
        assertThrows(Throwable.class, () -> itemRequestClient.getAllRequestsExceptUser(userId));
        assertThrows(Throwable.class, () -> itemRequestClient.get(path, userId, null));
    }

    @Test
    void getRequestById() {
        long userId = 1231L;
        long requestId = 1231L;
        String path = String.format("/%d", requestId);
        assertThrows(Throwable.class, () -> itemRequestClient.getAllRequestsExceptUser(requestId));
        assertThrows(Throwable.class, () -> itemRequestClient.get(path, userId, null));
    }
}