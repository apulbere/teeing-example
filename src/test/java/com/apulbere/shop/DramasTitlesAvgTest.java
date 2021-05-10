package com.apulbere.shop;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Year;
import java.util.List;
import java.util.stream.Stream;

import static com.apulbere.shop.DramasTitlesAvgTest.Genre.DRAMA;
import static java.lang.Double.parseDouble;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.*;
import static org.junit.jupiter.api.Assertions.*;

public class DramasTitlesAvgTest {

    record Movie(
        String title,
        double rating,
        Year year,
        List<Genre> genres
    ) {}

    enum Genre {
        ACTION,
        FICTION,
        DRAMA,
        CRIME,
        ADVENTURE,
        COMEDY,
        FANTASY
    }

    private List<Movie> readMoviesFromCSVFile() {
        try(Stream<String> lines = Files.lines(Path.of("src/test/resources/movies-db.csv"))) {
            return lines.map(this::lineToMovie).toList();
        } catch (IOException e) {
            return List.of();
        }
    }

    private Movie lineToMovie(String line) {
        var lineArr = line.split(";");
        var genres = stream(lineArr[3].split(",")).map(Genre::valueOf).toList();
        return new Movie(lineArr[0], parseDouble(lineArr[1]), Year.parse(lineArr[2]), genres);
    }

    @Test
    void shouldFindDramasTitlesAndAvgRating() {
        List<Movie> movies = readMoviesFromCSVFile();

        record MovieStatistics(List<String> titles, double avgRating) {}

        var movieStatistics = movies.stream()
                .filter(m -> m.genres().contains(DRAMA))
                .collect(teeing(
                        mapping(Movie::title, toList()),
                        averagingDouble(Movie::rating),
                        MovieStatistics::new
                ));

        var expectedTitles = List.of(
                "Pulp Fiction",
                "You've Got Mail",
                "Forrest Gump"
        );
        assertEquals(expectedTitles.size(), movieStatistics.titles().size());
        assertTrue(expectedTitles.containsAll(movieStatistics.titles()));
        assertEquals(8.13, movieStatistics.avgRating(), /* delta */ 2);
    }
}
