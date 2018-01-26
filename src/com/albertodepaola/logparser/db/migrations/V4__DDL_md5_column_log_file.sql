ALTER TABLE `logparser`.`log_file` 
ADD COLUMN `md5` VARCHAR(200) NULL AFTER `processDate`;
