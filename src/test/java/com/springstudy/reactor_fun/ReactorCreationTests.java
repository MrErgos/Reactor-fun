package com.springstudy.reactor_fun;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Stream;


public class ReactorCreationTests {
    @Test
    public void createAFlux_just() {
        Flux<String> vegetableFlux = Flux
                .just("Cucumber", "Tomato", "Potato", "Onion", "Basil");
        StepVerifier.create(vegetableFlux)
                .expectNext("Cucumber")
                .expectNext("Tomato")
                .expectNext("Potato")
                .expectNext("Onion")
                .expectNext("Basil")
                .verifyComplete();
        vegetableFlux.subscribe(
                s -> System.out.println("Here's some vegetable: " + s)
        );
    }

    @Test
    public void createAFlux_fromArray() {
        String[] vegetables = new String[] {
                "Cucumber", "Tomato", "Potato", "Onion", "Basil"
        };
        Flux<String> vegetableFlux = Flux
                .fromArray(vegetables);
        StepVerifier.create(vegetableFlux)

                .verifyComplete();
    }

    @Test
    public void createAFlux_fromIterable() {
        List<String> vegetables = List.of("Cucumber", "Tomato", "Potato", "Onion", "Basil");
        Flux<String> vegetableFlux = Flux
                .fromIterable(vegetables);
        StepVerifier.create(vegetableFlux)
                .expectNext("Cucumber")
                .expectNext("Tomato")
                .expectNext("Potato")
                .expectNext("Onion")
                .expectNext("Basil")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromStream() {
        Stream<String> vegetables = Stream.of("Cucumber", "Tomato", "Potato", "Onion", "Basil");
        Flux<String> vegetableFlux = Flux
                .fromStream(vegetables);
        StepVerifier.create(vegetableFlux)
                .expectNext("Cucumber")
                .expectNext("Tomato")
                .expectNext("Potato")
                .expectNext("Onion")
                .expectNext("Basil")
                .verifyComplete();
    }

    @Test
    public void createAFlux_range() {
        Flux<Integer> rangeFlux = Flux.range(5, 5);
        StepVerifier.create(rangeFlux)
                .expectNext(5)
                .expectNext(6)
                .expectNext(7)
                .expectNext(8)
                .expectNext(9)
                .verifyComplete();
    }

    @Test
    public void createAFlux_interval() {
        Flux<Long> intervalFlux = Flux
                .interval(Duration.of(1, ChronoUnit.SECONDS))
                .take(5);
        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();

    }
}
