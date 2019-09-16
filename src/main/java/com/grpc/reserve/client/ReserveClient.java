package com.grpc.reserve.client;

import com.grpc.reserve.CreateReservationRequest;
import com.grpc.reserve.Reservation;
import com.grpc.reserve.ReservationServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ReserveClient {
    public static void main(String args[])
    {
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("localhost", 53000)
                .usePlaintext() // disable TLS which is enabled by default and requires certificates
                .build();

        ReservationServiceGrpc.ReservationServiceBlockingStub
                reservationClient = ReservationServiceGrpc.newBlockingStub(channel);

        String createdReservationId = createReservation(reservationClient, "JDriven Coltbaan 3", "meeting-room");
        System.out.println(createdReservationId);
    }

    private static String createReservation(ReservationServiceGrpc.ReservationServiceBlockingStub reservationClient, String venue, String room) {
        System.out.println("--- Creating reservation ---");
        Reservation newReservation = Reservation.newBuilder()
                .setTitle("Lunchmeeting")
                .setVenue(venue)
                .setRoom(room)
                .build();

        Reservation createdReservationResponse = reservationClient.createReservation(
                CreateReservationRequest.newBuilder()
                        .setReservation(newReservation)
                        .build());

        String createdReservationId = createdReservationResponse.getId();
        System.out.println("response: " + createdReservationResponse);
        return createdReservationId;
    }
}
