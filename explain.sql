-- 1) enche a tabela (só em Postgres, no container) para o planner ter razões para usar índices
insert into vehicles (plate, brand, model, manufacture_year, status, mileage_km)
select 'X' || lpad(g::text, 6, '0'), 'Marca', 'Modelo', 2020, 'available', g
from generate_series(1, 200000) as g;

analyze vehicles;

-- 2) pesquisa exata pela matrícula: usa o índice da constraint unique (Index Scan)
explain analyze select * from vehicles where plate = 'X000123';

-- 3) pesquisa com wildcard à esquerda: não consegue usar um índice B-tree (Seq Scan)
explain analyze select * from vehicles where lower(plate) like '%123%';