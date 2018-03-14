package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.ScheduleEntity;
import com.cyanelix.railwatch.entity.UserEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ScheduleRepositoryIT {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        userRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    public void enabledAndDisabledSchedule_getByStateEnabled_returnsEnabled() {
        // Given...
        UserEntity user = new UserEntity(UserId.generate().get(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        ScheduleEntity enabledSchedule = new ScheduleEntity(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);
        ScheduleEntity disabledSchedule = new ScheduleEntity(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user);

        scheduleRepository.save(Arrays.asList(enabledSchedule, disabledSchedule));

        // When...
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByStateIs(ScheduleState.ENABLED);

        // Then...
        assertThat(scheduleEntities).containsExactly(enabledSchedule);
    }

    @Test
    public void enabledAndDisabledSchedule_getByStateDisabled_returnsDisabled() {
        // Given...
        UserEntity user = new UserEntity(UserId.generate().get(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        ScheduleEntity enabledSchedule = new ScheduleEntity(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);
        ScheduleEntity disabledSchedule = new ScheduleEntity(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user);

        scheduleRepository.save(Arrays.asList(enabledSchedule, disabledSchedule));

        // When...
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByStateIs(ScheduleState.DISABLED);

        // Then...
        assertThat(scheduleEntities).containsExactly(disabledSchedule);
    }

    @Test
    public void schedulesWithDifferentUsers_getByUser_returnsCorrectSchedule() {
        // Given...
        UserEntity user1 = new UserEntity(UserId.generate().get(), "notification-target", UserState.ENABLED);
        UserEntity user2 = new UserEntity(UserId.generate().get(), "notification-target-2", UserState.ENABLED);
        userRepository.save(Arrays.asList(user1, user2));

        ScheduleEntity schedule1 = new ScheduleEntity(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user1);
        ScheduleEntity schedule2 = new ScheduleEntity(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user2);

        scheduleRepository.save(Arrays.asList(schedule1, schedule2));

        // When...
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByUser(user1);

        // Then...
        assertThat(scheduleEntities).containsExactly(schedule1);
    }

    @Test
    public void singleSchedule_getByNonExistentUser_throwsException() {
        // Given...
        UserEntity user = new UserEntity(UserId.generate().get(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        ScheduleEntity schedule = new ScheduleEntity(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);

        scheduleRepository.save(schedule);

        UserEntity userToSearchBy = new UserEntity(UserId.generate().get(), "different-target", UserState.ENABLED);

        expectedException.expect(MappingException.class);
        expectedException.expectMessage("Cannot create a reference to an object with a NULL id.");

        // When...
        scheduleRepository.findByUser(userToSearchBy);

        // Then...
        // (exception expected)
    }
}
