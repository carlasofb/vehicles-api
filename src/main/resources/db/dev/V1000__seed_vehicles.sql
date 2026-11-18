-- Dados de desenvolvimento (só carregados no profile dev). Versão alta para nunca colidir com migrações reais.
insert into vehicles (plate, brand, model, manufacture_year, status, mileage_km) values
    ('AA-12-BB', 'Renault',    'Clio',     2021, 'available',   48200),
    ('CC-34-DD', 'Peugeot',    '208',      2022, 'in_service',  31500),
    ('EE-56-FF', 'Volkswagen', 'Golf',     2019, 'maintenance', 96300),
    ('GG-78-HH', 'Toyota',     'Corolla',  2023, 'available',   12400),
    ('II-90-JJ', 'Ford',       'Transit',  2020, 'in_service',  74800),
    ('KK-11-LL', 'Mercedes',   'Sprinter', 2021, 'maintenance', 88100),
    ('MM-22-NN', 'Nissan',     'Leaf',     2022, 'available',   25600),
    ('OO-33-PP', 'Citroën',    'Berlingo', 2018, 'in_service', 121000);