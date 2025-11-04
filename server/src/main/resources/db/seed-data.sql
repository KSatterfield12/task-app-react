-- Begin transaction block
BEGIN;

-- Drop existing tables
DROP TABLE IF EXISTS tasks;
DROP TABLE IF EXISTS users;

-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create tasks table
CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert household tasks
INSERT INTO task (title, description, completed, created_by) VALUES
('Sweep Kitchen Floor', 'Sweep crumbs and dust from tile floor', false, 'kami'),
('Mow Lawn', 'Use mower for front and backyard', false, 'admin'),
('Pick Up Pinecones', 'Clear yard of pinecones before mowing', true, 'kami'),
('Wash Dishes', 'Scrub and rinse all dinner dishes', false, 'kami'),
('Vacuum Living Room', 'Vacuum carpet and under furniture', true, 'kami'),
('Take Out Trash', 'Empty kitchen, bathroom, and bedroom bins', false, 'kami'),
('Change Bed Sheets', 'Replace linens in all bedrooms', false, 'kami'),
('Clean Bathroom', 'Scrub sink, toilet, and mirror', false, 'admin'),
('Dust Shelves', 'Remove dust from living room shelves and frames', false, 'kami'),
('Wipe Kitchen Counters', 'Disinfect all surfaces', false, 'kami'),
('Laundry - Wash Clothes', 'Start a load of mixed laundry', false, 'kami'),
('Laundry - Fold Clothes', 'Fold and sort clean clothes', true, 'kami'),
('Clean Out Fridge', 'Discard expired items and wipe shelves', false, 'kami'),
('Organize Pantry', 'Sort food items and group by category', false, 'kami'),
('Water Plants', 'Water indoor and porch plants', false, 'kami'),
('Clean Windows', 'Wash inside glass panes in living room', false, 'kami'),
('Sort Mail', 'Organize and discard junk mail', false, 'kami'),
('Wipe Mirrors', 'Clean bathroom and hallway mirrors', true, 'kami'),
('Rake Leaves', 'Clear fallen leaves from yard', false, 'admin'),
('Feed Pets', 'Give pets fresh water and food', false, 'kami'),
('Clean Pet Area', 'Wipe down crate and vacuum pet hair', false, 'kami'),
('Declutter Entryway', 'Tidy up shoes, coats, and mail by door', false, 'kami'),
('Empty Dishwasher', 'Put away clean dishes from last cycle', false, 'kami'),
('Clean Kitchen Sink', 'Scrub and sanitize sink and drain', false, 'kami'),
('Prep Meals', 'Chop veggies and portion lunches', false, 'kami'),
('Sweep Porch', 'Clear debris and dust from porch floor', false, 'admin');

-- Commit the transaction
COMMIT;