package com;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;

public class MessageReceiverShard extends AbstractBehavior<ShardOrderMessage> {

	public static Behavior<ShardOrderMessage> create(String entityId, int nodeId) {
		return Behaviors.setup(cxt -> new MessageReceiverShard(cxt, entityId, nodeId));
	}

	private ActorContext<ShardOrderMessage> context;
	
	private String entityId;
	
	private int nodeId;
	
	public MessageReceiverShard(ActorContext<ShardOrderMessage> context, String entityId, int nodeId) {
		super(context);
		this.context = context;
		this.entityId = entityId;
		this.nodeId = nodeId;
	}

	@Override
	public Receive<ShardOrderMessage> createReceive() {
		return newReceiveBuilder()
		        .onMessage(ShardOrderMessage.class, this::onOrderMessage)
		        .build();
	}
	
	private Behavior<ShardOrderMessage> onOrderMessage(ShardOrderMessage orderMessage) {
		orderMessage.getReplyTo().tell(new OrderMessageResponse("Inside sharded actor" + nodeId));
		System.out.println("Inside sharded actor" + nodeId);
		return this;
	}
	
}
