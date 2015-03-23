package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of Poll Service.
 */
public class PollServer {
    private static final Logger logger = Logger.getLogger(PollServer.class.getName());

    private final int port;
    private ServerImpl gRpcServer;
    private static String id="1ADC2FZ";

    public PollServer(int port) {
            this.port = port;
    }

    public void start() {
        gRpcServer = NettyServerBuilder.forPort(port)
                .addService(PollServiceGrpc.bindService(new PollServiceImpl()))
                .build().start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                PollServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    public void stop() {
        if (gRpcServer != null) {
            gRpcServer.shutdown();
        }
    }

    public static void main(String[] args) throws Exception {
        PollServer server = new PollServer(50051);
        server.start();
    }

    private static class PollServiceImpl implements PollServiceGrpc.PollService {

        @Override
        public void createPoll(PollRequest req, StreamObserver<PollResponse> responseObserver) {
            PollResponse reply = PollResponse.newBuilder().setId(id).build();
            responseObserver.onValue(reply);
            responseObserver.onCompleted();
			logger.info("Returning Moderator id.......");
			logger.info("--------------------------------------");
			logger.info("Moderator id is " +req.getModeratorId());
			logger.info("--------------------------------------");
        }


    }
}
