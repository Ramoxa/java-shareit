package ru.practicum.shareit.request.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class RequestDTOMapperTest {
    private final RequestDTOMapper requestDTOMapper;


    @Test
    void fromCreateDTO() {
        User user = new User(10, "ss", "sss");
        ItemRequestCreateDTO createDTO = new ItemRequestCreateDTO("ssss");
        ItemRequest result = RequestDTOMapper.fromCreateDTO(user, createDTO);
        assertEquals(user, result.getRequestor());
    }

    @Test
    void toDTO() {
        User user = new User(10, "ss", "sss");
        String description = "sssssss";
        ItemRequestCreateDTO createDTO = new ItemRequestCreateDTO(description);
        ItemRequest request = RequestDTOMapper.fromCreateDTO(user, createDTO);
        ItemRequestDTO dto = RequestDTOMapper.toDTO(request);
        assertEquals(description, dto.getDescription());
    }

    @Test
    void toDTOWithAnswers() {
        User user = new User(10, "ss", "sss");
        ItemRequestCreateDTO createDTO = new ItemRequestCreateDTO("ssss");
        ItemRequest request1 = RequestDTOMapper.fromCreateDTO(user, createDTO);
        User user2 = new User(102, "ssdss", "sdddss");
        ItemRequestCreateDTO createDTO2 = new ItemRequestCreateDTO("dddssss");
        ItemRequest request2 = RequestDTOMapper.fromCreateDTO(user2, createDTO2);
        List<ItemRequest> requests = List.of(request1, request2);
        List<ItemRequestDTOWithAnswers> answeres = requestDTOMapper.toDTOWithAnswers(requests);
        assertEquals(2, answeres.size());
    }

    @Test
    void testToDTOWithAnswers() {
        User user = new User(10, "ss", "sss");
        String description = "ssss";
        ItemRequestCreateDTO createDTO = new ItemRequestCreateDTO(description);
        ItemRequest request1 = RequestDTOMapper.fromCreateDTO(user, createDTO);
        ItemRequestDTOWithAnswers answer = requestDTOMapper.toDTOWithAnswers(request1);
        assertEquals(description, answer.getDescription());
    }
}