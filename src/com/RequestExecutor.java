package com;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.ShardingEnvelope;
import akka.cluster.sharding.typed.javadsl.EntityRef;

import static akka.actor.typed.javadsl.AskPattern.ask;

public class RequestExecutor {

	public String executeGet() {
		return "<h1>Say hello to akka-http GET</h1>";
	}

	public String executePost() {
		return "<h1>Say hello to akka-http POST</h1>";
	}

	// (fake) async database query api
	public CompletionStage<Optional<Item>> fetchItem(long itemId) {
		return CompletableFuture.completedFuture(Optional.of(new Item("foo", itemId)));
	}

	// (fake) async database query api
	public CompletionStage<Object> saveOrder(ActorSystem<Message> system, final Order order, EntityRef<ShardOrderMessage> shard) {
		CompletionStage<Object> stage = ask(system, 
			replyTo -> new OrderMessage(replyTo, order, shard), 
			Duration.ofSeconds(3),
	        system.scheduler());
		
		return stage;
	}
}
