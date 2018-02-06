package us.acgn.grpc.Interceptors;

import io.grpc.Attributes.Key;
import io.grpc.Grpc;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import us.acgn.grpc.RequestLogOuterClass.RequestLog;

public class IncomingMetadataInterceptor implements ServerInterceptor {

  private static final Logger logger = Logger.getLogger(IncomingMetadataInterceptor.class.getName());
  private final List<RequestLog> requestLogs = new ArrayList<>();

  private <ReqT, RespT> void logRequest(ServerCall<ReqT, RespT> call, Metadata headers) {
    RequestLog.Builder logBuilder =
        RequestLog.newBuilder()
            .setMethodName(call.getMethodDescriptor().getFullMethodName())
            .setRequestType(call.getMethodDescriptor().getType().name());

    InetSocketAddress address =
        (InetSocketAddress)call.getAttributes().get(Grpc.TRANSPORT_ATTR_REMOTE_ADDR);
    logBuilder.setClientIp(address.toString());

    logger.info(logBuilder.build().toString());
    requestLogs.add(logBuilder.build());
  }

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers,
      ServerCallHandler<ReqT, RespT> next) {
    logger.info(call.getAttributes().toString());
    logRequest(call, headers);
    return next.startCall(call, headers);
  }

}
