create table if not exists accident(
    accident_id serial primary key,
    accident_name varchar,
    accident_text varchar,
    accident_address varchar,
    accident_type_id int not null references type(type_id)
);
comment on table accident is 'Происшествия';