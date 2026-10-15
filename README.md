# Backend — Pet Shop API (loja única)

Spring Boot 3 + Java 21 + PostgreSQL + JWT.

Um único pet shop. Papéis: `PET_SHOP_ADMIN` (dono) e `ATENDENTE`.

## Pré-requisitos

- JDK 21
- Maven 3.9+
- PostgreSQL

## Rodar a API

```bash
cd backend
mvn spring-boot:run
```

API em `http://localhost:8080`. O Flyway aplica V1–V3 automaticamente.

**Importante:** após a simplificação single-shop, o banco antigo (SaaS) precisa ser **recriado** (drop + create do database `petshop`).

## Contas seed (automático no startup)

| Papel | E-mail | Senha |
|-------|--------|-------|
| Dono (`PET_SHOP_ADMIN`) | `dono@petshop.local` | `dono123` |
| Atendente | `atendente@petshop.local` | `atendente123` |

Configurável em `application.yml` (`app.owner.*`, `app.attendant.*`). Nome da loja: `app.shop-name`.

## Endpoints principais

### Auth / loja
- `POST /api/auth/login`
- `GET /api/shop` — nome da loja

### Equipe (dono)
- `GET/POST /api/users`
- `PATCH /api/users/{id}/active`

### Operação
- CRUD `/api/clients`, `/api/pets`, `/api/appointments`
- Catálogo `/api/services`, `/api/professionals`, `/api/vaccine-products`
- `GET /api/dashboard/today`
- Financeiro `/api/finance/receivables`, `/api/finance/payables` (payables: dono)

## Migrations

- `V1` — `users` (`PET_SHOP_ADMIN` \| `ATENDENTE`)
- `V2` — domínio operacional (sem `pet_shop_id`) + vacinas + despesas
- `V3` — seed de serviços, profissionais e vacinas
