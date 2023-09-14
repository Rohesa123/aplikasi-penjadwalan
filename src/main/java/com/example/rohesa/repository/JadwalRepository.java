package com.example.rohesa.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.example.rohesa.model.Jadwal;

@Repository
public interface JadwalRepository extends JpaRepository<Jadwal, Long> {
    @Query(value = "select * from jadwal where status = '1' and execution_type = 'scheduled' and execution_at >= cast(:date as timestamp)", nativeQuery = true)
    List<Jadwal> getGreaterThanDate(String date);

    @Query(value = "select * from jadwal where status = '1' and execution_type = 'scheduled' and execution_at <= cast(:date as timestamp)", nativeQuery = true)
    List<Jadwal> getLessThanDate(String date);

    Optional<Jadwal> findByUid(String uid);
}
