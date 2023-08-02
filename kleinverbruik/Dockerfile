FROM node:20.4.0

WORKDIR /app

COPY package.json package-lock.json tsconfig.json ./
RUN npm install

COPY src/* src/
RUN npm run build
RUN node src/init.mjs

ENV PORT=80
CMD node src/app.mjs
