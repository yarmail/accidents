create table if not exists rule(
    rule_id serial primary key,
    rule_name varchar
);
comment on table rule is 'Статьи, правила';