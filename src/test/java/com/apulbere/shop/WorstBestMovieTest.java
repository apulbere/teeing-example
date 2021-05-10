package com.apulbere.shop;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorstBestMovieTest {

    record Movie(String title, double rating) {}

    @Test
    void shouldFindWorstAndBestMovie() {
        var m1 = new Movie("Groundhog Day", 8);
        var m2 = new Movie("Stop! Or My Mom Will Shoot", 4.4);
        var m3 = new Movie("Forrest Gump", 8.8);

        record MovieStatistics(Movie worst, Movie best) {}

        var ratingComparator = Comparator.comparing(Movie::rating);

        var movieStatistics = Stream.of(m1, m2, m3)
                .collect(Collectors.teeing(
                        Collectors.minBy(ratingComparator),
                        Collectors.maxBy(ratingComparator),
                        (worst, best) -> new MovieStatistics(
                                worst.orElse(null),
                                best.orElse(null)
                        )
                ));

        assertEquals(m2, movieStatistics.worst());
        assertEquals(m3, movieStatistics.best());
    }
}
