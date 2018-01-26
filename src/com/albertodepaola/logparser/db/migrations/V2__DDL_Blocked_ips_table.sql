CREATE TABLE `logparser`.`blocked_ip` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `logFile` INT NOT NULL,
  `ipv4` VARCHAR(15) NULL,
  `ipv6` VARCHAR(45) NULL,
  `description` VARCHAR(250) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_blocked_ips_1_idx` (`logFile` ASC),
  CONSTRAINT `fk_blocked_ip_log_file`
    FOREIGN KEY (`logFile`)
    REFERENCES `logparser`.`log_file` (`id`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION);
