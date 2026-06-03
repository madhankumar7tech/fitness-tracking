package com.gym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "workout_schedules")
public class WorkoutSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String coach;

    @Column(name = "time_slot", nullable = false)
    private String timeSlot;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek;

    // Constructors
    public WorkoutSchedule() {}

    public WorkoutSchedule(String name, String coach, String timeSlot, Integer maxCapacity, String dayOfWeek) {
        this.name = name;
        this.coach = coach;
        this.timeSlot = timeSlot;
        this.maxCapacity = maxCapacity;
        this.dayOfWeek = dayOfWeek;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoach() {
        return coach;
    }

    public void setCoach(String coach) {
        this.coach = coach;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public String toString() {
        return "WorkoutSchedule{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coach='" + coach + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                '}';
    }
}
