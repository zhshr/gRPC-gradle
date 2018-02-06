package us.acgn.grpc;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import us.acgn.grpc.Interceptors.IncomingMetadataInterceptor;

public class MyRpcServer {
  private static final Logger logger =Logger.getLogger(MyRpcServer.class.getName());

  private Server server;
  private final int port;
  private final Collection<BindableService> services;

  public MyRpcServer(int port, Collection<BindableService> services) {
    this.port = port;
    this.services = services;
  }

  public void start() throws IOException {
    ServerBuilder builder = ServerBuilder.forPort(port);

    services.forEach(
        service -> {
          logger.info("Adding service: " + service.getClass().getSimpleName());
          builder.addService(service);
        });
    builder.addService(ProtoReflectionService.newInstance());
    builder.intercept(new IncomingMetadataInterceptor());

    server = builder.build();
    server.start();
    logger.info("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        MyRpcServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  public void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }
}
