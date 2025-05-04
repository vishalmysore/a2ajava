package io.github.vishalmysore.a2a.repository;

import io.github.vishalmysore.a2a.domain.Task;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAll();
    // Additional query methods can be defined here if needed


}