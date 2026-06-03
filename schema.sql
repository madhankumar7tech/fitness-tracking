-- SQL Schema for Gym Management Application
-- You can copy-paste this into your Supabase SQL Editor to set up the tables.

-- 1. Membership Plans Table
CREATE TABLE IF NOT EXISTS membership_plans (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL,
    duration_months INTEGER NOT NULL,
    description TEXT
);

-- 2. Members Table
CREATE TABLE IF NOT EXISTS members (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    join_date DATE NOT NULL,
    plan_id BIGINT REFERENCES membership_plans(id) ON DELETE SET NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 3. Payments Table
CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    member_id BIGINT NOT NULL REFERENCES members(id) ON DELETE CASCADE,
    amount DOUBLE PRECISION NOT NULL,
    payment_date DATE NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- 4. Workout Schedules Table
CREATE TABLE IF NOT EXISTS workout_schedules (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    coach VARCHAR(255),
    time_slot VARCHAR(100) NOT NULL,
    max_capacity INTEGER NOT NULL,
    day_of_week VARCHAR(50) NOT NULL
);
