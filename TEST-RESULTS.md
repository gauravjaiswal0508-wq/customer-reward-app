# Test Cases and Execution Status

## Test suite

Run the following from the project root on a machine with JDK 8+ and Maven 3.6+:

```bash
mvn clean test
```

| Test class | Scenarios covered |
|---|---|
| `RewardCalculatorTest` | Amounts at/below/above $50 and $100; $120 example; fractional dollar handling; null and negative amount rejection |
| `RewardApiIntegrationTest` | Known customer response; default timeframe; customer and transaction details; unknown customer returns 404; timeframe below 1 and above 36 returns 400 |

## Execution status in this delivery environment

**Not run here.** Java 21 is available, but Maven is not installed and this environment cannot resolve external hosts to download it. Therefore, no passing test result is claimed. Run `mvn clean test` locally and retain the actual Maven/Surefire output for the submission.

The test source files are included in `src/test/java`. Generated build output is intentionally excluded from source control.
