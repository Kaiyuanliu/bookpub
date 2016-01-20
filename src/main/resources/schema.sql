-- Create syntax for TABLE 'book_reviewers'
DROP TABLE IF EXISTS `book_reviewers`;

-- Create syntax for TABLE 'book'
DROP TABLE IF EXISTS `book`;

-- Create syntax for TABLE 'author'
DROP TABLE IF EXISTS `author`;
CREATE TABLE `author` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255) DEFAULT NULL ,
  `last_name` VARCHAR(255) DEFAULT NULL ,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'publisher'
DROP TABLE IF EXISTS `publisher`;
CREATE TABLE `publisher` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) DEFAULT NULL ,
  PRIMARY KEY (`id`)
);

-- Create syntax for TABLE 'reviewer'
DROP TABLE IF EXISTS `reviewer`;
CREATE TABLE `reviewer` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(255) DEFAULT NULL ,
  `last_name` VARCHAR(255) DEFAULT NULL ,
  PRIMARY KEY (`id`)
);


CREATE TABLE `book` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(255) DEFAULT NULL ,
  `isbn` VARCHAR(255) DEFAULT NULL ,
  `title` VARCHAR(255) DEFAULT NULL ,
  `author` BIGINT(20) DEFAULT NULL ,
  `publisher` BIGINT(20) DEFAULT NULL ,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_publisher` FOREIGN KEY (`publisher`) REFERENCES `publisher` (`id`),
  CONSTRAINT `FK_author` FOREIGN KEY (`author`) REFERENCES `author` (`id`)
);


CREATE TABLE `book_reviewers` (
  `books` BIGINT(20) NOT NULL ,
  `reviewers` BIGINT(20) NOT NULL ,
  CONSTRAINT `FK_book` FOREIGN KEY (`books`) REFERENCES `book` (`id`),
  CONSTRAINT `FK_reviewer` FOREIGN KEY (`reviewers`) REFERENCES `reviewer` (`id`)
);