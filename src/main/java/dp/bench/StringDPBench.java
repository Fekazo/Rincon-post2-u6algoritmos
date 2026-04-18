package dp.bench;

import dp.StringDP;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
public class StringDPBench {

    @Param({"100", "500", "1000"})
    public int n;

    private String X;
    private String Y;

    @Setup(Level.Trial)
    public void setup() {
        Random rnd = new Random(42);
        StringBuilder sb1 = new StringBuilder(n);
        StringBuilder sb2 = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb1.append((char) ('a' + rnd.nextInt(26)));
            sb2.append((char) ('a' + rnd.nextInt(26)));
        }
        X = sb1.toString();
        Y = sb2.toString();
    }

    @Benchmark
    public int lcsLength() {
        return StringDP.lcsLength(X, Y);
    }

    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(StringDPBench.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }
}