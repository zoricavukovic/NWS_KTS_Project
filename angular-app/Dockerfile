FROM node:16.14.2
WORKDIR /usr/src/app
COPY package.json .
RUN npm i -g @angular/cli@~14.2.6
RUN npm i
COPY . .
RUN ng build
CMD ng serve --host 0.0.0.0
