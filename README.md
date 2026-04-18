# rincon-post2-u6

## StringDP — Benchmark `lcsLength`

Benchmark ejecutado con JMH sobre cadenas aleatorias de igual longitud (semilla 42),
5 iteraciones de medición, modo `AverageTime`.

| n    | Tiempo promedio | Error     |
|------|----------------|-----------|
| 100  | 0.045 ms       | ± 0.012   |
| 500  | 1.178 ms       | ± 0.212   |
| 1000 | 4.823 ms       | ± 0.100   |

### Análisis de crecimiento cuadrático

`lcsLength` es **O(n²)**. Al multiplicar `n` por un factor `k`, el tiempo esperado
crece `k²`:

- n: 100 → 500 (×5): tiempo 0.045 → 1.178 ms → factor real **×26.2** (esperado ×25 ✓)
- n: 500 → 1000 (×2): tiempo 1.178 → 4.823 ms → factor real **×4.1** (esperado ×4 ✓)

Los factores observados confirman el crecimiento cuadrático **O(n²)**.

## Ejecución de tests

```bash
mvn test -Dtest=StringDPTest
mvn test -Dtest=FloydWarshallTest
mvn test