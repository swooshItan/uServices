FROM nginx:latest

# Copy build output to corresponding nginx folder
COPY ./context/app /usr/share/nginx/html

# Copy config
COPY ./context/config/default.conf /etc/nginx/conf.d/default.conf

