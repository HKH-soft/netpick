FROM node:22.16-alpine
WORKDIR /app
COPY package*.json .
RUN npm i --silent
COPY . .
EXPOSE 5173
CMD [ "npm" , "run" , "dev" ]