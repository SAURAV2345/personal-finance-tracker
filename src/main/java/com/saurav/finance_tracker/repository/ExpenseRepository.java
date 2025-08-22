package com.saurav.finance_tracker.repository;

import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserId(Long id);

    List<Expense> findByUserIdOrderByIdAsc(Long userId, Pageable pageable);

    List<Expense> findByUserIdAndIdGreaterThanOrderByIdAsc(Long userId, Long afterId,Pageable pageable);
    @Query("SELECT e FROM Expense e " +
            "WHERE e.user.id = :userId " +
            "AND e.date >= :startDate " +
            "AND e.date <= :endDate")
    List<Expense> findExpensesForMonth(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
