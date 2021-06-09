package org.homepoker.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;

/**
 * This is really not related to any of the poker work, but more a place where I can experiment/practice Reactive
 * exercises as  I learn the programming model.
 * 
 * @author tyler.vangorder
 *
 */
class ReactiveTestingPlayground {

				private static final Logger logger = LoggerFactory.getLogger(PokerUtilitiesTest.class);

				static {
								ReactorDebugAgent.init();
				}

				@Test
				void reactorPlayground() throws InterruptedException {
								List<String> stringValues =
										Flux.interval(Duration.ofMillis(100))
												.take(10)
												.flatMap(yo -> Flux.just(yo.toString()))
												.doOnNext(this::info)
												.toStream().collect(Collectors.toList());

								assertThat(stringValues).hasSize(10);

			//		Thread.sleep(2000);
		
				}

				private void info(String letter) {
								logger.info("LETTER : " + letter);
				}


				/**
				 * Testing how to run a set of mono's (that are intentionally delayed for 2 seconds) can be
				 * run in parallel. Note: This can be achieved with merge, but if you use concat, the monos
				 * will be run serially.  
				 */
				@Test
				@DisplayName("Test parallel processing of Monos")
				@Timeout(3)
				void testParallel() {
								List<Mono<String>> descriptions = new ArrayList<>();
								//First setup 200 monos with a second second delay for each.
								for (int index = 0; index < 200; index++) {
												descriptions.add(Mono.just("Index " + index).delayElement(Duration.ofSeconds(2)));
								}

								//If we process in parallel, the time to complete the flux of those monos should be around 2 seconds.
								long start = System.currentTimeMillis();
								List<String> results = Flux.merge(descriptions).toStream().collect(Collectors.toList());
								System.out.println("Done with Processing in " + (System.currentTimeMillis() - start) + "ms");
								results.stream().forEach(System.out::println);
								assertThat(results).hasSize(200);
				}
}
