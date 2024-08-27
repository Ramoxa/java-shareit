package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;
import java.util.Optional;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Optional<ItemRequest> findById(long requestId);

    @Query("""
            SELECT ir
            FROM ItemRequest AS ir
            WHERE ir.requestor.id=:userId
            ORDER BY ir.created
            """)
    @EntityGraph(attributePaths = {"requestor"})
    List<ItemRequest> findAllByRequestor(long userId);

    @Query("""
            SELECT ir
            FROM ItemRequest AS ir
            WHERE ir.requestor.id<>:userId
            ORDER BY ir.created
            """)
    @EntityGraph(attributePaths = {"requestor"})
    List<ItemRequest> findAllExceptRequestor(long userId);
}
