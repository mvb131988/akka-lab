package com;

import java.time.Duration;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.cluster.sharding.typed.ShardingEnvelope;

public class MessageReceiver extends AbstractBehavior<Message> {

	public static Behavior<Message> create() {
		return Behaviors.setup(MessageReceiver::new);
	}

	private ActorContext<Message> context;

	private ActorRef<Object> replyTo;
	
	public MessageReceiver(ActorContext<Message> context) {
		super(context);
		this.context = context;
	}

	@Override
	public Receive<Message> createReceive() {
		return newReceiveBuilder()
		        .onMessage(OrderMessage.class, this::onOrderMessage)
		        .onMessage(OrderMessageResponse.class, this::onReceive)
		        .build();
	}
	
	private Behavior<Message> onOrderMessage(OrderMessage orderMessage) {
		Order order = orderMessage.getOrder();
		replyTo = orderMessage.getReplyTo();
		//orderMessage.getShard().tell(new ShardOrderMessage(this.getContext().getSelf(), order));
		
		final Duration timeout = Duration.ofSeconds(3);
		context.ask(Message.class, 
					orderMessage.getShard(),
					timeout,
					(ActorRef<Message> ref) -> new ShardOrderMessage(ref, order),
					(response, throwable) -> {
				          System.out.println(response);
				          return response;
			        });
		
		return this;
	}

	private Behavior<Message> onReceive(OrderMessageResponse message) {
		replyTo.tell(new OrderMessage(null, null, message.getResponse()));
		return this;
	}
	
}
