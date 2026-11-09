FROM quay.io/keycloak/keycloak:latest

ADD --chown=keycloak:keycloak \
    https://raw.githubusercontent.com/aznamier/keycloak-event-listener-rabbitmq/target/keycloak-to-rabbit-3.0.5.jar \
    /opt/keycloak/providers/keycloak-to-rabbit-3.0.5.jar

RUN /opt/keycloak/bin/kc.sh build