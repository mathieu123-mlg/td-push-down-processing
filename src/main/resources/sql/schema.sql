create type invoice_status as enum ('DRAFT','CONFIRMED', 'PAID');

create table invoice
(
    id            serial primary key,
    customer_name varchar not null,
    status        invoice_status
);

create table invoice_line
(
    id         serial primary key,
    invoice_id int references invoice (id),
    label      varchar        not null,
    quantity   int            not null,
    unit_price numeric(10, 2) not null
);

create table tax_config
(
    id    serial primary key,
    label varchar       not null,
    rate  numeric(5, 2) not null
)