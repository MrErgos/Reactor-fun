package com.springstudy.reactor_fun;

import lombok.Data;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ReactorTransformationTests {
    @Test
    public void skipFew() {
        Flux<String> countFlux = Flux
                .just("one", "two", "skip a few", "ninety nine", "one hundred")
                .skip(3);

        StepVerifier.create(countFlux)
                .expectNext("ninety nine", "one hundred")
                .verifyComplete();
    }

    @Test
    public void skipFewSeconds() {
        Flux<String> countFlux = Flux
                .just("one", "two", "skip a few", "ninety nine", "one hundred")
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofSeconds(3));

        StepVerifier.create(countFlux)
                .expectNext("skip a few","ninety nine", "one hundred")
                .verifyComplete();
    }

    @Test
    public void takeFew() {
        Flux<String> players = Flux
                .just("You're in", "You're in, too", "You're out")
                .take(2);

        StepVerifier.create(players)
                .expectNext("You're in", "You're in, too")
                .verifyComplete();
    }

    @Test
    public void takeFewSeconds() {
        Flux<String> players = Flux
                .just("You're in", "You're in, too", "You're out")
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofSeconds(3));

        StepVerifier.create(players)
                .expectNext("You're in", "You're in, too")
                .verifyComplete();
    }

    @Test
    public void filterFlux() {
        Flux<String> canyons = Flux
                .just("Colca Canyon, Peru", "Colca Canyon, Peru, USA", "Grand Canyon South Rim, USA")
                .filter(canyon -> canyon.endsWith("USA"));

        StepVerifier.create(canyons)
                .expectNext("Colca Canyon, Peru, USA", "Grand Canyon South Rim, USA")
                .verifyComplete();
    }

    @Test
    public void distinctFlux() {
        Flux<String> namesFlux = Flux
                .just("Nick", "Max", "Nick", "Masha", "Bob", "Max")
                .distinct();

        StepVerifier.create(namesFlux)
                .expectNext("Nick", "Max", "Masha", "Bob")
                .verifyComplete();
    }

    @Test
    public void mapFlux() {
        Flux<Player> playerFlux = Flux
                .just("Noobmaster69 - 1.5", "player1430234 - 10.5")
                .map(s -> {
                    String[] split = s.split("\\s-\\s");
                    return new Player(split[0], Double.parseDouble(split[1]));
                });
        StepVerifier.create(playerFlux)
                .expectNext(new Player("Noobmaster69", 1.5))
                .expectNext(new Player("player1430234", 10.5))
                .verifyComplete();
    }

    @Data
    private static class Player {
        private final String nickname;
        private final double kd;
    }

    @Test
    public void flatMapFlux() {
        Flux<Player> playerFlux = Flux
                .just("Noobmaster69 - 1.5", "player1430234 - 10.5")
                .flatMap(s -> Mono.just(s)
                        .map(p -> {
                            String[] split = p.split("\\s-\\s");
                            return new Player(split[0], Double.parseDouble(split[1]));
                        })
                        .subscribeOn(Schedulers.parallel())
                );

        List<Player> playerList = Arrays.asList(
                new Player("Noobmaster69", 1.5),
                new Player("player1430234", 10.5)
        );

        StepVerifier.create(playerFlux)
                .expectNextMatches(player -> playerList.contains(player))
                .expectNextMatches(player -> playerList.contains(player))
                .verifyComplete();

    }
}
