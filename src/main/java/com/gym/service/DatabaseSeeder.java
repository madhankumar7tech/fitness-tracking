package com.gym.service;

import com.gym.model.*;
import com.gym.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final MembershipPlanRepository planRepository;
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final WorkoutScheduleRepository scheduleRepository;

    public DatabaseSeeder(MembershipPlanRepository planRepository,
                          MemberRepository memberRepository,
                          PaymentRepository paymentRepository,
                          WorkoutScheduleRepository scheduleRepository) {
        this.planRepository = planRepository;
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (planRepository.count() == 0) {
            seedDatabase();
        }
    }

    private void seedDatabase() {
        System.out.println("Seeding database with default gym data...");

        // 1. Seed Membership Plans
        MembershipPlan basic = new MembershipPlan("Silver Plan", 29.99, 1, "Access to gym floor and standard cardio equipment.");
        MembershipPlan premium = new MembershipPlan("Gold Plan", 49.99, 3, "Access to gym floor, group classes, and locker room.");
        MembershipPlan vip = new MembershipPlan("Platinum VIP", 99.99, 12, "All-inclusive access, personal trainer sessions, and spa privileges.");
        
        planRepository.saveAll(Arrays.asList(basic, premium, vip));

        // 2. Seed Members
        Member m1 = new Member("John Doe", "john.doe@email.com", "+1 555-0199", LocalDate.now().minusMonths(2), premium, true);
        Member m2 = new Member("Jane Smith", "jane.smith@email.com", "+1 555-0120", LocalDate.now().minusMonths(1), vip, true);
        Member m3 = new Member("Mike Johnson", "mike.j@email.com", "+1 555-0143", LocalDate.now().minusWeeks(2), basic, true);
        Member m4 = new Member("Sarah Connor", "sarah.c@email.com", "+1 555-0165", LocalDate.now().minusMonths(6), basic, false);

        memberRepository.saveAll(Arrays.asList(m1, m2, m3, m4));

        // 3. Seed Payments
        Payment p1 = new Payment(m1, 49.99, LocalDate.now().minusMonths(2), "Credit Card", "PAID");
        Payment p2 = new Payment(m1, 49.99, LocalDate.now().minusMonths(1), "Credit Card", "PAID");
        Payment p3 = new Payment(m2, 99.99, LocalDate.now().minusMonths(1), "Bank Transfer", "PAID");
        Payment p4 = new Payment(m3, 29.99, LocalDate.now().minusWeeks(2), "UPI", "PAID");
        Payment p5 = new Payment(m4, 29.99, LocalDate.now().minusMonths(6), "Cash", "PAID");

        paymentRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        // 4. Seed Workout Schedules
        WorkoutSchedule s1 = new WorkoutSchedule("Morning Yoga", "Elena Rostova", "07:00 AM - 08:00 AM", 15, "Monday");
        WorkoutSchedule s2 = new WorkoutSchedule("Cardio Burn", "Marcus Aurelius", "08:30 AM - 09:30 AM", 25, "Tuesday");
        WorkoutSchedule s3 = new WorkoutSchedule("Power Weightlifting", "Arnold Schwarzenegger", "06:00 PM - 07:30 PM", 12, "Wednesday");
        WorkoutSchedule s4 = new WorkoutSchedule("Zumba Fitness", "Sophia Loren", "05:00 PM - 06:00 PM", 30, "Thursday");
        WorkoutSchedule s5 = new WorkoutSchedule("CrossFit Intensive", "Chris Hemsworth", "07:00 AM - 08:15 AM", 20, "Friday");
        WorkoutSchedule s6 = new WorkoutSchedule("Core Pilates", "Elena Rostova", "09:00 AM - 10:00 AM", 15, "Saturday");

        scheduleRepository.saveAll(Arrays.asList(s1, s2, s3, s4, s5, s6));

        System.out.println("Gym data seeding completed successfully.");
    }
}
