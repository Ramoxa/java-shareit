package ru.practicum.shareit.item.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import ru.practicum.shareit.item.dto.comment.CommentCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemCreateDTO;
import ru.practicum.shareit.item.dto.item.ItemUpdateDTO;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ItemClientTest {

    @Spy
    private ItemClient itemClient =
            new ItemClient("http://localhost:9090", new RestTemplateBuilder());


    @Test
    void createItem() {
        long userId = 112L;
        long id = 10L;
        String name = "name";
        String description = "description";
        boolean available = true;
        ItemCreateDTO itemCreateDTO = new ItemCreateDTO(name, description, available, 10L);
        String path = "";
        assertThrows(Throwable.class, () -> itemClient.createItem(userId, itemCreateDTO));
        assertThrows(Throwable.class, () -> itemClient.post(path, userId, null, itemCreateDTO));
    }

    @Test
    void updateItem() {
        long userId = 100;
        long itemId = 212;
        String name = "ssss";
        String description = "sssaqdewds";
        boolean available = true;
        String path = String.format("/%d", itemId);
        ItemUpdateDTO itemUpdateDTO = new ItemUpdateDTO(name, description, available);
        assertThrows(Throwable.class, () -> itemClient.updateItem(userId, itemId, itemUpdateDTO));
        assertThrows(Throwable.class, () -> itemClient.post(path, userId, null, itemUpdateDTO));
    }

    @Test
    void getItem() {
        long userId = 100;
        long itemId = 212;
        String path = String.format("/%d", itemId);
        assertThrows(Throwable.class, () -> itemClient.getItem(userId, itemId));
        assertThrows(Throwable.class, () -> itemClient.get(path, userId, null));
    }

    @Test
    void getAllItems() {
        long userId = 100;
        long itemId = 212;
        String path = "";
        assertThrows(Throwable.class, () -> itemClient.getAllItems(userId));
        assertThrows(Throwable.class, () -> itemClient.get(path, userId, null));
    }

    @Test
    void searchItems() {
        String text = "sss";
        String path = "/search";
        assertThrows(Throwable.class, () -> itemClient.searchItems(text));
        assertThrows(Throwable.class, () -> itemClient.get(path, null, null));
    }

    @Test
    void addComment() {
        long userId = 21321;
        long itemId = 212;
        String text = "saxsdxa";
        CommentCreateDTO comment = new CommentCreateDTO(text);
        String path = String.format("/%d/comment", itemId);
        assertThrows(Throwable.class, () -> itemClient.addComment(userId, itemId, comment));
        assertThrows(Throwable.class, () -> itemClient.post(path, userId, null, comment));
    }
}