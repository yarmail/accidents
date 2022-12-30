INSERT INTO users (username, password, enabled, authority_id)
VALUES ('root','$2a$10$xt0xv8961redy9XH0ak.TuwS1p6VQ.G.vEKpnkqUFRnWGYVK4No8u',
        true, (SELECT id from authorities where authority = 'ROLE_ADMIN'));

COMMENT ON TABLE users is 'пароль из слова secret на основе BCryptPasswordEncoder';
