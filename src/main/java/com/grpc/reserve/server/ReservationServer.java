package com.grpc.reserve.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class ReservationServer
{
	public static void main( String[] args ) throws IOException, InterruptedException
	{

		ReservationRepository repository = new ReservationRepository();

		Server service = ServerBuilder.forPort( 53000 ).addService( new ReservationController( repository ) ).build().start();

		Runtime.getRuntime().addShutdownHook( new Thread( service::shutdownNow ) );
		System.out.println( "Started listening for rpc calls on 53000..." );
		service.awaitTermination();
	}
}
