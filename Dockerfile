# Stage 1: The build environment, with all the tools
# We use a Gradle image that has JDK 21 installed.
FROM gradle:8.7-jdk21-focal AS builder

# Set the working directory inside the container
WORKDIR /home/gradle/project

# Copy the entire project into the container
COPY . .

# Grant execution permission to the Gradle wrapper
RUN chmod +x ./gradlew

# Build the application using the Gradle wrapper.
# The '-Dquarkus.package.type=fast-jar' is an optimization for containerized builds.
RUN ./gradlew build -Dquarkus.package.type=fast-jar


# Stage 2: The final, lightweight production image
# We use a minimal Java runtime image provided by Quarkus.
FROM quarkus/ubi-quarkus-mandrel-builder-image:jdk-21 AS runner

# Set the working directory for the application
WORKDIR /app

# Copy only the necessary runtime files from the 'builder' stage
# This makes the final image much smaller and more secure.
COPY --from=builder /home/gradle/project/build/quarkus-app/ ./

# Expose the port the application will run on
EXPOSE 8080

# The command to run the application when the container starts
CMD ["java", "-jar", "quarkus-run.jar"]