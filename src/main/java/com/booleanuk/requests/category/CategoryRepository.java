package com.booleanuk.requests.category;

import com.booleanuk.requests.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByType (ECategory type);
    List<Category> findByUser (User user);

    List<Category> findByUserAndType( User user, ECategory type);
}
