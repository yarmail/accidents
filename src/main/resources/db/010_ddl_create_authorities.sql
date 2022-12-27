CREATE TABLE authorities(
    authority_id SERIAL PRIMARY KEY,
    username VARCHAR REFERENCES users(username) ON DELETE CASCADE,
    authority VARCHAR NOT NULL
);