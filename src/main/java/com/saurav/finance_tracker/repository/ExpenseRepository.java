package com.saurav.finance_tracker.repository;

import com.saurav.finance_tracker.model.Expense;
import com.saurav.finance_tracker.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
    List<Expense> findByUser(User user);
    List<Expense> findByUserId(Long id);

    List<Expense> findByUserIdOrderByIdAsc(Long userId, Pageable pageable);

    List<Expense> findByUserIdAndIdGreaterThanOrderByIdAsc(Long userId, Long afterId,Pageable pageable);

}
