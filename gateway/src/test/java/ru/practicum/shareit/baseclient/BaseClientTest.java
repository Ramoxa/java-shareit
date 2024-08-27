package ru.practicum.shareit.baseclient;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BaseClientTest {

    @Mock
    private RestTemplate rest;

    @InjectMocks
    private BaseClient baseClient;

    @Test
    public void getTest() {
        Object body = new Object();
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.CREATED);
        when(rest.exchange(
                anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(response);
        assertEquals(response, baseClient.get(null, null, null));
    }

    @Test
    public void postTest() {
        Object body = new Object();
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.CREATED);
        when(rest.exchange(
                anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(response);
        assertEquals(response, baseClient.post(null, null, null, null));
    }

    @Test
    public void patchTest() {
        Object body = new Object();
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.CREATED);
        when(rest.exchange(
                anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(response);
        assertEquals(response, baseClient.patch(null, null, null, null));
    }

    @Test
    public void deleteTest() {
        Object body = new Object();
        ResponseEntity<Object> response = new ResponseEntity<>(body, HttpStatus.CREATED);
        when(rest.exchange(
                anyString(),
                ArgumentMatchers.any(HttpMethod.class),
                ArgumentMatchers.any(),
                ArgumentMatchers.<Class<Object>>any()))
                .thenReturn(response);
        assertDoesNotThrow(() -> baseClient.delete(null));
    }

    @Test
    public void getTestErrorTest() {
        assertThrows(Throwable.class, () -> baseClient.get(null, null, null));
    }

    @Test
    public void postTestErrorTest() {
        assertThrows(Throwable.class, () -> baseClient.post(null, null, null, null));
    }

    @Test
    public void patchTestErrorTest() {
        assertThrows(Throwable.class, () -> baseClient.patch(null, null, null, null));
    }

    @Test
    public void deleteTestErrorTest() {
        assertThrows(Throwable.class, () -> baseClient.delete(null));
    }
}