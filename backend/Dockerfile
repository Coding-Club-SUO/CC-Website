# Use Node.js base image
FROM node:20-alpine

# Set the working directory
WORKDIR /app

# Copy dependencies and install
COPY package*.json .
RUN npm install

# Copy the rest of the backend code
COPY . .

# Expose the backend port
EXPOSE 8000

# Start the Express app
RUN npm run build
CMD ["node", "dist/index.js"]