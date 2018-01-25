CREATE TABLE `logparser`.`log_file` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `accessLog` BLOB NOT NULL,
  `startDate` DATETIME NOT NULL,
  `duration` VARCHAR(100) NOT NULL,
  `threshold` INT(15) NOT NULL,
  `processDate` DATETIME NOT NULL,
  PRIMARY KEY (`id`));

  CREATE TABLE `logparser`.`log_entry` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `logFile` INT NOT NULL,
  `ipv4` VARCHAR(15) NULL,
  `ipv6` VARCHAR(45) NULL,
  `date` DATETIME NULL,
  `request` VARCHAR(4000) NULL,
  `status` INT(4) NULL,
  `userAgent` VARCHAR(500) NULL,
  `completeLine` VARCHAR(4000) NULL,
  PRIMARY KEY (`id`));
