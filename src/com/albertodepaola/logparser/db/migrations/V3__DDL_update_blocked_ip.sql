ALTER TABLE `logparser`.`blocked_ip` 
ADD COLUMN `ocurrences` INT NULL AFTER `ipv6`,
ADD COLUMN `updateTime` DATETIME NULL AFTER `description`;
