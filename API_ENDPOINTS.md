# API Endpoints Schema

Компактная схема endpoint-ов приложения (Spring Boot), сгруппированная по уровню доступа.

## Visual map (Mermaid)

```mermaid
flowchart LR
  subgraph Public
    P0[Public permitAll]
    P1[POST /user/login]
    P2[POST /user/register]
    P3[GET /cars]
    P4[POST /rentals]
    P5[POST /rentals/price]
  end

  subgraph Authenticated
    A0[Authenticated user]
    A1[GET /profile/me]
    A2[GET /rentals/my]
  end

  subgraph Admin
    B0[Admin role]
    B1[Car CRUD /cars /cars/:uuid]
    B2[CarBrand CRUD /car-brands /car-brands/:uuid]
    B3[CarModel CRUD /car-models /car-models/:uuid]
    B4[CarSpecs CRUD /car-specs /car-specs/:uuid]
    B5[Engine CRUD /engines /engines/:uuid]
    B6[EngineType CRUD /engine-types /engine-types/:uuid]
    B7[EngineSpecs GET POST /engine-specs]
    B8[LoyaltyRule CRUD /loyalty-rules /loyalty-rules/:uuid]
    B9[GET /rentals/admin]
    B10[PATCH /rentals/:uuid/status]
  end
```

## Public + Authenticated

| Access | Method | Path |
|---|---|---|
| `permitAll` | POST | `/user/login` |
| `permitAll` | POST | `/user/register` |
| `permitAll` | GET | `/cars` |
| `permitAll` | POST | `/rentals` |
| `permitAll` | POST | `/rentals/price` |
| `authenticated` | GET | `/profile/me` |
| `authenticated` | GET | `/rentals/my` |

## Admin (`ROLE_ADMIN`)
| Resource | GET              | POST | PUT/PATCH               | DELETE                 |
|---|------------------|---|-------------------------|------------------------|
| `/cars` | `/cars` | `/cars` | `/cars/{uuid}`          | `/cars/{uuid}`         |
| `/car-brands` | `/car-brands`    | `/car-brands` | `/car-brands/{uuid}`    | `/car-brands/{uuid}`   |
| `/car-models` | `/car-models`    | `/car-models` | `/car-models/{uuid}`    | `/car-models/{uuid}`   |
| `/car-specs` | `/car-specs`     | `/car-specs` | `/car-specs/{uuid}`     | `/car-specs/{uuid}`    |
| `/engines` | `/engines`       | `/engines` | `/engines/{uuid}`       | `/engines/{uuid}`      |
| `/engine-types` | `/engine-types`  | `/engine-types` | `/engine-types/{uuid}`  | `/engine-types/{uuid}` |
| `/engine-specs` | `/engine-specs`  | `/engine-specs` | `/engine-specs{uuid}`   | `/engine-specs{uuid}`  |
| `/loyalty-rules` | `/loyalty-rules` | `/loyalty-rules` | `/loyalty-rules/{uuid}` | `/loyalty-rules/{uuid}` |

### Special admin endpoints

| Method | Path | Purpose |
|---|---|---|
| GET | `/rentals/admin` | Admin list of rental orders |
| PATCH | `/rentals/{uuid}/status` | Update rental order status |

---
