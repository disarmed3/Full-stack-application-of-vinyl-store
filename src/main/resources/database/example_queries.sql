select *
from products
where stock < 6;


----------  INNER JOIN  ----------------------------------

SELECT o.order_number, p.name, pc.quantity, p.price
FROM public.orders o
         inner join public.product_cart pc on o.id = pc.order_id
         inner join public.products p on pc.product_id = p.id;
------------  INNER JOIN  --------------------------------
SELECT o.order_number, p.name, pc.quantity, p.price
FROM public.products p
         inner join public.product_cart pc on p.id = pc.product_id
         inner join public.orders o on o.id = pc.order_id;
------------  LEFT JOIN  ---------------------------------
SELECT o.order_number, p.name, pc.quantity, p.price
FROM public.products p
         left join public.product_cart pc on p.id = pc.product_id
         left join public.orders o on o.id = pc.order_id;
------------  LEFT OUTER JOIN  ---------------------------------
SELECT o.order_number, p.name, pc.quantity, p.price
FROM public.products p
         left join public.product_cart pc on p.id = pc.product_id
         left join public.orders o on o.id = pc.order_id
where o.order_number is null;
---------------------------------------------


SELECT o.order_number, sum(p.price * pc.quantity) as total_price
FROM public.orders o
         inner join public.product_cart pc on o.id = pc.order_id
         inner join public.products p on pc.product_id = p.id
GROUP BY o.order_number

SELECT o.order_number, count(1) as unique_products
FROM public.orders o
         inner join public.product_cart pc on o.id = pc.order_id
         inner join public.products p on pc.product_id = p.id
GROUP BY o.order_number


SELECT o.order_number, sum(pc.quantity) as temaxia
FROM public.orders o
         inner join public.product_cart pc on o.id = pc.order_id
         inner join public.products p on pc.product_id = p.id
GROUP BY o.order_number;


SELECT p.name,
       COALESCE(SUM(pc.quantity), 0) AS temaxia,
       count(o.order_number) as num_of_unique_orders
FROM public.products p
         left join public.product_cart pc on p.id = pc.product_id
         left join public.orders o on o.id = pc.order_id
GROUP BY p.name
HAVING COALESCE(SUM(pc.quantity), 0) > 1;



-- SELECT FROM WHERE
-- ORDER BY
-- LEFT, INNER joins
-- GROUP BY and HAVING