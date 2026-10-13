# Customer Rewards API

A Java 8-compatible Spring Boot REST API that calculates retailer reward points per transaction, summarizes them by calendar month, and returns customer-level totals for a caller-selected timeframe. This project implements the attached Java homework and additional tips, including asynchronous data retrieval simulation, validation, structured errors, logging, sample data, automated tests, and API documentation.

## Requirements and technology

- JDK 8 or later (the source/bytecode target is Java 8)
- Maven 3.6+
- Spring Boot 2.7.18, Spring Web, Bean Validation
- JUnit 5, Spring Boot Test, MockMvc
- In-memory sample repositories; no external database is required

> The repository contains source code and tests only. Do not commit generated executable/JAR files. Demo data is in memory and is not real customer data.

## Reward calculation

Points are calculated independently for each transaction:

| Amount tier | Points |
|---|---:|
| First $50 (up to and including $50) | 0 |
| Portion above $50 through $100 | 1 point per whole dollar |
| Portion above $100 | 2 points per whole dollar, in addition to the 50 points earned in the middle tier |

Examples: `$49.99 → 0`, `$75.00 → 25`, `$100.00 → 50`, `$120.00 → 90`.

Fractional dollars do not earn partial points; each eligible tier is floored independently. Currency amounts use `BigDecimal`, not binary floating point. The assignment example is therefore implemented as `50 + (20 * 2) = 90` points for a $120 purchase.

## Run the application

From this directory:

```bash
mvn clean test
mvn spring-boot:run
```

The API listens on `http://localhost:8080`. To package the application locally (do not commit the output):

```bash
mvn clean package
java -jar target/rewards-api-1.0.0.jar
```

## REST API

### `GET /api/rewards/{customerId}?months={months}`

Returns reward details for one known customer.

| Parameter | Required | Default | Validation |
|---|---|---|---|
| `customerId` (path) | Yes | — | Must identify a seeded customer |
| `months` (query) | No | `3` | Integer from 1 to 36 |

The date range begins on the first day of the earliest included calendar month and ends today. For example, `months=3` includes the current calendar month and the two preceding months. The response includes customer identity, timeframe, spend and transaction totals, each month's spend/count/points (including months with no transactions), and each transaction's date, description, amount, and calculated points.

Example request:

```bash
curl -i "http://localhost:8080/api/rewards/C001?months=3"
```

Seeded customer IDs are `C001`, `C002`, and `C003`. Sample transactions are generated relative to the date the application starts so the recent timeframe can be demonstrated.

Example response shape (amounts are JSON strings with two decimal places):

```json
{
  "customerId": "C001",
  "customerName": "Aarav Sharma",
  "email": "aarav@example.com",
  "timeframeMonths": 3,
  "startDate": "2026-08-01",
  "endDate": "2026-10-13",
  "transactionCount": 5,
  "totalSpend": "554.99",
  "totalRewardPoints": 435,
  "monthlyRewards": [
    {"month": "2026-08", "transactionCount": 1, "rewardPoints": 50, "totalSpend": "100.00"},
    {"month": "2026-09", "transactionCount": 2, "rewardPoints": 270, "totalSpend": "259.99"},
    {"month": "2026-10", "transactionCount": 2, "rewardPoints": 115, "totalSpend": "195.00"}
  ],
  "transactions": [
    {"transactionId": "T1005", "transactionDate": "2026-08-05", "description": "Furniture", "amount": "100.00", "rewardPoints": 50}
  ]
}
```

The dates and transaction examples above illustrate the response format; the actual rolling sample data changes with the system date. Use the live response for exact totals.

### Error responses

Errors use a consistent JSON body containing `timestamp`, `status`, `error`, `message`, and `path`.

- `200 OK`: rewards calculated successfully
- `400 Bad Request`: invalid `months`, non-integer parameter, or invalid input
- `404 Not Found`: customer ID is not in the sample customer repository
- `500 Internal Server Error`: unexpected server-side failure; details are logged server-side and not exposed to the caller

Examples:

```bash
curl -i "http://localhost:8080/api/rewards/UNKNOWN?months=3"
curl -i "http://localhost:8080/api/rewards/C001?months=0"
```

### Postman

Import `postman_collection.json` into Postman. It contains smoke checks for a successful three-month response, unknown customer (404), and invalid timeframe (400). The base URL is configurable using the collection variable `baseUrl`.

## Design and implementation

- `controller/RewardController`: REST route and request parameter handling.
- `service/RewardService`: date-window calculation, asynchronous retrieval coordination, monthly grouping, and response assembly.
- `service/RewardCalculator`: isolated, deterministic tier calculation.
- `service/TransactionService`: simulates an asynchronous upstream fetch with `@Async` and a dedicated executor; replace this boundary with a real repository/client in production.
- `repository/CustomerRepository` and `repository/TransactionRepository`: deterministic in-memory demo data.
- `dto/`: response contracts for customer totals, monthly summaries, and individual transactions.
- `exception/GlobalExceptionHandler`: consistent API errors and server-side exception logging.
- `config/AsyncConfig`: bounded executor for the asynchronous fetch simulation.

## Test plan

Run `mvn clean test`. Automated tests cover:

- Reward tiers below, at, and above $50/$100, including the `$120 = 90 points` example.
- Fractional dollar behavior and invalid null/negative amounts.
- Successful API response and detailed customer/transaction fields.
- Default three-month timeframe.
- Unknown customer (404), timeframe below minimum (400), and timeframe above maximum (400).

Do not report a test as passed unless the Maven command completes successfully in your environment. This development environment may not have Maven installed; if so, run the command on a machine with JDK and Maven installed and retain its output as the actual test report.

## Version control / GitHub

Use Git commits rather than uploading a folder as the source-control workflow. From the project root:

```bash
git init
git add .
git commit -m "feat: implement customer rewards calculation API"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/customer-rewards-api.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username. This workspace cannot publish to your GitHub account without an authenticated remote supplied by you.

## Production considerations

This is a complete assignment/demo implementation, not a production financial system. Before production use, replace in-memory storage with a database, add authentication/authorization, define currency and timezone policies, add request tracing/metrics and CI, and consider pagination for high-volume transaction histories.
