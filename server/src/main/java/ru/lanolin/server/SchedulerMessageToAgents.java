package ru.lanolin.server;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.lanolin.common.Command;
import ru.lanolin.common.Message;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = { @Autowired })
public class SchedulerMessageToAgents {

	private final AgentParameters agentParameters;
	private final RSocketRequester.Builder rsocketRequesterBuilder;
	private final RSocketStrategies strategies;
	private Message msg;

	@PostConstruct
	public void postConstruct() {
		msg = Message.builder()
				.id("server")
				.command(Command.Ping)
				.message(UUID.randomUUID().toString())
				.build();
	}

	private void agentSend(AgentConnectionParameter agentConnection) {
		RSocketRequester requester = agentConnection.getRequester();
		if (requester == null) {
			requester = rsocketRequesterBuilder.rsocketStrategies(strategies)
					.tcp(agentConnection.getIp(), agentConnection.getPort());
			agentConnection.setRequester(requester);
		}

		requester.route("info.ping-pong")
				.data(msg)
				.retrieveMono(Message.class)
				.doOnNext(message -> {
					// TODO: check message to return
					log.info("Next {}", message);
					agentConnection.setAvailable(true);
				})
				.doOnError(throwable -> {
					log.info("Error {}", throwable.getLocalizedMessage());
					agentConnection.setAvailable(false);
					agentConnection.setRequester(null);
				})
				.subscribe();
	}

	@Scheduled(fixedDelay = 5000L)
	public void pingPong() {
		agentParameters.getAgents().forEach(this::agentSend);
	}

}
