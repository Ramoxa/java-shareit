package ru.practicum.shareit.bookingTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDtoTests {
    private final JacksonTester<BookingDto> json;

    @Test
    void testSerialize() throws Exception {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .booker(UserDto.builder()
                        .id(1L)
                        .name("TestUserName")
                        .email("TestUser@Email.com")
                        .build())
                .item(ItemDto.builder()
                        .id(1L)
                        .requestId(1L)
                        .available(true)
                        .description("TestItemDescription")
                        .name("TestItemName")
                        .build())
                .status("APPROVED")
                .build();

        JsonContent<BookingDto> result = json.write(bookingDto);

        assertThat(result)
                .hasJsonPath("$.id")
                .hasJsonPath("$.start")
                .hasJsonPath("$.end")
                .hasJsonPath("$.status")
                .hasJsonPath("$.booker.id")
                .hasJsonPath("$.booker.name")
                .hasJsonPath("$.booker.email")
                .hasJsonPath("$.item.id")
                .hasJsonPath("$.item.requestId")
                .hasJsonPath("$.item.available")
                .hasJsonPath("$.item.description")
                .hasJsonPath("$.item.name");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(item_id -> assertThat(item_id.longValue()).isEqualTo(bookingDto.getId()));

        assertThat(result).extractingJsonPathStringValue("$.start")
                .satisfies(created -> assertThat(created).isNotNull());

        assertThat(result).extractingJsonPathStringValue("$.end")
                .satisfies(created -> assertThat(created).isNotNull());

        assertThat(result).extractingJsonPathStringValue("$.status")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(bookingDto.getStatus()));

        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .satisfies(item_id -> assertThat(item_id.longValue()).isEqualTo(bookingDto.getBooker().getId()));

        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getBooker().getName()));

        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getBooker().getEmail()));

        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(bookingDto.getItem().getId()));

        assertThat(result).extractingJsonPathNumberValue("$.item.requestId")
                .satisfies(requestId -> assertThat(requestId.longValue()).isEqualTo(bookingDto.getItem().getRequestId()));

        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .satisfies(item_name -> assertThat(item_name).isEqualTo(bookingDto.getItem().getName()));

        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .satisfies(item_description -> assertThat(item_description).isEqualTo(bookingDto.getItem().getDescription()));

        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .satisfies(item_available -> assertThat(item_available).isEqualTo(bookingDto.getItem().getAvailable()));
    }
}