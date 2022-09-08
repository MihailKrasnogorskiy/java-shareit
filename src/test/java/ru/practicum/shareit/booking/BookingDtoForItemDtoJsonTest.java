package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * тестовый класс BookingDtoForItemDto
 */
@JsonTest
public class BookingDtoForItemDtoJsonTest {
    @Autowired
    private JacksonTester<BookingDtoForItemDto> json;

    @Test
    void test25_BookingDtoForItemDto() throws Exception {

        LocalDateTime start = LocalDateTime.now().withNano(0).plusDays(1);
        LocalDateTime end = start.plusDays(1);
        BookingDtoForItemDto dto = BookingDtoForItemDto.builder()
                .id(1L)
                .bookerId(1L)
                .itemId(1L)
                .start(start)
                .end(end)
                .status(BookingStatus.APPROVED)
                .build();
        JsonContent<BookingDtoForItemDto> result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo(start.toString());
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo(end.toString());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");
    }
}
