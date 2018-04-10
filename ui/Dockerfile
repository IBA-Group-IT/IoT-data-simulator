#Build ui bundle stage
FROM node:9.10.0-alpine as bundle-builder

# Install native deps build tools
RUN apk add --no-cache make gcc g++ python

RUN	mkdir -p /usr/app
WORKDIR /usr/app
COPY public-src public-src

WORKDIR /usr/app/public-src
RUN npm install
RUN npm run build:prod

# Final image build with node env and static bundle
FROM node:9.10.0-alpine

RUN apk add --update --no-cache openssl

RUN	mkdir -p /usr/app
WORKDIR /usr/app
	
COPY package.json .
RUN npm install --only=production
COPY config.js .
COPY server.js .
COPY bin bin
COPY --from=bundle-builder /usr/app/public/ public 

ENV NPM_CONFIG_LOGLEVEL error
ENV NODE_ENV production
ENV HOST "0.0.0.0"
EXPOSE 8090

CMD ["npm", "start"]
