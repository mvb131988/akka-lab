package com.old;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeoutException;

import com.receptionist.PrintActor;
import com.receptionist.PrintRequest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.discovery.Discovery;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;


public class Main {

	public static void main(String[] args) throws TimeoutException, InterruptedException {
		int port = Integer.parseInt(System.getenv("HTTP_PORT"));
		
		Config config = ConfigFactory.load();
		ActorSystem system = ActorSystem.create("routes", config);

		System.out.println("old main");
		
		AkkaManagement.get(system).start();
		ClusterBootstrap.get(system).start();
		Discovery.get(system).loadServiceDiscovery("kubernetes-api");
		
		//start shard region
		ActorRef shardRegion = TestShard.startTestShard(system, null, null, 2);
		
		final Http http = Http.get(system);
		//int port = Integer.parseInt("808"+args[0]);
//		//port = Integer.parseInt(System.getProperty("DCLUSTERPORT"));
		final CompletionStage<ServerBinding> binding = 
				http.newServerAt("0.0.0.0", port).bind(new OldRoutes().route(system, shardRegion));
		
		for(;;) {
			Thread.sleep(1000);
		}
	}
	
}
