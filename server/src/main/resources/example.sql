-- Insert test data for `person_data`
INSERT INTO `person_data` (`age`, `email`, `firstName`, `lastName`, `phoneNumber`, `sex`)
VALUES
    (25, 'john.doe@example.com', 'Иван', 'Иванов', '+375123456789', 'Мужской'),
    (30, 'jane.smith@example.com', 'Анна', 'Смирнова', '+375987654321', 'Женский'),
    (28, 'user3@example.com', 'Алиса', 'Браун', '+375123123123', 'Женский'),
    (32, 'user4@example.com', 'Борис', 'Зеленский', '+375234234234', 'Мужской'),
    (29, 'user5@example.com', 'Виктор', 'Чернов', '+375345345345', 'Мужской'),
    (35, 'user6@example.com', 'Диана', 'Белая', '+375456456456', 'Женский'),
    (26, 'user7@example.com', 'Ева', 'Серая', '+375567567567', 'Женский'),
    (31, 'user8@example.com', 'Франц', 'Желтов', '+375678678678', 'Мужской'),
    (27, 'user9@example.com', 'Галина', 'Синяя', '+375789789789', 'Женский'),
    (33, 'user10@example.com', 'Геннадий', 'Краснов', '+375890890890', 'Мужской'),
    (20, 'ignat.malko@gmail.com', 'Игнат', 'Малько', '+375445884385', 'Мужской');

-- Insert test data for `users`
INSERT INTO `users` (`role`, `person_data_id`, `login`, `password`)
VALUES
    (0, 1, 'john_doe', 'password123'),
    (1, 2, 'jane_smith', 'securepass'),
    (0, 3, 'alice_brown', 'password3'),
    (0, 4, 'bob_green', 'password4'),
    (0, 5, 'charlie_black', 'password5'),
    (0, 6, 'diana_white', 'password6'),
    (0, 7, 'eva_gray', 'password7'),
    (0, 8, 'frank_yellow', 'password8'),
    (0, 9, 'grace_blue', 'password9'),
    (0, 10, 'hank_red', 'password10'),
    (2, 11, 'iamignat', 'admin');

-- Insert test data for `interest_rates`
INSERT INTO `interest_rates` (value, type) VALUES (0.0015, 0), (0.24, 1);
-- Insert test data for `accounts`
INSERT INTO `accounts` (`balance`, `created`, `status`, `type`, `owner_id`, `rate_id`, `number`)
VALUES
    (1000.00, '2024-01-01', 1, 0, 1, 1, '4539462023456781'),
    (2000.00, '2024-02-01', 1, 1, 1, 2, '4539462023456782'),
    (1500.00, '2024-01-01', 1, 0, 2, 1, '4539462023456783'),
    (2500.00, '2024-02-01', 1, 1, 2, 2, '4539462023456784'),
    (800.00, '2024-01-01', 1, 0, 3, 1, '4539462023456785'),
    (1200.00, '2024-02-01', 1, 1, 3, 2, '4539462023456786'),
    (700.00, '2024-01-01', 1, 0, 4, 1, '4539462023456787'),
    (1700.00, '2024-02-01', 1, 1, 4, 2, '4539462023456788'),
    (1100.00, '2024-01-01', 1, 0, 5, 1, '4539462023456789'),
    (1300.00, '2024-02-01', 1, 1, 5, 2, '4539462023456790'),
    (400.00, '2024-01-01', 1, 0, 6, 1, '4539462023456791'),
    (900.00, '2024-02-01', 1, 1, 6, 2, '4539462023456792'),
    (600.00, '2024-01-01', 1, 0, 7, 1, '4539462023456793'),
    (1400.00, '2024-02-01', 1, 1, 7, 2, '4539462023456794'),
    (500.00, '2024-01-01', 1, 0, 8, 1, '4539462023456795'),
    (1000.00, '2024-02-01', 1, 1, 8, 2, '4539462023456796'),
    (650.00, '2024-01-01', 1, 0, 9, 1, '4539462023456797'),
    (1350.00, '2024-02-01', 1, 1, 9, 2, '4539462023456798'),
    (1000.00, '2024-01-01', 1, 0, 10, 1, '4539462023456799'),
    (1250.00, '2024-02-02', 1, 1, 10, 2, '4539462023456800');

-- Insert test data for `transactions`
INSERT INTO `transactions` (`amount`, `date`, `receiver_id`, `sender_id`, `message`)
VALUES
-- Transactions for user 1 accounts
(50.00, '2024-03-01', 3, 1, 'Payment to user 3'),
(75.00, '2024-03-02', 4, 2, 'Payment to user 4'),
(100.00, '2024-03-03', 5, 1, 'Payment to user 5'),
(120.00, '2024-03-04', 6, 2, 'Payment to user 6'),
(80.00, '2024-03-05', 7, 1, 'Payment to user 7'),
(90.00, '2024-03-06', 8, 2, 'Payment to user 8'),
(60.00, '2024-03-07', 9, 1, 'Payment to user 9'),
(70.00, '2024-03-08', 10, 2, 'Payment to user 10'),
(55.00, '2024-03-09', 3, 1, 'Payment to user 3'),
(65.00, '2024-03-10', 4, 2, 'Payment to user 4'),
-- Transactions for user 2 accounts
(80.00, '2024-03-01', 1, 3, 'Transfer from user 1'),
(90.00, '2024-03-02', 2, 4, 'Transfer from user 2'),
(85.00, '2024-03-03', 3, 5, 'Transfer from user 3'),
(95.00, '2024-03-04', 4, 6, 'Transfer from user 4'),
(70.00, '2024-03-05', 5, 7, 'Transfer from user 5'),
(75.00, '2024-03-06', 6, 8, 'Transfer from user 6'),
(65.00, '2024-03-07', 7, 9, 'Transfer from user 7'),
(50.00, '2024-03-08', 8, 10, 'Transfer from user 8'),
(95.00, '2024-03-09', 9, 3, 'Transfer from user 9'),
(100.00, '2024-03-10', 10, 4, 'Transfer from user 10');
