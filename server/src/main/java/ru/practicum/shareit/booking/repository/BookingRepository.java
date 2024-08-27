package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getAllBookingsOfUser(long userId);

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.end<CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getPastBookingsOfUser(long userId);

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.start>CURRENT_TIMESTAMP
            AND b.end<CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getCurrentBookingsOfUser(long userId);

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.start>CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getFutureBookingsOfUser(long userId);

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.status='REJECTED'
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getRejectedBookingsOfUser(long userId);

    @Query("""
            SELECT b FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.status='WAITING'
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getWaitingBookingsOfUser(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getAllBookingsForUserItems(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND b.end<CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getPastBookingsForUserItems(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND b.start>CURRENT_TIMESTAMP
            AND b.end<CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getCurrentBookingsForUserItems(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND b.start>CURRENT_TIMESTAMP
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getFutureBookingsForUserItems(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND b.status='REJECTED'
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getRejectedBookingsForUserItems(long userId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND b.status='WAITING'
            ORDER BY b.start ASC
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    List<Booking> getWaitingBookingsForUserItems(long userId);

    @Query("""
            SELECT CASE WHEN COUNT(b)> 0 THEN TRUE ELSE FALSE END
            FROM Booking AS b
            WHERE b.booker.id=:userId
            AND b.item.id=:itemId
            AND b.end<CURRENT_TIMESTAMP
            """)
    boolean isUserBookedItem(long userId, long itemId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND i.id = :itemId
            AND b.end < CURRENT_TIMESTAMP
            ORDER BY b.end DESC
            LIMIT 1
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    Optional<Booking> getLastBookingForItemOwnedByUser(long userId, long itemId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND i.id IN :itemIds
            AND b.end < CURRENT_TIMESTAMP
            """)
    @EntityGraph(attributePaths = {"item"})
    List<Booking> getAllLastBookings(long userId, Collection<Long> itemIds);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND i.id = :itemId
            AND b.start > CURRENT_TIMESTAMP
            AND b.status != 'REJECTED'
            ORDER BY b.start ASC
            LIMIT 1
            """)
    @EntityGraph(attributePaths = {"item", "booker"})
    Optional<Booking> getNextBookingForItemOwnedByUser(long userId, long itemId);

    @Query("""
            SELECT b
            FROM Booking AS b
            JOIN FETCH b.item AS i
            WHERE i.owner.id=:userId
            AND i.id IN :itemIds
            AND b.start > CURRENT_TIMESTAMP
            AND b.status != 'REJECTED'
            """)
    @EntityGraph(attributePaths = {"item"})
    List<Booking> getAllNextBooking(long userId, Collection<Long> itemIds);
}
