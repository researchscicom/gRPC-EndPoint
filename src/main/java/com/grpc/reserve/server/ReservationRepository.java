package com.grpc.reserve.server;

import com.grpc.reserve.Reservation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ReservationRepository
{
	private Map<String, Reservation> reservationById = new HashMap<>();

	public Optional<Reservation> findReservation( String id )
	{
		return Optional.ofNullable( reservationById.get( id ) );
	}

	public Reservation createReservation( Reservation reservation )
	{
		String id = UUID.randomUUID().toString();
		Reservation reservationWithId = reservation.toBuilder().setId( id ).build();

		reservationById.put( id, reservationWithId );

		return reservationWithId;
	}
}
