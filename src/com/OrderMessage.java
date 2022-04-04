package com;

import akka.actor.typed.ActorRef;
import akka.cluster.sharding.typed.javadsl.EntityRef;

public class OrderMessage implements Message {

	private EntityRef<ShardOrderMessage> shard; 

	private final ActorRef<Object> replyTo;

	private Order order;

	private String responseMessage;
	
	public OrderMessage(ActorRef<Object> replyTo, Order order, EntityRef<ShardOrderMessage> shard) {
		this.replyTo = replyTo;
		this.order = order;
		this.shard = shard;
	}

	public OrderMessage(ActorRef<Object> replyTo, Order order, String m) {
		this.replyTo = replyTo;
		this.order = order;
		this.responseMessage = m;
	}
	
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public ActorRef<Object> getReplyTo() {
		return replyTo;
	}
	
	public String getResponseMessage() {
		return responseMessage;
	}
	
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public EntityRef<ShardOrderMessage> getShard() {
		return shard;
	}

	public void setShard(EntityRef<ShardOrderMessage> shard) {
		this.shard = shard;
	}
}
