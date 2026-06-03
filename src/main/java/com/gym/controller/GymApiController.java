package com.gym.controller;

import com.gym.model.*;
import com.gym.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GymApiController {

    private final MemberRepository memberRepository;
    private final MembershipPlanRepository planRepository;
    private final PaymentRepository paymentRepository;
    private final WorkoutScheduleRepository scheduleRepository;

    public GymApiController(MemberRepository memberRepository,
                            MembershipPlanRepository planRepository,
                            PaymentRepository paymentRepository,
                            WorkoutScheduleRepository scheduleRepository) {
        this.memberRepository = memberRepository;
        this.planRepository = planRepository;
        this.paymentRepository = paymentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    // --- DASHBOARD STATS ---
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalMembers = memberRepository.count();
        long activeMembers = memberRepository.findAll().stream().filter(Member::getActive).count();
        double totalRevenue = paymentRepository.findAll().stream()
                .filter(p -> "PAID".equalsIgnoreCase(p.getStatus()))
                .mapToDouble(Payment::getAmount)
                .sum();
        long totalSchedules = scheduleRepository.count();

        stats.put("totalMembers", totalMembers);
        stats.put("activeMembers", activeMembers);
        stats.put("totalRevenue", totalRevenue);
        stats.put("totalSchedules", totalSchedules);

        // Plans Popularity Chart Data
        Map<String, Integer> planPopularity = new HashMap<>();
        for (Member m : memberRepository.findAll()) {
            if (m.getPlan() != null) {
                planPopularity.put(m.getPlan().getName(), planPopularity.getOrDefault(m.getPlan().getName(), 0) + 1);
            }
        }
        stats.put("planPopularity", planPopularity);

        // Payments Breakdown by Method
        Map<String, Integer> paymentMethods = new HashMap<>();
        for (Payment p : paymentRepository.findAll()) {
            paymentMethods.put(p.getPaymentMethod(), paymentMethods.getOrDefault(p.getPaymentMethod(), 0) + 1);
        }
        stats.put("paymentMethods", paymentMethods);

        return ResponseEntity.ok(stats);
    }

    // --- MEMBERS ENDPOINTS ---
    @GetMapping("/members")
    public List<Member> getMembers(@RequestParam(value = "query", required = false) String query) {
        if (query != null && !query.trim().isEmpty()) {
            return memberRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
        }
        return memberRepository.findAll();
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        return memberRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/members")
    public ResponseEntity<Member> saveOrUpdateMember(@RequestBody Map<String, Object> payload) {
        String name = (String) payload.get("name");
        String email = (String) payload.get("email");
        String phone = (String) payload.get("phone");
        String joinDateStr = (String) payload.get("joinDate");
        Boolean active = (Boolean) payload.get("active");
        
        // Handle plan association
        MembershipPlan plan = null;
        if (payload.get("planId") != null) {
            Long planId = Long.valueOf(payload.get("planId").toString());
            plan = planRepository.findById(planId).orElse(null);
        }

        Member member;
        if (payload.get("id") != null) {
            Long id = Long.valueOf(payload.get("id").toString());
            member = memberRepository.findById(id).orElse(new Member());
        } else {
            member = new Member();
        }

        member.setName(name);
        member.setEmail(email);
        member.setPhone(phone);
        member.setJoinDate(joinDateStr != null ? LocalDate.parse(joinDateStr) : LocalDate.now());
        member.setPlan(plan);
        member.setActive(active != null ? active : true);

        Member saved = memberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/members/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (memberRepository.existsById(id)) {
            memberRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- PLANS ENDPOINTS ---
    @GetMapping("/plans")
    public List<MembershipPlan> getPlans() {
        return planRepository.findAll();
    }

    @PostMapping("/plans")
    public ResponseEntity<MembershipPlan> saveOrUpdatePlan(@RequestBody MembershipPlan plan) {
        MembershipPlan saved = planRepository.save(plan);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/plans/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- PAYMENTS ENDPOINTS ---
    @GetMapping("/payments")
    public List<Payment> getPayments() {
        return paymentRepository.findAllByOrderByPaymentDateDesc();
    }

    @PostMapping("/payments")
    public ResponseEntity<?> savePayment(@RequestBody Map<String, Object> payload) {
        Long memberId = Long.valueOf(payload.get("memberId").toString());
        Double amount = Double.valueOf(payload.get("amount").toString());
        String paymentMethod = (String) payload.get("paymentMethod");
        String status = (String) payload.get("status");
        String paymentDateStr = (String) payload.get("paymentDate");

        Optional<Member> memberOpt = memberRepository.findById(memberId);
        if (memberOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Member not found");
        }

        Payment payment = new Payment();
        payment.setMember(memberOpt.get());
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(status != null ? status : "PAID");
        payment.setPaymentDate(paymentDateStr != null ? LocalDate.parse(paymentDateStr) : LocalDate.now());

        Payment saved = paymentRepository.save(payment);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // --- SCHEDULES ENDPOINTS ---
    @GetMapping("/schedules")
    public List<WorkoutSchedule> getSchedules() {
        return scheduleRepository.findAll();
    }

    @PostMapping("/schedules")
    public ResponseEntity<WorkoutSchedule> saveOrUpdateSchedule(@RequestBody WorkoutSchedule schedule) {
        WorkoutSchedule saved = scheduleRepository.save(schedule);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id) {
        if (scheduleRepository.existsById(id)) {
            scheduleRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
