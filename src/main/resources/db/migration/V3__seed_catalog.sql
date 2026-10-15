-- Default catalog for the single pet shop (owner/attendant created by app on startup)

INSERT INTO services (name, category, duration_min, price_cents, active)
VALUES
    ('Banho', 'BANHO', 60, 6000, TRUE),
    ('Tosa', 'TOSA', 90, 8000, TRUE),
    ('Consulta veterinária', 'CONSULTA', 30, 12000, TRUE),
    ('Vacinação', 'VACINA', 20, 9000, TRUE),
    ('Vermifugação', 'VERMIFUGO', 15, 5000, TRUE),
    ('Atendimento domiciliar', 'DOMICILIAR', 60, 15000, TRUE);

INSERT INTO professionals (name, type, active)
VALUES
    ('Profissional Banho/Tosa', 'BANHO_TOSA', TRUE),
    ('Veterinário(a)', 'VETERINARIO', TRUE);

INSERT INTO vaccine_products (name, species, default_interval_days, recommended_doses, active)
VALUES
    ('Antirrábica', 'TODOS', 365, 1, TRUE),
    ('V8 / V10 (Polivalente)', 'CAO', 21, 3, TRUE),
    ('Giárdia', 'CAO', 21, 2, TRUE),
    ('Gripe canina', 'CAO', 365, 1, TRUE),
    ('Tríplice felina (V3)', 'GATO', 21, 3, TRUE),
    ('Quádrupla felina (V4)', 'GATO', 21, 3, TRUE),
    ('Leucemia felina (FeLV)', 'GATO', 21, 2, TRUE);
