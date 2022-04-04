package com.old;

import static akka.http.javadsl.server.PathMatchers.longSegment;

import java.time.Duration;
import java.util.concurrent.CompletionStage;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.StatusCode;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;

public class OldRoutes extends AllDirectives {

	public Route route(ActorSystem system, ActorRef shardRegion) {
		Route r = 
		      concat(
				 get(() ->
			        pathPrefix("item", () ->
			          path(longSegment(), (Long id) -> {
			        	  
			        	  CompletionStage<Object> stage = 
			        			  Patterns.ask(shardRegion, 
			        			  			   new TestMessage(id), 
			        			  			   Duration.ofMillis(1000)).toCompletableFuture();
			        			
	  		              return onSuccess(stage, maybeItem -> complete(StatusCodes.NOT_FOUND, "Not Found"));
			          }))),
			      post(() ->
			        path("save-message", () ->
			          entity(Jackson.unmarshaller(TestMessage.class), m -> {
			        	  CompletionStage<Object> stage = 
			        			  Patterns.ask(shardRegion, 
			        			  			   m, 
			        			  			   Duration.ofMillis(1000)).toCompletableFuture();
			        			
	  		              return onSuccess(stage, maybeItem -> complete(StatusCodes.OK, "Saved"));
			          }))),
			      post(() ->
			        path("read-message", () ->
			          entity(Jackson.unmarshaller(ReadTestMessage.class), m -> {
			        	  CompletionStage<Object> stage = 
			        			  Patterns.ask(shardRegion, 
			        			  			   m, 
			        			  			   Duration.ofMillis(1000)).toCompletableFuture();
			        	  
	  		              return onSuccess(stage, 
	  		            		  		   resp -> complete(StatusCodes.OK, 
	  		            		  				   			(ReadTestMessageResponse)resp, 
	  		            		  				   			Jackson.marshaller())
	  		            		  		  );
			          })))
		      );
		
		return r;
	}
	
}
