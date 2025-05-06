package io.github.vishalmysore.a2a.repository;

import io.github.vishalmysore.a2a.domain.Task;

import java.util.List;


import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "a2a.persistence", havingValue = "database")
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAll();
    // Additional query methods can be defined here if needed


}