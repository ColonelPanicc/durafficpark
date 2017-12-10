![Duraffic Park](https://raw.githubusercontent.com/OliMac1/durafficpark/master/frontend/static/durafficpark.png)
# DurafficPark
Welcome to Duraffic Park.

We've made traffic simulations so astounding that they'll capture the imagination
of the entire planet.

This is a product of [Oliver McLeod](https://github.com/OliMac1), [Ryan Collins](https://github.com/OhmGeek), [Mike Croall](https://github.com/MikeCroall), [George Price](https://github.com/GeorgePrice), and [Sara Chen](https://github.com/sara-h-chen). Built at Durhack, 2018.

## Running the application
First, you will want to run the Java worker. Using a terminal, navigate to the
root of the repository, and then:

```bash
mvn clean install
java -jar target/durafficpark-1.0-SNAPSHOT.jar
```

Then run the nodejs server, by running:

```bash
npm start
```

## Deploying to Heroku
The code is designed to work with Heroku.

Enable both the Java and nodejs buildpacks, and then deploy (using the included Procfile). This will then work out of the box.
