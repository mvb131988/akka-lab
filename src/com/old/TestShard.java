package com.old;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.sharding.ClusterSharding;
import akka.cluster.sharding.ClusterShardingSettings;
import akka.cluster.sharding.ShardRegion;

public class TestShard {

	public static ActorRef startTestShard(ActorSystem system, 
									  String shardType, 
									  String role, 
									  int numberOfShards) 
	{
		return ClusterSharding.get(system)
			.start("TestShardActor", 
					Props.create(TestShardActor.class), 
					ClusterShardingSettings.apply(system).withRole(role), messageExtractor(numberOfShards));
	}
	
	private static ShardRegion.MessageExtractor messageExtractor(int numberOfShards) {
		return new ShardRegion.HashCodeMessageExtractor(numberOfShards) {

			@Override
			public String entityId(Object message) {
				if(message instanceof TestMessage) {
					return Long.toString(((TestMessage) message).getId());
				}
				if(message instanceof ReadTestMessage) {
					return Long.toString(((ReadTestMessage) message).getId());
				}
				return null;
			}
			
		};
	}
	
}
