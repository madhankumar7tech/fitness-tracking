package com.gym.model;

import jakarta.persistence.*;

@Entity
@Table(name = "membership_plans")
public class MembershipPlan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Double price;
    
    @Column(name = "duration_months", nullable = false)
    private Integer durationMonths;
    
    private String description;

    // Constructors
    public MembershipPlan() {}

    public MembershipPlan(String name, Double price, Integer durationMonths, String description) {
        this.name = name;
        this.price = price;
        this.durationMonths = durationMonths;
        this.description = description;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MembershipPlan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", durationMonths=" + durationMonths +
                ", description='" + description + '\'' +
                '}';
    }
}
