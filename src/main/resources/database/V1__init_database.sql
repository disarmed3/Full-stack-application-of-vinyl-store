CREATE TABLE IF NOT EXISTS public.users
(
    id       bigserial NOT NULL,
    name     TEXT   NOT NULL,
    email    TEXT   NOT NULL,
    password TEXT   NOT NULL,
    phone_number TEXT,
    address TEXT,
    PRIMARY KEY (id)
    );


CREATE TABLE IF NOT EXISTS public.products
(
    id bigserial not null,
    sku text,
    name text not null,
    description text,
    stock DECIMAL(18, 5),
    price DECIMAL(18, 5),
    PRIMARY KEY (id)
    );

INSERT INTO public.users ( name, email, password, address)
VALUES ('Chris', 'csekas@ctrlspace.dev', '123456', null),
       ('Tasos', 'tasos@ctrlspace.dev', '123456', null);


INSERT INTO public.products (sku, name, description, stock, price)
VALUES ('SKU-123', 'Laptop', 'Acer Aspire 5', 10, 500.0),
       ('SKU-456', 'Smartphone', 'Samsung Galaxy S21', 5, 1000.0),
       ('SKU-869', 'Tablet', 'Apple iPad Pro', 3, 800.0),
       ('SKU-533', 'Smartwatch', 'Apple Watch Series 6', 7, 400.0),
       ('SKU-839', 'Headphones', 'Sony WH-1000XM4', 2, 300.0);

CREATE TABLE IF NOT EXISTS public.orders
(
    id bigserial not null,
    user_id bigint not null,
    order_number text not null,
    order_status text not null,
    create_at timestamp not null,
    update_at timestamp not null,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES public.users (id)
    );

CREATE TABLE IF NOT EXISTS public.product_cart (
                                                   id bigserial not null,
                                                   order_id bigint not null,
                                                   product_id bigint not null,
                                                   quantity int not null,
                                                   PRIMARY KEY (id),
    FOREIGN KEY (order_id) REFERENCES public.orders (id),
    FOREIGN KEY (product_id) REFERENCES public.products (id)
    );


insert into public.orders (user_id, order_number, order_status, create_at, update_at)
values ((select id from users where email = 'csekas@ctrlspace.dev') ,
        'ORDER-123',
        'PENDING',
        now(),
        now());


insert into public.product_cart (order_id, product_id, quantity)
values ((select id from orders where order_number = 'ORDER-123'),
        (select id from products where sku = 'SKU-123'),
        2),
       ((select id from orders where order_number = 'ORDER-123'),
        (select id from products where sku = 'SKU-456'),
        1);


insert into public.orders (user_id, order_number, order_status, create_at, update_at)
values ((select id from users where email = 'tasos@ctrlspace.dev') ,
        'ORDER-456',
        'COMPLETED',
        now(),
        now());

insert into public.product_cart (order_id, product_id, quantity)
values ((select id from orders where order_number = 'ORDER-456'),
        (select id from products where sku = 'SKU-869'),
        1),
       ((select id from orders where order_number = 'ORDER-456'),
        (select id from products where sku = 'SKU-533'),
        2);

insert into public.product_cart (order_id, product_id, quantity)
values ((select id from orders where order_number = 'ORDER-456'),
        (select id from products where sku = 'SKU-123'),
        1);