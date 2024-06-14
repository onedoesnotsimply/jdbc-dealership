-- Drop the database if it exists, create it if it doesn't

DROP DATABASE IF EXISTS jdbc_cardealerships;

CREATE DATABASE IF NOT EXISTS jdbc_cardealerships;

USE jdbc_cardealerships;

-- Create tables

CREATE TABLE `dealerships` (
`dealership_id` INT NOT NULL AUTO_INCREMENT,
`name` varchar(50),
`address` varchar(50),
`phone` varchar(12),
PRIMARY KEY(`dealership_id`)
);

CREATE TABLE `vehicles` (
`vin` INT NOT NULL,
`year` INT,
`make` varchar(50),
`model` varchar(50),
`vehicleType` varchar(50),
`color` varchar(50),
`odometer` INT,
`price` DOUBLE,
`sold` BOOL,
PRIMARY KEY(`vin`)
);

CREATE TABLE `inventory` (
`dealership_id` INT,
`vin` INT
);

CREATE TABLE `sales_contracts` (
`sales_id` INT NOT NULL AUTO_INCREMENT,
`dealership_id` INT,
`sales_date` DATE,
`name` varchar(50),
`email` varchar(50),
`vin` INT NOT NULL,
`year` INT,
`make` varchar(50),
`model` varchar(50),
`vehicleType` varchar(50),
`color` varchar(50),
`odometer` INT,
`price` DOUBLE,
`financed` BOOLEAN,
PRIMARY KEY(`sales_id`),
FOREIGN KEY(`vin`) REFERENCES vehicles(vin),
FOREIGN KEY(`dealership_id`) REFERENCES dealerships(dealership_id)
);

CREATE TABLE `lease_contracts` (
`lease_id` INT NOT NULL AUTO_INCREMENT,
`dealership_id` INT,
`lease_date` DATE,
`name` varchar(50),
`email` varchar(50),
`vin` INT NOT NULL,
`year` INT,
`make` varchar(50),
`model` varchar(50),
`vehicleType` varchar(50),
`color` varchar(50),
`odometer` INT,
`price` DOUBLE,
`expected_ending_price` DOUBLE,
`lease_fee` DOUBLE,
PRIMARY KEY(`lease_id`),
FOREIGN KEY(`vin`) REFERENCES vehicles(vin),
FOREIGN KEY(`dealership_id`) REFERENCES dealerships(dealership_id)
);

-- Truncate all data
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE dealerships;
TRUNCATE TABLE vehicles;
TRUNCATE TABLE sales_contracts;
TRUNCATE TABLE lease_contracts;
SET FOREIGN_KEY_CHECKS = 1;
TRUNCATE TABLE inventory;

-- Add data to dealerships
INSERT INTO dealerships VALUES(1, 'B & D Motors', '123 Somewhere', '888-999-1010');
INSERT INTO dealerships VALUES(2, 'Harney & Sons', '999 Another St', '999-222-2932');

-- Add data to vehicles
INSERT INTO vehicles VALUES(1111, 1998, 'Ford', 'Explorer', 'Truck', 'Red', 130000, 13000.0, false);
INSERT INTO vehicles VALUES(2222, 2004, 'Jeep', 'Wrangler', 'SUV', 'Black', 54000, 25999.98, false);
INSERT INTO vehicles VALUES(3333, 2023, 'Toyota', 'Prius', 'Compact', 'Pink', 230000, 7000.24, true);
INSERT INTO vehicles VALUES(4444, 2020, 'Honda', 'Accord', 'Sedan', 'Silver', 150000, 9000.0, true);

-- Add data to inventory
INSERT INTO inventory VALUES(2, 1111);
INSERT INTO inventory VALUES(2, 4444);
INSERT INTO inventory VALUES(1, 2222);
INSERT INTO inventory VALUES(1, 3333);

-- Add data to sales_contracts
INSERT INTO sales_contracts VALUES (1, 1, '2024-02-20', 'Emily Trifone', 'ET@example.com', 3333, 2023, 'Toyota', 'Prius', 'Compact','Pink', 230000, 9000.0, true);

-- Add data to lease_contracts
INSERT INTO lease_contracts VALUES (1, 2, '2024-06-13', 'Adam Trifone', 'AT@example.com', 4444, 2020, 'Honda', 'Accord', 'Sedan', 'Silver', 150000, 9000.0, 4500.0, 630.0);
