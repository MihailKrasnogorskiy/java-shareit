CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY (START 1 MINVALUE 1) NOT NULL,
    name  VARCHAR(255)                                                 NOT NULL,
    email VARCHAR(512)                                                 NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);