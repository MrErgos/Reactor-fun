package com.springstudy.reactor_fun;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ReactorBufferingTests {
    @Test
    public void bufferFlux() {
        Flux<String> vegetableFlux = Flux
                .just("Cucumber", "Tomato", "Potato", "Onion", "Basil");
        Flux<List<String>> bufferedFlux = vegetableFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("Cucumber", "Tomato", "Potato"))
                .expectNext(Arrays.asList("Onion", "Basil"))
                .verifyComplete();
    }

    @Test
    public void bufferAndFlatMapFlux() {
        Flux
                .just("Cucumber", "Tomato", "Potato", "Onion", "Basil")
                .buffer(3)
                .flatMap(x -> Flux.fromIterable(x)
                        .map(String::toUpperCase)
                        .subscribeOn(Schedulers.parallel())
                        .log()
                ).subscribe();
    }

    @Test
    public void collectListFlux() {
        Flux<String> vegetableFlux = Flux
                .just("Cucumber", "Tomato", "Potato", "Onion", "Basil");
        Mono<List<String>> vegetableListMono = vegetableFlux.collectList();
        StepVerifier.create(vegetableListMono)
                .expectNext(Arrays.asList(
                        "Cucumber", "Tomato", "Potato", "Onion", "Basil"
                ))
                .verifyComplete();
    }

    @Test
    public void collectMapFlux() {
        Flux<String> animalFlux = Flux.just(
                "aardvark", "elephant", "koala", "eagle", "kangaroo"
        );
        Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(s -> s.charAt(0));

        StepVerifier.create(animalMapMono)
                .expectNextMatches(characterStringMap -> {
                    return characterStringMap.size() == 3 &&
                            characterStringMap.get('a').equals("aardvark") &&
                            characterStringMap.get('e').equals("eagle") &&
                            characterStringMap.get('k').equals("kangaroo");
                })
                .verifyComplete();
    }

    @Test
    public void allFlux() {
        Flux<String> animalFlux = Flux
                .just("Sloth", "Snail", "Starfish");
        Mono<Boolean> startsWithSMono = animalFlux.all(s -> s.startsWith("S"));
        StepVerifier.create(startsWithSMono)
                .expectNext(true)
                .verifyComplete();

        Mono<Boolean> hasKMono = animalFlux.all(s -> s.contains("l"));
        StepVerifier.create(hasKMono)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    public void anyFlux() {
        Flux<String> animalFlux = Flux
                .just("Sloth", "Snail", "Starfish");
        Mono<Boolean> hasFMono = animalFlux.any(s -> s.contains("f"));
        StepVerifier.create(hasFMono)
                .expectNext(true)
                .verifyComplete();
        Mono<Boolean> endsWithHMono = animalFlux.any(s -> s.endsWith("a"));
        StepVerifier.create(endsWithHMono)
                .expectNext(false)
                .verifyComplete();
    }
}
