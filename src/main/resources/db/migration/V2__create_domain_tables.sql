-- Domain tables (single pet shop — no pet_shop_id)

CREATE TABLE clients (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(150)  NOT NULL,
    phone           VARCHAR(30),
    mobile          VARCHAR(30),
    email           VARCHAR(180),
    document        VARCHAR(20),
    address         TEXT,
    notes           TEXT,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_clients_name ON clients (name);
CREATE INDEX idx_clients_mobile ON clients (mobile);
CREATE INDEX idx_clients_document ON clients (document) WHERE document IS NOT NULL;

CREATE TABLE pets (
    id              BIGSERIAL PRIMARY KEY,
    client_id       BIGINT        NOT NULL,
    name            VARCHAR(100)  NOT NULL,
    species         VARCHAR(40)   NOT NULL,
    breed           VARCHAR(80),
    sex             VARCHAR(20),
    birth_date      DATE,
    weight_kg       NUMERIC(6, 2),
    allergies       TEXT,
    notes           TEXT,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pets_client
        FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE RESTRICT,
    CONSTRAINT ck_pets_weight CHECK (weight_kg IS NULL OR weight_kg > 0),
    CONSTRAINT ck_pets_sex CHECK (sex IS NULL OR sex IN ('MACHO', 'FEMEA', 'INDEFINIDO'))
);

CREATE INDEX idx_pets_client_id ON pets (client_id);
CREATE INDEX idx_pets_name ON pets (name);

CREATE TABLE professionals (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(120)  NOT NULL,
    type            VARCHAR(30)   NOT NULL,
    active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_professionals_type
        CHECK (type IN ('BANHO_TOSA', 'VETERINARIO', 'GERAL'))
);

CREATE INDEX idx_professionals_type ON professionals (type);
CREATE INDEX idx_professionals_active ON professionals (active);

CREATE TABLE services (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(120)  NOT NULL,
    category        VARCHAR(30)   NOT NULL,
    duration_min    INTEGER,
    price_cents     INTEGER       NOT NULL DEFAULT 0,
    active          BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_services_name UNIQUE (name),
    CONSTRAINT ck_services_duration CHECK (duration_min IS NULL OR duration_min > 0),
    CONSTRAINT ck_services_price CHECK (price_cents >= 0),
    CONSTRAINT ck_services_category
        CHECK (category IN ('BANHO', 'TOSA', 'CONSULTA', 'VACINA', 'VERMIFUGO', 'DOMICILIAR', 'OUTRO'))
);

CREATE INDEX idx_services_category ON services (category);

CREATE TABLE vaccine_products (
    id                      BIGSERIAL PRIMARY KEY,
    name                    VARCHAR(120)  NOT NULL,
    species                 VARCHAR(20)   NOT NULL DEFAULT 'TODOS',
    default_interval_days   INT           NOT NULL DEFAULT 365,
    recommended_doses       INT           NOT NULL DEFAULT 1,
    active                  BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_vaccine_products_name UNIQUE (name),
    CONSTRAINT ck_vaccine_products_species
        CHECK (species IN ('CAO', 'GATO', 'TODOS')),
    CONSTRAINT ck_vaccine_products_interval CHECK (default_interval_days > 0),
    CONSTRAINT ck_vaccine_products_doses CHECK (recommended_doses > 0)
);

CREATE TABLE appointments (
    id                  BIGSERIAL PRIMARY KEY,
    client_id           BIGINT        NOT NULL,
    pet_id              BIGINT        NOT NULL,
    service_id          BIGINT        NOT NULL,
    professional_id     BIGINT,
    scheduled_at        TIMESTAMPTZ   NOT NULL,
    status              VARCHAR(30)   NOT NULL DEFAULT 'AGENDADO',
    is_home_visit       BOOLEAN       NOT NULL DEFAULT FALSE,
    address_override    TEXT,
    notes               TEXT,
    check_in_at         TIMESTAMPTZ,
    completed_at        TIMESTAMPTZ,
    service_price_cents INTEGER,
    amount_cents        INTEGER,
    payment_status      VARCHAR(20),
    payment_method      VARCHAR(30),
    paid_at             TIMESTAMPTZ,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_appointments_client
        FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE RESTRICT,
    CONSTRAINT fk_appointments_pet
        FOREIGN KEY (pet_id) REFERENCES pets (id) ON DELETE RESTRICT,
    CONSTRAINT fk_appointments_service
        FOREIGN KEY (service_id) REFERENCES services (id) ON DELETE RESTRICT,
    CONSTRAINT fk_appointments_professional
        FOREIGN KEY (professional_id) REFERENCES professionals (id) ON DELETE SET NULL,
    CONSTRAINT ck_appointments_status
        CHECK (status IN ('AGENDADO', 'CONFIRMADO', 'EM_ATENDIMENTO', 'FINALIZADO', 'CANCELADO')),
    CONSTRAINT ck_appointments_service_price
        CHECK (service_price_cents IS NULL OR service_price_cents >= 0),
    CONSTRAINT ck_appointments_amount
        CHECK (amount_cents IS NULL OR amount_cents >= 0),
    CONSTRAINT ck_appointments_payment_status
        CHECK (payment_status IS NULL OR payment_status IN ('PENDENTE', 'PAGO', 'ISENTO')),
    CONSTRAINT ck_appointments_payment_method
        CHECK (payment_method IS NULL OR payment_method IN (
            'DINHEIRO', 'PIX', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'OUTRO'
        ))
);

CREATE INDEX idx_appointments_scheduled_at ON appointments (scheduled_at);
CREATE INDEX idx_appointments_status ON appointments (status);
CREATE INDEX idx_appointments_pet_id ON appointments (pet_id);
CREATE INDEX idx_appointments_client_id ON appointments (client_id);

CREATE TABLE pet_history (
    id                  BIGSERIAL PRIMARY KEY,
    pet_id              BIGINT        NOT NULL,
    appointment_id      BIGINT,
    professional_id     BIGINT,
    vaccine_product_id  BIGINT,
    dose_number         SMALLINT,
    type                VARCHAR(30)   NOT NULL,
    title               VARCHAR(180)  NOT NULL,
    body                TEXT,
    occurred_at         TIMESTAMPTZ   NOT NULL,
    next_due_at         DATE,
    weight_kg           NUMERIC(6, 2),
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_pet_history_pet
        FOREIGN KEY (pet_id) REFERENCES pets (id) ON DELETE CASCADE,
    CONSTRAINT fk_pet_history_appointment
        FOREIGN KEY (appointment_id) REFERENCES appointments (id) ON DELETE SET NULL,
    CONSTRAINT fk_pet_history_professional
        FOREIGN KEY (professional_id) REFERENCES professionals (id) ON DELETE SET NULL,
    CONSTRAINT fk_pet_history_vaccine_product
        FOREIGN KEY (vaccine_product_id) REFERENCES vaccine_products (id) ON DELETE SET NULL,
    CONSTRAINT ck_pet_history_type
        CHECK (type IN ('BANHO', 'TOSA', 'CONSULTA', 'VACINA', 'VERMIFUGO', 'DOMICILIAR', 'RETORNO', 'OUTRO')),
    CONSTRAINT ck_pet_history_weight CHECK (weight_kg IS NULL OR weight_kg > 0)
);

CREATE INDEX idx_pet_history_pet_id ON pet_history (pet_id);
CREATE INDEX idx_pet_history_occurred_at ON pet_history (occurred_at DESC);
CREATE INDEX idx_pet_history_next_due_at ON pet_history (next_due_at) WHERE next_due_at IS NOT NULL;
CREATE INDEX idx_pet_history_vaccine_product_id ON pet_history (vaccine_product_id);
CREATE UNIQUE INDEX uk_pet_history_appointment
    ON pet_history (appointment_id)
    WHERE appointment_id IS NOT NULL;

CREATE TABLE expenses (
    id               BIGSERIAL PRIMARY KEY,
    description      VARCHAR(200)  NOT NULL,
    category         VARCHAR(40)   NOT NULL,
    amount_cents     INTEGER       NOT NULL,
    due_date         DATE          NOT NULL,
    status           VARCHAR(20)   NOT NULL DEFAULT 'ABERTA',
    payment_method   VARCHAR(30),
    paid_at          TIMESTAMPTZ,
    notes            TEXT,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT ck_expenses_amount CHECK (amount_cents >= 0),
    CONSTRAINT ck_expenses_category CHECK (category IN (
        'ALUGUEL', 'UTILIDADES', 'FORNECEDOR', 'SALARIO', 'IMPOSTO', 'MANUTENCAO', 'OUTRO'
    )),
    CONSTRAINT ck_expenses_status CHECK (status IN ('ABERTA', 'PAGA', 'CANCELADA')),
    CONSTRAINT ck_expenses_payment_method CHECK (
        payment_method IS NULL OR payment_method IN (
            'DINHEIRO', 'PIX', 'CARTAO_DEBITO', 'CARTAO_CREDITO', 'OUTRO'
        )
    )
);

CREATE INDEX idx_expenses_due_date ON expenses (due_date);
CREATE INDEX idx_expenses_status ON expenses (status);
