DROP TABLE IF EXISTS p9_db.patient;
DROP TABLE IF EXISTS p9_db.user;

CREATE TABLE IF NOT EXISTS p9_db.patient (
    id BIGINT NOT NULL AUTO_INCREMENT,
    address VARCHAR(200) NOT NULL,
    birthdate DATE NOT NULL,
    firstname VARCHAR(100) NOT NULL,
    lastname VARCHAR(100) NOT NULL,
    phone VARCHAR(12),
    genre VARCHAR(1) NOT NULL,
    PRIMARY KEY (id)
    )  ENGINE=INNODB

CREATE TABLE IF NOT EXISTS p9_db.user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(125) NOT NULL,
    password VARCHAR(125) NOT NULL,
    role VARCHAR(125) NOT NULL,
    PRIMARY KEY (id)
    )  ENGINE=INNODB

INSERT INTO  p9_db.patient(id,firstName,lastName,birthdate,genre,address,phone)
VALUES
(1,'Test','TestNone','1966-12-31','F','1 Brookside St','100-222-3333'),
(2,'Test','TestBorderline','1945-06-24','M','2 High St','200-333-4444'),
(3,'Test','TestInDanger','2004-06-18','M','3 Club Road','300-444-5555'),
(4,'Test','TestEarlyOnset','2002-06-28','F','4 Valley Dr','400-555-6666'),
(5,'Lucas','Ferguson','1968-06-22','M','2 Warren Street','387-866-1399'),
(6,'Pippa','Rees','1952-09-27','F','745 West Valley Farms Drive','628-423-0993'),
(7,'Edward','Arnold','1952-11-11','M','599 East Garden Ave','123-727-2779'),
(8,'Anthony','Sharp','1946-11-26','M','894 Hall Street','451-761-8383'),
(9,'Wendy','Ince','1958-06-29','F','4 Southampton Road','802-911-9975'),
(10,'Tracey','Ross','1949-12-07','F','40 Sulphur Springs Dr','131-396-5049'),
(11,'Claire','Wilson','1966-12-31','F','12 Cobblestone St','300-452-1091'),
(12,'Max','Buckland','1945-06-24','M','193 Vale St','833-534-0864'),
(13,'Natalie','Clark','1964-06-18','F','12 Beechwood Road','241-467-9197'),
(14,'Piers','Bailey','1959-06-28','M','1202 Bumble Dr','747-815-0557');

INSERT INTO p9_db.user(id,username,password,role)
VALUES
(1,'domp','$2y$12$6VlKNg2DItKlOHOi7s2z6eT367Y93xJ.n5z2gU3CxA7.ydVOWsItK','ADMIN'),
(2,'user','$2y$12$2xdc.tu5oVtBmtxmcKPX4OpHHf6PsReAjYDvbxyq8c1UCwF0P.tGC','USER');