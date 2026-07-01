#!/bin/bash

if [[ -z "$STRIPE_SECRET" || -z "$KEYCLOAK_SECRET" ]]; then
    echo "Error: missing secret(s)"
    exit 1
fi

mvn clean install
export STRIPE_SECRET="$STRIPE_SECRET"
export KEYCLOAK_SECRET="$KEYCLOAK_SECRET"

if docker compose up -d --build; then
  echo "App successfully started"
fi