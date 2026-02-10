package rng;

public final class FunctionalRNG {

    private final long seed;

    public FunctionalRNG(long seed) {
        this.seed = seed;
    }

    public double uniform01(
            long storeId,
            long dayIndex,
            long customerId,
            long ruleId,
            long step
    ) {
        long x = hash(seed, storeId, dayIndex, customerId, ruleId, step);
        long mantissa = (x >>> 11);
        return mantissa * (1.0 / (1L << 53));
    }

    public boolean bernoulli(
            double probability,
            long storeId,
            long dayIndex,
            long customerId,
            long ruleId,
            long step
    ) {
        if (probability <= 0.0) return false;
        if (probability >= 1.0) return true;
        return uniform01(storeId, dayIndex, customerId, ruleId, step) < probability;
    }

    public int uniformIntInclusive(
            int lowInclusive,
            int highInclusive,
            long storeId,
            long dayIndex,
            long customerId,
            long ruleId,
            long step
    ) {
        if (highInclusive < lowInclusive) {
            throw new IllegalArgumentException("Invalid range");
        }
        int span = highInclusive - lowInclusive + 1;
        long x = hash(seed, storeId, dayIndex, customerId, ruleId, step);
        int offset = floorMod(x, span);
        return lowInclusive + offset;
    }

    private static int floorMod(long x, int m) {
        long r = x % m;
        return (int) (r >= 0 ? r : r + m);
    }

    private static long hash(long seed,
                             long a,
                             long b,
                             long c,
                             long d,
                             long e) {
        long x = seed;
        x ^= mix64(a + 0x9E3779B97F4A7C15L);
        x ^= mix64(b + 0xC2B2AE3D27D4EB4FL);
        x ^= mix64(c + 0x165667B19E3779F9L);
        x ^= mix64(d + 0x85EBCA77C2B2AE63L);
        x ^= mix64(e + 0x27D4EB2F165667C5L);
        return splitMix64(x);
    }

    private static long splitMix64(long x) {
        long z = x + 0x9E3779B97F4A7C15L;
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }

    private static long mix64(long z) {
        z ^= (z >>> 33);
        z *= 0xff51afd7ed558ccdL;
        z ^= (z >>> 33);
        z *= 0xc4ceb9fe1a85ec53L;
        z ^= (z >>> 33);
        return z;
    }
}
