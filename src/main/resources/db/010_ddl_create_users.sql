CREATE TABLE users(
    id serial primary key ,
    username VARCHAR NOT NULL unique,
    password VARCHAR NOT NULL,
    enabled BOOLEAN DEFAULT TRUE,
    authority_id INT NOT NULL REFERENCES authorities(id)
);