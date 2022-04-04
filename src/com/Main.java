package com;

import static akka.http.javadsl.server.PathMatchers.longSegment;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import com.receptionist.PrintActor;
import com.receptionist.PrintRequest;

import akka.actor.typed.ActorRef;
import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.ShardingEnvelope;
import akka.cluster.sharding.typed.javadsl.ClusterSharding;
import akka.cluster.sharding.typed.javadsl.Entity;
import akka.cluster.sharding.typed.javadsl.EntityRef;
import akka.cluster.sharding.typed.javadsl.EntityTypeKey;
import akka.discovery.Discovery;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.ExceptionHandler;
import akka.http.javadsl.server.Route;
import akka.management.cluster.bootstrap.ClusterBootstrap;
import akka.management.javadsl.AkkaManagement;

public class Main extends AllDirectives {

	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt("808"+args[0]);
		
		// boot up server using the route as defined below
		ActorSystem<PrintRequest> system = ActorSystem.create(PrintActor.create(), "routes");

		AkkaManagement.get(system).start();
		ClusterBootstrap.get(system).start();
		Discovery.get(system).loadServiceDiscovery("kubernetes-api");
		
		//////////////////////// Sharding //////////////////////////////////////////////

//------------------------------------------
		ClusterSharding sharding = ClusterSharding.get(system);
		EntityTypeKey<ShardOrderMessage> typeKey = EntityTypeKey.create(ShardOrderMessage.class, "ShardOrderMessage");
//------------------------------------------		
		
//		if(Integer.parseInt(args[0]) == 1) {
		
//---------------------------------------------------
			System.out.println("Creating shard region");
			ActorRef<ShardingEnvelope<ShardOrderMessage>> shardRegion =
					sharding.init(Entity.of(typeKey, ctx -> MessageReceiverShard.create(ctx.getEntityId(), Integer.parseInt(args[0]))));
//-----------------------------------------------------
		
//		}

//		EntityRef<ShardOrderMessage> entityRef = null;
	
//---------------------------------------------------------		
		EntityRef<ShardOrderMessage> entityRef = sharding.entityRefFor(typeKey, "orderMessage-1");
//---------------------------------------------------------
		///////////////////////////////////////////////////////////////////////////////
		
		final Http http = Http.get(system);

		// In order to access all directives we need an instance where the routes are
		// define.
		Main app = new Main();

		final CompletionStage<ServerBinding> binding = http.newServerAt("0.0.0.0", port).bind(app.createRoute(system, entityRef));

		for(;;)
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
//		System.out.println("Server online at http://localhost:" + port + "/\nPress RETURN to stop...");
//	    //System.in.read(); // let it run until user presses return
//
//	    binding
//	        .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
//	        .thenAccept(unbound -> system.terminate()); // and shutdown when done
	}
	
	private Route createRoute(ActorSystem<PrintRequest> system, EntityRef<ShardOrderMessage> shard) {
		final ExceptionHandler divByZeroHandler = ExceptionHandler.newBuilder()
                .match(Exception.class, x -> {
                        return complete(StatusCodes.BAD_REQUEST, "Error! You tried to divide with zero!");
                })
                .build();
		
		return concat(
			      get(() ->
			        pathPrefix("item", () ->
			          path(longSegment(), (Long id) -> {
			        	  
			        	  system.tell(new PrintRequest(id));
			        	  
			        	  RequestExecutor re = new RequestExecutor();
			              final CompletionStage<Optional<Item>> futureMaybeItem = re.fetchItem(id);
			              
			              return onSuccess(futureMaybeItem, maybeItem ->
			                maybeItem.map(item -> completeOK(item, Jackson.marshaller()))
			                  .orElseGet(() -> complete(StatusCodes.NOT_FOUND, "Not Found"))
			              );
//			        	return complete(new RequestExecutor().executeGet());
			          }))),
			      handleExceptions(
			    	  divByZeroHandler,
				      () -> post(() ->
				        path("create-order", () ->
				          entity(Jackson.unmarshaller(Order.class), order -> {
				        	  RequestExecutor re = new RequestExecutor();
				              CompletionStage<Object> futureSaved = null;
//				              CompletionStage<Object> futureSaved = re.saveOrder(system, order, shard);
				              return onSuccess(futureSaved, done -> {
				                return complete(((OrderMessage)done).getResponseMessage());
				              });
	//			        	  return complete(new RequestExecutor().executePost());
				          })))
				  )
			    );
	}

}
