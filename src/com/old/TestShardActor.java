package com.old;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.actor.ReceiveTimeout;

public class TestShardActor extends AbstractActor {

	private TestMessage original;
	
	public TestShardActor() {
		getContext().setReceiveTimeout(Duration.ofMinutes(1));
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(TestMessage.class, m -> handle(m))
				.match(ReadTestMessage.class, m -> handleRead(m))
				.match(ReceiveTimeout.class, m -> selfDestroy())
				.build();
	}
	
	private void handle(TestMessage m) {
		original = m;
		System.out.println("Message received: " + m.getId());
	}
	
	private void handleRead(ReadTestMessage m) {
		ReadTestMessageResponse resp = new ReadTestMessageResponse(original.getMessage());
		sender().tell(resp, self());
	}
	
	private void selfDestroy() {
		context().stop(self());
	}

}
