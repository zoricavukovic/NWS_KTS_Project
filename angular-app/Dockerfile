# ### STAGE 1: Build ###
# FROM node:16.14.2 AS build
# WORKDIR /usr/src/app
# COPY package.json ./
# RUN npm install
# COPY . .
# RUN npm run build

# ### STAGE 2: Run ###
# FROM nginx:1.17.1-alpine
# COPY default.conf /etc/nginx/conf.d/default.conf
# COPY --from=build /usr/src/app/dist/angular-app /usr/share/nginx/html
# EXPOSE 4200

FROM node:16.14.2
WORKDIR /usr/src/app
COPY package.json .
RUN npm i -g @angular/cli@~14.2.6
RUN npm i
COPY . .
RUN ng build
CMD ng serve --host 0.0.0.0
