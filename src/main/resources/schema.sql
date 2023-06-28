DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS user(
                      uid BIGINT PRIMARY KEY AUTO_INCREMENT,
                      phone_number VARCHAR(255),
                      identity ENUM('Driver', 'Passenger'),
                      secret_key VARCHAR(255),
                      user_name VARCHAR(255),
                      car_number VARCHAR(255),
                      car_type VARCHAR(255),
                      total_ride_number DOUBLE,
                      province VARCHAR(255),
                      city VARCHAR(255)
);

DROP TABLE IF EXISTS `logs`;
CREATE TABLE IF NOT EXISTS `logs` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT,
                        `timestamp` DATETIME NOT NULL,
                        `source_module` VARCHAR(255) NOT NULL,
                        `log_level` ENUM('DEBUG', 'INFO', 'WARNING', 'ERROR') NOT NULL,
                        `log_content` TEXT NOT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `ride`;
CREATE TABLE IF NOT EXISTS `ride` (
                        `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                        `creation_time` DATETIME,
                        `passenger_uid` BIGINT,
                        `driver_uid` BIGINT,
                        `mqtt_channel_name` VARCHAR(255),
                        `ride_type` ENUM('Economy', 'Comfort', 'Luxury'),
                        `start_point_coordinates` VARCHAR(255),
                        `start_point_address` VARCHAR(255),
                        `end_point_coordinates` VARCHAR(255),
                        `end_point_address` VARCHAR(255),
                        `status` ENUM('Created', 'DriverAccepted', 'PickedUpPassenger', 'OnRide', 'Arrived', 'Cancelled', 'OnAlert'),
                        `driver_accept_time` DATETIME,
                        `pick_up_time` DATETIME,
                        `arrival_time` DATETIME,
                        `cancellation_time` DATETIME,
                        `ride_length` DOUBLE,
                        `order_id` BIGINT,
                        `alert_status` VARCHAR(255),
                        `aftersales_status` VARCHAR(255),
                        `ride_score` DOUBLE,
                        `ride_review` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE IF EXISTS `order`;
CREATE TABLE IF NOT EXISTS `order` (
                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                         `ride_id` BIGINT ,
                         `creation_time` DATETIME NOT NULL,
                         `total_cost` DOUBLE,
                         `base_cost` DOUBLE,
                         `ride_and_fuel_cost` DOUBLE,
                         `time_cost` DOUBLE,
                         `special_location_servicecost` DOUBLE,
                         `dynamic_cost` DOUBLE,
                         `status` ENUM('Unpaid', 'Paid','PaidOrderComplete', 'RefundProcessing', 'Refunded') NOT NULL,
                         `payment_platform` VARCHAR(255),
                         `payment_platform_serial_number` VARCHAR(255),
                         `payment_result_from_platform` VARCHAR(255),
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `coordinate` (
                                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                            `longitude` DOUBLE,
                                            `latitude` DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

DROP TABLE track;
CREATE TABLE IF NOT EXISTS `track` (
                                       `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
                                       `ride_id` BIGINT,
                                       `time_sequence` DATETIME,
                                       `mqtt_channel_name` VARCHAR(255),
                                       `coordinate` varchar(255),
                                       `speed_track` DOUBLE,
                                       `altitude` DOUBLE,
                                       FOREIGN KEY (`ride_id`) REFERENCES `ride`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

