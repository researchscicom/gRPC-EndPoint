package com.grpc.reserve.server;

import com.grpc.reserve.CreateReservationRequest;
import com.grpc.reserve.GetReservationRequest;
import com.grpc.reserve.Reservation;
import com.grpc.reserve.ReservationServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class ReservationController extends ReservationServiceGrpc.ReservationServiceImplBase
{
	@Autowired
	private ReservationRepository reservationRepository;

	public ReservationController( ReservationRepository reservationRepository )
	{
		this.reservationRepository = reservationRepository;
	}

	@Override
	public void createReservation( CreateReservationRequest request, StreamObserver<Reservation> responseObserver )
	{
		Reservation res = Reservation.newBuilder().setId( request.getReservation().getId() ).setRoom( request.getReservation().getRoom() ).setVenue( request.getReservation().getVenue() ).
				setTitle( request.getReservation().getTitle() ).build();
		Reservation createdReservation = reservationRepository.createReservation( res );
		System.out.println( createdReservation.getRoom() );
		responseObserver.onNext( createdReservation );
		responseObserver.onCompleted();
	}

	@Override
	public void getReservation( GetReservationRequest request, StreamObserver<Reservation> responseObserver )
	{
		System.out.println( "getReservation() called" );
		Optional<Reservation> optionalReservation = reservationRepository.findReservation( request.getId() );
		if ( optionalReservation.isPresent() )
		{
			responseObserver.onNext( optionalReservation.orElse( Reservation.newBuilder().build() ) );
			responseObserver.onCompleted();
		}
		else
		{
			responseObserver.onError( Status.NOT_FOUND.withDescription( "no reservation with id " + request.getId() ).asRuntimeException() );
		}
	}
}
