package org.homepoker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class HomePoker {

	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(HomePoker.class, args);
	}
}
