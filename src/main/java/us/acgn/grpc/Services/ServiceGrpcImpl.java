package us.acgn.grpc.Services;

import io.grpc.stub.StreamObserver;
import us.acgn.grpc.ServiceGrpc.ServiceImplBase;
import us.acgn.grpc.ServiceOuterClass.Point;

public class ServiceGrpcImpl extends ServiceImplBase {
  @Override
  public void getFeature(Point point, StreamObserver<Point> observer) {
    observer.onNext(point);
    observer.onCompleted();
  }
}
