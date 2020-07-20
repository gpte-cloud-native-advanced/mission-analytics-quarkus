#### Mission Analytics Service

* Implemented with Quarkus 1.5.0.Final


To create Kafka source::
apiVersion: sources.knative.dev/v1alpha1
kind: KafkaSource
metadata:
  name: kafka-source
spec:
  bootstrapServers:
   - kafka-cluster-kafka-bootstrap:9092
  topics:
   - topic-mission-event
  sink:
    ref:
      apiVersion: serving.knative.dev/v1
      kind: Service
      name: mission-analytics-serverless-quarkus-hello

