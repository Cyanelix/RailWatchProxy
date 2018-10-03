package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
    List<Schedule> findByStateIs(ScheduleState scheduleState);
    List<Schedule> findByUser(User user);
}
