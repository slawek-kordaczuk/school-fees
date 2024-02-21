DROP TABLE IF EXISTS parent CASCADE;

CREATE TABLE parent
(
    id         UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS school CASCADE;

CREATE TABLE school
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(100)   NOT NULL,
    hour_price NUMERIC(10, 2) NOT NULL
);

DROP TABLE IF EXISTS child CASCADE;

CREATE TABLE child
(
    id         UUID PRIMARY KEY,
    parent_id  UUID        NOT NULL,
    school_id  UUID        NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    foreign key (parent_id) references parent (id),
    foreign key (school_id) references school (id)
);

DROP TABLE IF EXISTS attendance CASCADE;

CREATE TABLE attendance
(
    id         UUID PRIMARY KEY,
    child_id   UUID      NOT NULL,
    entry_date TIMESTAMP NOT NULL,
    exit_date  TIMESTAMP NOT NULL,
    foreign key (child_id) references child (id)
);



