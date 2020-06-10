-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema trip_planning_assistant
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `trip_planning_assistant` ;

-- -----------------------------------------------------
-- Schema trip_planning_assistant
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `trip_planning_assistant` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `trip_planning_assistant` ;

-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`yelp_buisness`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`yelp_buisness` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`yelp_buisness` (
  `buisness_id` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `address` VARCHAR(255) NULL,
  `city` VARCHAR(255) NULL,
  `state` VARCHAR(255) NULL,
  `postal_code` VARCHAR(255) NULL,
  `latitude` DECIMAL(12,9) NULL,
  `longitude` DECIMAL(12,9) NULL,
  `stars` DECIMAL(3,2) NULL,
  `review_count` INT NULL,
  `is_open` TINYINT NULL,
  `categories` VARCHAR(255) NULL,
  PRIMARY KEY (`buisness_id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `buisness_id_UNIQUE` ON `trip_planning_assistant`.`yelp_buisness` (`buisness_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`business_attribute`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`business_attribute` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`business_attribute` (
  `uuid` VARCHAR(255) NOT NULL,
  `business_id` VARCHAR(255) NOT NULL,
  `attribute_name` VARCHAR(255) NOT NULL,
  `attribute_value` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT `fk_business_attribute_yelp_buisness`
    FOREIGN KEY (`business_id`)
    REFERENCES `trip_planning_assistant`.`yelp_buisness` (`buisness_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `uuid_UNIQUE` ON `trip_planning_assistant`.`business_attribute` (`uuid` ASC) VISIBLE;

CREATE INDEX `fk_business_attribute_yelp_buisness_idx` ON `trip_planning_assistant`.`business_attribute` (`business_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`business_hour`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`business_hour` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`business_hour` (
  `uuid` VARCHAR(255) NOT NULL,
  `business_id` VARCHAR(255) NOT NULL,
  `day_of_week` VARCHAR(255) NOT NULL,
  `open_time` TIME NOT NULL,
  `close_time` TIME NOT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT `fk_business_hour_yelp_buisness1`
    FOREIGN KEY (`business_id`)
    REFERENCES `trip_planning_assistant`.`yelp_buisness` (`buisness_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `business_id_UNIQUE` ON `trip_planning_assistant`.`business_hour` (`uuid` ASC) VISIBLE;

CREATE INDEX `fk_business_hour_yelp_buisness1_idx` ON `trip_planning_assistant`.`business_hour` (`business_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`user` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`user` (
  `user_id` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `member_since` TIMESTAMP NOT NULL,
  `review_count` INT NOT NULL,
  `useful` INT NOT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `user_id_UNIQUE` ON `trip_planning_assistant`.`user` (`user_id` ASC) VISIBLE;

CREATE UNIQUE INDEX `email_UNIQUE` ON `trip_planning_assistant`.`user` (`email` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`business_checkin`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`business_checkin` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`business_checkin` (
  `uuid` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NULL,
  `business_id` VARCHAR(255) NOT NULL,
  `checkin_time` TIMESTAMP NOT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT `fk_business_checkin_yelp_buisness1`
    FOREIGN KEY (`business_id`)
    REFERENCES `trip_planning_assistant`.`yelp_buisness` (`buisness_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_business_checkin_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `trip_planning_assistant`.`user` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `uuid_UNIQUE` ON `trip_planning_assistant`.`business_checkin` (`uuid` ASC) VISIBLE;

CREATE INDEX `fk_business_checkin_yelp_buisness1_idx` ON `trip_planning_assistant`.`business_checkin` (`business_id` ASC) VISIBLE;

CREATE INDEX `fk_business_checkin_user1_idx` ON `trip_planning_assistant`.`business_checkin` (`user_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`business_tips`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`business_tips` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`business_tips` (
  `uuid` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NULL,
  `business_id` VARCHAR(255) NOT NULL,
  `text` VARCHAR(255) NOT NULL,
  `date` TIMESTAMP NOT NULL,
  `compliment_count` INT NOT NULL,
  PRIMARY KEY (`uuid`),
  CONSTRAINT `fk_business_tips_yelp_buisness1`
    FOREIGN KEY (`business_id`)
    REFERENCES `trip_planning_assistant`.`yelp_buisness` (`buisness_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_business_tips_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `trip_planning_assistant`.`user` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `uuid_UNIQUE` ON `trip_planning_assistant`.`business_tips` (`uuid` ASC) VISIBLE;

CREATE INDEX `fk_business_tips_yelp_buisness1_idx` ON `trip_planning_assistant`.`business_tips` (`business_id` ASC) VISIBLE;

CREATE INDEX `fk_business_tips_user1_idx` ON `trip_planning_assistant`.`business_tips` (`user_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`business_review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`business_review` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`business_review` (
  `review_id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NULL,
  `business_id` VARCHAR(255) NOT NULL,
  `stars` DECIMAL(3,2) NOT NULL,
  `useful` INT NOT NULL,
  `text` VARCHAR(255) NULL,
  `date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`review_id`),
  CONSTRAINT `fk_business_review_yelp_buisness1`
    FOREIGN KEY (`business_id`)
    REFERENCES `trip_planning_assistant`.`yelp_buisness` (`buisness_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_business_review_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `trip_planning_assistant`.`user` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `review_id_UNIQUE` ON `trip_planning_assistant`.`business_review` (`review_id` ASC) VISIBLE;

CREATE INDEX `fk_business_review_yelp_buisness1_idx` ON `trip_planning_assistant`.`business_review` (`business_id` ASC) VISIBLE;

CREATE INDEX `fk_business_review_user1_idx` ON `trip_planning_assistant`.`business_review` (`user_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`airbnb_host`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`airbnb_host` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`airbnb_host` (
  `host_id` VARCHAR(255) NOT NULL,
  `host_url` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `since` DATE NOT NULL,
  `location` VARCHAR(255) NULL,
  `about` TEXT NULL,
  `response_time` VARCHAR(255) NULL,
  `response_rate` DECIMAL(5,2) NULL,
  `listing_count` INT NULL,
  `verification` VARCHAR(255) NULL,
  PRIMARY KEY (`host_id`))
ENGINE = InnoDB;

CREATE UNIQUE INDEX `host_id_UNIQUE` ON `trip_planning_assistant`.`airbnb_host` (`host_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`airbnb_listing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`airbnb_listing` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`airbnb_listing` (
  `listing_id` VARCHAR(255) NOT NULL,
  `host_id` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NULL,
  `summary` TEXT NULL,
  `space` TEXT NULL,
  `description` TEXT NULL,
  `neighborhood_overview` TEXT NULL,
  `notes` TEXT NULL,
  `transit` TEXT NULL,
  `access` TEXT NULL,
  `interaction` TEXT NULL,
  `house_rules` TEXT NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_airbnb_listing_airbnb_host1`
    FOREIGN KEY (`host_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_host` (`host_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`airbnb_listing` (`listing_id` ASC) VISIBLE;

CREATE INDEX `fk_airbnb_listing_airbnb_host1_idx` ON `trip_planning_assistant`.`airbnb_listing` (`host_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`location_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`location_detail` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`location_detail` (
  `listing_id` VARCHAR(255) NOT NULL,
  `street` VARCHAR(255) NULL,
  `city` VARCHAR(255) NULL,
  `state` VARCHAR(255) NULL,
  `zip_code` VARCHAR(255) NULL,
  `country_code` VARCHAR(255) NULL,
  `country` VARCHAR(255) NULL,
  `latitude` DECIMAL(12,9) NULL,
  `longitude` DECIMAL(12,9) NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_location_detail_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`location_detail` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`room_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`room_detail` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`room_detail` (
  `listing_id` VARCHAR(255) NOT NULL,
  `property_type` VARCHAR(255) NULL,
  `room_type` VARCHAR(255) NULL,
  `accommodates` INT NULL,
  `bathroom_count` INT NULL,
  `bedroom_count` INT NULL,
  `bed_count` INT NULL,
  `bed_type` VARCHAR(255) NULL,
  `amenities` TEXT NULL,
  `features` TEXT NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_room_detail_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`room_detail` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`reservation_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`reservation_detail` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`reservation_detail` (
  `listing_id` VARCHAR(255) NOT NULL,
  `daily_price` DECIMAL(11,2) NULL,
  `weekly_price` DECIMAL(11,2) NULL,
  `monthly_price` DECIMAL(11,2) NULL,
  `deposit` DECIMAL(11,2) NULL,
  `cleaning_fee` DECIMAL(11,2) NULL,
  `minimum_nights` INT NULL,
  `maximum_nights` INT NULL,
  `cancellation_policy` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_reservation_detail_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`reservation_detail` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`review_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`review_detail` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`review_detail` (
  `listing_id` VARCHAR(255) NOT NULL,
  `review_count` INT NULL,
  `rating_score` INT NULL,
  `accuracy_score` INT NULL,
  `cleanliness_score` INT NULL,
  `checkin_score` INT NULL,
  `communication_score` INT NULL,
  `location_score` INT NULL,
  `value_score` INT NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_review_detail_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`review_detail` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`url_detail`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`url_detail` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`url_detail` (
  `listing_id` VARCHAR(255) NOT NULL,
  `listing_url` VARCHAR(255) NOT NULL,
  `thumbnail_url` VARCHAR(255) NULL,
  `medium_url` VARCHAR(255) NULL,
  `picture_url` VARCHAR(255) NULL,
  `xl_picture_url` VARCHAR(255) NULL,
  PRIMARY KEY (`listing_id`),
  CONSTRAINT `fk_url_detail_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`url_detail` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`airbnb_review`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`airbnb_review` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`airbnb_review` (
  `review_id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NULL,
  `listing_id` VARCHAR(255) NOT NULL,
  `comment` TEXT NOT NULL,
  `date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`review_id`),
  CONSTRAINT `fk_airbnb_review_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `trip_planning_assistant`.`user` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_airbnb_review_airbnb_listing1`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`airbnb_review` (`review_id` ASC) VISIBLE;

CREATE INDEX `fk_airbnb_review_user1_idx` ON `trip_planning_assistant`.`airbnb_review` (`user_id` ASC) VISIBLE;

CREATE INDEX `fk_airbnb_review_airbnb_listing1_idx` ON `trip_planning_assistant`.`airbnb_review` (`listing_id` ASC) VISIBLE;


-- -----------------------------------------------------
-- Table `trip_planning_assistant`.`airbnb_rating`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `trip_planning_assistant`.`airbnb_rating` ;

CREATE TABLE IF NOT EXISTS `trip_planning_assistant`.`airbnb_rating` (
  `rating_id` VARCHAR(255) NOT NULL,
  `user_id` VARCHAR(255) NULL,
  `listing_id` VARCHAR(255) NOT NULL,
  `rating_score` INT NOT NULL,
  `accuracy_score` INT NOT NULL,
  `cleanliness_score` INT NOT NULL,
  `checkin_score` INT NOT NULL,
  `communication_score` INT NOT NULL,
  `location_score` INT NOT NULL,
  `value_score` INT NOT NULL,
  `date` TIMESTAMP NOT NULL,
  PRIMARY KEY (`rating_id`),
  CONSTRAINT `fk_airbnb_review_user10`
    FOREIGN KEY (`user_id`)
    REFERENCES `trip_planning_assistant`.`user` (`user_id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_airbnb_review_airbnb_listing10`
    FOREIGN KEY (`listing_id`)
    REFERENCES `trip_planning_assistant`.`airbnb_listing` (`listing_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `listing_id_UNIQUE` ON `trip_planning_assistant`.`airbnb_rating` (`rating_id` ASC) VISIBLE;

CREATE INDEX `fk_airbnb_review_user1_idx` ON `trip_planning_assistant`.`airbnb_rating` (`user_id` ASC) VISIBLE;

CREATE INDEX `fk_airbnb_review_airbnb_listing1_idx` ON `trip_planning_assistant`.`airbnb_rating` (`listing_id` ASC) VISIBLE;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
