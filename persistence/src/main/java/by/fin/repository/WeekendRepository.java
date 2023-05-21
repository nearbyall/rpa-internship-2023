package by.fin.repository;


import by.fin.repository.entity.Weekend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeekendRepository extends JpaRepository<Weekend, Long> {

    Optional<Weekend> findByCalendarDate(Date date);

    @Query("SELECT w FROM Weekend w WHERE w.calendarDate BETWEEN :startDate AND :endDate")
    List<Weekend> findAllByDateBetween(Date startDate, Date endDate);

}