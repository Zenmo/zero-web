FROM node:20.4.0

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install

COPY *.json .
COPY src/* src/
RUN node src/init.mjs

ENV PORT=80
CMD node src/app.mjs
