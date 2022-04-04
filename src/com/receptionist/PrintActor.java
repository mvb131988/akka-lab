package com.receptionist;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

public class PrintActor extends AbstractBehavior<PrintRequest> {

	private static class ListingResponse extends PrintRequest {
		final Receptionist.Listing listing;

		private ListingResponse(Receptionist.Listing listing) {
			super(-1);
			this.listing = listing;
		}
	}

	private final ActorContext<PrintRequest> context;
	private final ActorRef<Receptionist.Listing> listingResponseAdapter;

	public static Behavior<PrintRequest> create() {
		return Behaviors.setup(PrintActor::new);
	}

	public PrintActor(ActorContext<PrintRequest> context) {
		super(context);
		this.context = context;
		this.listingResponseAdapter = context.messageAdapter(Receptionist.Listing.class, ListingResponse::new);
	}

	@Override
	public Receive<PrintRequest> createReceive() {
		return newReceiveBuilder().onMessage(PrintRequest.class, this::onStart)
				.onMessage(ListingResponse.class, response -> onListing(response.listing)).build();
	}

	private Behavior<PrintRequest> onStart(PrintRequest command) {
		context.getSystem()
			   .receptionist()
			   .tell(
					   Receptionist.find(
							   ServiceKey.create(PrintRequest.class, Long.toString(command.getId())), 
							   listingResponseAdapter)
				);

//		ActorRef<PrintRequest> destination = getContext().spawn(PrintExecutorActor.create(command.getId()),
//				Long.toString(command.getId()));
//		destination.tell(command);
		return this;
	}
	
	private Behavior<PrintRequest> onListing(Receptionist.Listing msg) {
		System.out.println(msg.getKey());
		return Behaviors.same();
	}

}
