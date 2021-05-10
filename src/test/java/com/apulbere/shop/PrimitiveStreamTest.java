package com.apulbere.shop;

import org.junit.jupiter.api.Test;

import java.util.DoubleSummaryStatistics;
import java.util.IntSummaryStatistics;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PrimitiveStreamTest {

    @Test
    void shouldFindMinAndMaxInt() {
        IntSummaryStatistics statistics = IntStream.of(32, 42, 1, 2)
                .summaryStatistics();

        assertEquals(42, statistics.getMax());
        assertEquals(1, statistics.getMin());
    }

    @Test
    void shouldFindMinAndMaxProduct() {
        record Product(String label, double price) {}

        var products = Stream.of(
                new Product("T-Shirt", 12.99),
                new Product("socks", 5.09),
                new Product("pants", 89.1)
        );

        DoubleSummaryStatistics statistics = products.collect(Collectors.summarizingDouble(Product::price));

        assertEquals(89.1, statistics.getMax());
        assertEquals(5.09, statistics.getMin());
    }
}
