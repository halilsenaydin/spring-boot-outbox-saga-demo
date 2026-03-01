package io.github.halilsenaydin.shared.dataAccess.abstracts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OutboxRepository<T> extends JpaRepository<T, Long> {
    List<T> findByProcessedFalse();
    Page<T> findByProcessedFalse(Pageable pageable);
}
