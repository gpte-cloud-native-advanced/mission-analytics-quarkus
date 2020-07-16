#### Mission Analytics Service

* Implemented with Quarkus 1.5.0.Final

To build::
$ ./mvnw clean package -Dquarkus.container-image.build=true -Dquarkus.kubernetes-client.trust-certs=true
$ ./mvnw clean package -Dquarkus.kubernetes.deploy=true -Dquarkus.kubernetes-client.trust-certs=true

To create Knative service:
$ export REGISTRY=default-route-openshift-image-registry.apps.$(oc whoami --show-server | cut -d. -f2- | cut -d: -f1)
$ podman login -u $(oc whoami) -p $(oc whoami -t) ${REGISTRY}
$ skopeo copy docker://${REGISTRY}/user2-er-demo/mission-analytics-service-quarkus:1.0-SNAPSHOT docker://quay.io/nandanj/mission-analytics-serverless-quarkus:latest --dest-creds nandanj:<your password>
$ podman login quay.io/nandanj -u nandanj -p <your password>
$ oc create -f service.yml 
service.serving.knative.dev/mission-analytics-serverless-quarkus created
$ oc get knative
NAME                                                                     LATESTCREATED                                LATESTREADY                                  READY   REASON
configuration.serving.knative.dev/mission-analytics-serverless-quarkus   mission-analytics-serverless-quarkus-hello   mission-analytics-serverless-quarkus-hello   True    
service.serving.knative.dev/mission-analytics-serverless-quarkus   http://mission-analytics-serverless-quarkus-user2-er-demo.apps.cluster-71e5.71e5.example.opentlc.com   mission-analytics-serverless-quarkus-hello   mission-analytics-serverless-quarkus-hello   True    
revision.serving.knative.dev/mission-analytics-serverless-quarkus-hello   mission-analytics-serverless-quarkus   mission-analytics-serverless-quarkus-hello   1            True    
route.serving.knative.dev/mission-analytics-serverless-quarkus   http://mission-analytics-serverless-quarkus-user2-er-demo.apps.cluster-71e5.71e5.example.opentlc.com   True


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


To create Broker and Trigger::

