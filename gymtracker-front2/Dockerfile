# Stage 1: Build the React App using Create React App
FROM node:19-alpine AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available) to install dependencies
COPY package*.json ./

# Install dependencies using npm ci
RUN npm ci

# Copy the rest of the application files
COPY . .

# Run the build command using Create React App
RUN npm run build

# Debug step to verify the build folder creation
RUN ls -al /app/build  # This will list the files in /app/build after the build

# Stage 2: Serve the React App using Nginx
FROM nginx:alpine

# Ensure correct permissions for nginx
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx && \
    chown nginx.root /var/cache/nginx /var/run /var/log/nginx && \
    mkdir -p /etc/nginx/html/ && chmod 777 /etc/nginx/html/ && \
    sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf

# Copy the React build from the build stage to Nginx's html directory
COPY --from=build /app/build /usr/share/nginx/html/

# Copy the Nginx configuration file
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Expose the port Nginx is running on
EXPOSE 3000

# Set the user to nginx for security
USER nginx:root
