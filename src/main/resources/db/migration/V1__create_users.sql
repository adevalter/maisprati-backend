-- Single-shop: users only (no SaaS / multi-tenant)

CREATE TABLE users (
    id               BIGSERIAL PRIMARY KEY,
    name             VARCHAR(120)  NOT NULL,
    email            VARCHAR(180)  NOT NULL,
    password_hash    VARCHAR(255)  NOT NULL,
    role             VARCHAR(30)   NOT NULL,
    active           BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT ck_users_role
        CHECK (role IN ('PET_SHOP_ADMIN', 'ATENDENTE'))
);

CREATE INDEX idx_users_role ON users (role);
