FROM node:9.2.0-alpine as node_modules_installer

# install native deps build tools
RUN apk add --no-cache make gcc g++ python bash
RUN apk add --update openssl

# Create app directory
RUN mkdir -p /usr/data-sender
WORKDIR /usr/data-sender

# Bundle app source
COPY package.json .
RUN npm install

FROM node:9.2.0-alpine
RUN apk add --no-cache bash

# Create app directory
RUN mkdir -p /usr/data-sender
WORKDIR /usr/data-sender

COPY --from=node_modules_installer /usr/data-sender/node_modules/ ./node_modules
COPY . .

# Remote debug port
EXPOSE 5858

RUN ls /usr/data-sender

ENTRYPOINT ["/bin/bash", "-c", "/usr/data-sender/docker-entrypoint.sh"]