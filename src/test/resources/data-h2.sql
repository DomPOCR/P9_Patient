DROP TABLE if exists UserTest;

CREATE TABLE UserTest (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(125) NOT NULL,
    password VARCHAR(125) NOT NULL,
    role VARCHAR(125) NOT NULL );

insert into UserTest(id,username, password, role)
values(1,'admin', '$2a$10$l.LADbaPZKdrDYy4kPzN9u10fomM2HuhFh6oeDIfcHsAHwS7foNvu', 'ADMIN');

insert into UserTest(id,username, password, role)
values(2,'User',  '$2a$10$Y.ryVIaZ1xCNEI3KKmMhFOAiIi1cNfhtDNj1XHjA7gXNhJKjDzNtK', 'USER');