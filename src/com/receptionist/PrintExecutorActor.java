package com.receptionist;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.receptionist.Receptionist;
import akka.actor.typed.receptionist.ServiceKey;

public class PrintExecutorActor extends AbstractBehavior<PrintRequest> {

	public static Behavior<PrintRequest> create(long id) {
		return Behaviors.setup(context -> {
			ServiceKey<PrintRequest> pingServiceKey =
				      ServiceKey.create(PrintRequest.class, Long.toString(id));

			context.getSystem().receptionist().tell(Receptionist.register(pingServiceKey, context.getSelf()));
			
			return new PrintExecutorActor(context);
		});
	}
	
	public PrintExecutorActor(ActorContext<PrintRequest> context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Receive<PrintRequest> createReceive() {
		// TODO Auto-generated method stub
		return null;
	}

}
