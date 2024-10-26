package com.springstudy.reactor_fun;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;

public class ReactorCombinationTests {
    @Test
    public void mergeFluxes() {
        Flux<String> characterFlux = Flux
                .just("James Bond", "Vito Corleone", "Don Draper", "Tony Stark", "John Wick")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> drinkFlux = Flux
                .just("Scotch Whisky", "Red Wine", "Old Fashioned", "Scotch Whisky", "Bourbon")
                .delaySubscription(Duration.ofMillis(500))
                .delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = characterFlux.mergeWith(drinkFlux);

        StepVerifier.create(mergedFlux)
                .expectNext("James Bond")
                .expectNext("Scotch Whisky")
                .expectNext("Vito Corleone")
                .expectNext("Red Wine")
                .expectNext("Don Draper")
                .expectNext("Old Fashioned")
                .expectNext("Tony Stark")
                .expectNext("Scotch Whisky")
                .expectNext("John Wick")
                .expectNext("Bourbon")
                .verifyComplete();
    }

    @Test
    public void zipFluxes() {
        Flux<String> characterFlux = Flux
                .just("James Bond", "Vito Corleone", "Don Draper", "Tony Stark", "John Wick")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> drinkFlux = Flux
                .just("Scotch Whisky", "Red Wine", "Old Fashioned", "Scotch Whisky", "Bourbon")
                .delaySubscription(Duration.ofMillis(500))
                .delayElements(Duration.ofSeconds(1));

        Flux<Tuple2<String, String>> zippedFlux = Flux.zip(characterFlux, drinkFlux);

        StepVerifier.create(zippedFlux)
                .expectNextMatches(p ->
                        p.getT1().equals("James Bond") && p.getT2().equals("Scotch Whisky"))
                .expectNextMatches(p ->
                        p.getT1().equals("Vito Corleone") && p.getT2().equals("Red Wine"))
                .expectNextMatches(p ->
                        p.getT1().equals("Don Draper") && p.getT2().equals("Old Fashioned"))
                .expectNextMatches(p ->
                        p.getT1().equals("Tony Stark") && p.getT2().equals("Scotch Whisky"))
                .expectNextMatches(p ->
                        p.getT1().equals("John Wick") && p.getT2().equals("Bourbon"))
                .verifyComplete();
    }

    @Test
    public void zipFluxToObject() {
        Flux<String> characterFlux = Flux
                .just("James Bond", "Vito Corleone", "Don Draper", "Tony Stark", "John Wick")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> drinkFlux = Flux
                .just("Scotch Whisky", "Red Wine", "Old Fashioned", "Scotch Whisky", "Bourbon")
                .delaySubscription(Duration.ofMillis(500))
                .delayElements(Duration.ofSeconds(1));

        Flux<String> zippedFlux = Flux.zip(characterFlux, drinkFlux, (c, d) -> c + " drinks " + d);

        StepVerifier.create(zippedFlux)
                .expectNext("James Bond drinks Scotch Whisky")
                .expectNext("Vito Corleone drinks Red Wine")
                .expectNext("Don Draper drinks Old Fashioned")
                .expectNext("Tony Stark drinks Scotch Whisky")
                .expectNext("John Wick drinks Bourbon")
                .verifyComplete();
    }

    @Test
    public void firstWithSignalFlux() {
        Flux<String> slowAnimalFlux = Flux
                .just("Three-Toed Sloth", "Garden Snail", "Starfish")
                .delaySubscription(Duration.ofMillis(500));
        Flux<String> fastAnimalFlux = Flux
                .just("Brown Hare", "Blue Wildebeest", "Marlin");

        Flux<String> firstFlux = Flux.firstWithSignal(slowAnimalFlux, fastAnimalFlux);
        StepVerifier.create(firstFlux)
                .expectNext("Brown Hare")
                .expectNext("Blue Wildebeest")
                .expectNext("Marlin")
                .verifyComplete();
    }
}
