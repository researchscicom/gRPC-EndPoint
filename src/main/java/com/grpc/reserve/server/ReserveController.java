package com.grpc.reserve.server;

import com.grpc.reserve.CreateReservationRequest;
import com.grpc.reserve.GetReservationRequest;
import com.grpc.reserve.Reservation;
import com.grpc.reserve.ReservationServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

class ReserveController extends ReservationServiceGrpc.ReservationServiceImplBase{
    @Autowired
    private ReserverRepository reservationRepository;

    public ReserveController(ReserverRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<Reservation> responseObserver) {
        Reservation res = Reservation.newBuilder().setId(request.getReservation().getId())
                .setRoom("W002").setVenue("UCSC").setTitle("Java").build();
        Reservation createdReservation = reservationRepository.createReservation(res);
        responseObserver.onNext(createdReservation);
        responseObserver.onCompleted();
    }
    @Override
    public void getReservation(GetReservationRequest request, StreamObserver<Reservation> responseObserver) {
        System.out.println("getReservation() called");
        Optional<Reservation> optionalReservation = reservationRepository.findReservation(request.getId());
        if (optionalReservation.isPresent()) {
            responseObserver.onNext(optionalReservation.orElse(Reservation.newBuilder().build()));
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("no reservation with id " + request.getId())
                    .asRuntimeException());
        }
    }
}
