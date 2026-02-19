insert into invoice (id, customer_name, status)
values (1, 'Alice', 'CONFIRMED'),
       (2, 'Bob', 'PAID'),
       (3, 'Charlie', 'DRAFT');

insert into invoice_line (invoice_id, label, quantity, unit_price)
values (1, 'Produit A', 2, 100),
       (1, 'Produit B', 1, 50),
       (2, 'Produit A', 5, 100),
       (2, 'Service C', 1, 200),
       (3, 'Produit B', 3, 50);

insert into tax_config (label, rate)
values ('TVA STANDARD', 20)