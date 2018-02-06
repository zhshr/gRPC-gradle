package us.acgn.grpc;

import com.google.common.collect.ImmutableSet;
import io.grpc.Server;
import java.io.IOException;
import java.util.logging.Logger;
import us.acgn.grpc.Services.ServiceGrpcImpl;

public class Main {
  private static final Logger logger = Logger.getLogger(Main.class.getName());

  private Server server;

  public static void main(String[] args) throws IOException, InterruptedException {
    MyRpcServer myServer = new MyRpcServer(2222, ImmutableSet.of(new ServiceGrpcImpl()));
    myServer.start();
    myServer.blockUntilShutdown();
  }
}
