create table if not exists accident_rule(
    accident_rule_id serial primary key,
    accident_id int references accident(accident_id),
    rule_id int references rule(rule_id)
);