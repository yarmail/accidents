create table if not exists accident(
    id serial primary key,
    name varchar,
    text varchar,
    address varchar,
    type_id int not null references acc_type(id)
);