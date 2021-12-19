package com.vladimish.consulter.timetable.db;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Long> {
    List<Record> findAllByClient(String client);

    List<Record> findAllByEmployee(String employee);
}
