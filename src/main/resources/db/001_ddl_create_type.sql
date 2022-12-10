create table if not exists type(
    type_id serial primary key,
    type_name varchar
);
comment on table type is 'Тип происшествия';
