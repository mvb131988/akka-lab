package com;

import akka.actor.typed.ActorRef;

public class ShardOrderMessage implements Message {

	private final ActorRef<Message> replyTo;

	private Order order;
	
	public ShardOrderMessage(ActorRef<Message> replyTo, Order order) {
		super();
		this.replyTo = replyTo;
		this.order = order;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ActorRef<Message> getReplyTo() {
		return replyTo;
	}
	
}
