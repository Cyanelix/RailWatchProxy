package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.ScheduleState;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ScheduleRepository extends MongoRepository<ScheduleEntity, String> {
    List<ScheduleEntity> findByStateIs(ScheduleState scheduleState);
    List<ScheduleEntity> findByUser(UserEntity user);
}
