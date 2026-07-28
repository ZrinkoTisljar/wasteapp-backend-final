FROM eclipse-temurin:21-jdk AS build

WORKDIR /app
#kopira backend projekt u kontejner
COPY . .

#Daje dozvolu Maven Wrapper skripti i zatim pokreće Maven izgradnju
# Maven Wrapper omogućuje korištenje
# odgovarajuće verzije Mavena bez njegove zasebne instalacije u image
# clean package briše predhodne rezultate i stvara izvršnu .jar datoteku
# -DskipTest preskaće pokretanje testova tijekom produkcijske Doker izgradnje
RUN chmod +x mvnw && ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre
#postavlja /app kao radni direktorij unutar Docker kontejnera
WORKDIR /app

# Time Docker pronalazi generirani .jar bez obzira na verziju ili naziv projekta i kopira ga pod nazivom app.jar
COPY --from=build /app/target/*.jar app.jar
# označava da aplikacija koristi lokalno port 8080
EXPOSE 8080
#pokreće Spring Boot aplikaciju
ENTRYPOINT ["java", "-jar", "app.jar"]