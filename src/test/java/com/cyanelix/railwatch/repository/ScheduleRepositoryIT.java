package com.cyanelix.railwatch.repository;

import com.cyanelix.railwatch.domain.*;
import com.cyanelix.railwatch.entity.Schedule;
import com.cyanelix.railwatch.entity.User;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mapping.MappingException;
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
        User user = new User(UserId.generate(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        Schedule enabledSchedule = new Schedule(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);
        Schedule disabledSchedule = new Schedule(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user);

        scheduleRepository.saveAll(Arrays.asList(enabledSchedule, disabledSchedule));

        // When...
        List<Schedule> scheduleEntities = scheduleRepository.findByStateIs(ScheduleState.ENABLED);

        // Then...
        assertThat(scheduleEntities).containsExactly(enabledSchedule);
    }

    @Test
    public void enabledAndDisabledSchedule_getByStateDisabled_returnsDisabled() {
        // Given...
        User user = new User(UserId.generate(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        Schedule enabledSchedule = new Schedule(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);
        Schedule disabledSchedule = new Schedule(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user);

        scheduleRepository.saveAll(Arrays.asList(enabledSchedule, disabledSchedule));

        // When...
        List<Schedule> scheduleEntities = scheduleRepository.findByStateIs(ScheduleState.DISABLED);

        // Then...
        assertThat(scheduleEntities).containsExactly(disabledSchedule);
    }

    @Test
    public void schedulesWithDifferentUsers_getByUser_returnsCorrectSchedule() {
        // Given...
        User user1 = new User(UserId.generate(), "notification-target", UserState.ENABLED);
        User user2 = new User(UserId.generate(), "notification-target-2", UserState.ENABLED);
        userRepository.saveAll(Arrays.asList(user1, user2));

        Schedule schedule1 = new Schedule(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user1);
        Schedule schedule2 = new Schedule(LocalTime.MIN, LocalTime.NOON, DayRange.of(DayOfWeek.MONDAY),
                Station.of("BAZ"), Station.of("FOB"), ScheduleState.DISABLED, user2);

        scheduleRepository.saveAll(Arrays.asList(schedule1, schedule2));

        // When...
        List<Schedule> scheduleEntities = scheduleRepository.findByUser(user1);

        // Then...
        assertThat(scheduleEntities).containsExactly(schedule1);
    }

    @Test
    public void singleSchedule_getByNonExistentUser_throwsException() {
        // Given...
        User user = new User(UserId.generate(), "notification-target", UserState.ENABLED);
        userRepository.save(user);

        Schedule schedule = new Schedule(LocalTime.NOON, LocalTime.MIDNIGHT, DayRange.ALL,
                Station.of("FOO"), Station.of("BAR"), ScheduleState.ENABLED, user);

        scheduleRepository.save(schedule);

        User userToSearchBy = new User(UserId.generate(), "different-target", UserState.ENABLED);

        expectedException.expect(MappingException.class);
        expectedException.expectMessage("Cannot create a reference to an object with a NULL id.");

        // When...
        scheduleRepository.findByUser(userToSearchBy);

        // Then...
        // (exception expected)
    }
}
