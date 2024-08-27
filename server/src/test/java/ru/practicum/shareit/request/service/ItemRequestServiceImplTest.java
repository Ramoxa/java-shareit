package ru.practicum.shareit.request.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.IdNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestCreateDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTO;
import ru.practicum.shareit.request.dto.ItemRequestDTOWithAnswers;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserCreateDTO;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ItemRequestServiceImplTest {

    private final ItemRequestServiceImpl itemRequestService;
    private final UserServiceImpl userService;

    private final EntityManager em;


    @Test
    @DisplayName("create correct item")
    void createItemRequest() {
        String description = "some description";
        UserCreateDTO userCreateDTO = new UserCreateDTO("Anna", "anna@mail.ru");
        long userId = userService.createUser(userCreateDTO).getId();

        ItemRequestCreateDTO itemRequestCreateDTO = new ItemRequestCreateDTO(description);
        ItemRequestDTO returnedDTO = itemRequestService.createItemRequest(userId, itemRequestCreateDTO);
        TypedQuery<ItemRequest> getQuery = em.createQuery("SELECT ir FROM ItemRequest AS ir WHERE ir.description=:description",
                ItemRequest.class).setParameter("description", description);
        ItemRequest savedRequest = getQuery.getSingleResult();
        assertEquals(userId, savedRequest.getRequestor().getId());
        assertEquals(description, savedRequest.getDescription());
        assertEquals(description, returnedDTO.getDescription());
    }

    @Test
    void getIncorrectUserRequests() {
        assertThrows(IdNotFoundException.class, () -> itemRequestService.getUserRequests(1L));
    }

    @Test
    void getUserRequests() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("Anna", "anna@mail.ru");
        long userId = userService.createUser(userCreateDTO).getId();
        List<ItemRequestDTOWithAnswers> answeres = itemRequestService.getUserRequests(userId);
        assertEquals(0, answeres.size());
    }

    @Test
    void getAllRequestsExceptIncorrectUser() {
        assertThrows(IdNotFoundException.class, () -> itemRequestService.getAllRequestsExceptUser(1L));
    }

    @Test
    void getRequestById() {
        UserCreateDTO userCreateDTO = new UserCreateDTO("Anna", "anna@mail.ru");
        long userId = userService.createUser(userCreateDTO).getId();
        assertThrows(IdNotFoundException.class, () -> itemRequestService.getRequestById(1L));
    }
}