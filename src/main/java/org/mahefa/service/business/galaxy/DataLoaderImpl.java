package org.mahefa.service.business.galaxy;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.Galaxy;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.PhysicalCharacteristic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataLoaderImpl implements DataLoader {

    @Value("${galaxy.location.file}") private String galaxyLocationFile;

    @Override
    public List<Galaxy> loadGalaxies() {
        List<Galaxy> galaxies = new ArrayList<>();

        try(InputStream inputStream = getClass().getResourceAsStream(galaxyLocationFile)) {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(inputStream);

            if(jsonParser != null) {
                jsonParser.nextToken(); // Start Object {
                JsonToken jsonToken = jsonParser.nextToken();

                if (jsonToken == JsonToken.FIELD_NAME && "galaxies".equals(jsonParser.getCurrentName())) {
                    Galaxy.Builder galaxy = null;
                    jsonToken = jsonParser.nextToken(); // Start Array [

                    while (jsonToken != JsonToken.END_ARRAY) {
                        jsonToken = jsonParser.nextToken();

                        if (jsonToken == JsonToken.START_OBJECT) // Start Object {
                            continue;

                        final String currentName = jsonParser.getCurrentName();

                        if (jsonToken == JsonToken.FIELD_NAME && "designation".equals(currentName)) {
                            jsonToken = jsonParser.nextToken();

                            if (jsonToken == JsonToken.VALUE_STRING) {
                                galaxy = new Galaxy.Builder(jsonParser.getText());
                            }
                        } else if (jsonToken == JsonToken.FIELD_NAME && "planets".equals(currentName)) {
                            galaxy.addPlanets(loadCelestialBodies(jsonParser));
                        } else if (jsonToken == JsonToken.FIELD_NAME && "stars".equals(currentName)) {
                            galaxy.addStars(loadCelestialBodies(jsonParser));
                        }

                        if (jsonToken == JsonToken.END_OBJECT) // End Object }
                            galaxies.add(galaxy.build());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return galaxies;
    }

    List<CelestialBody> loadCelestialBodies(JsonParser jsonParser) throws Exception {
        List<CelestialBody> celestialBodies = new ArrayList<>();
        jsonParser.nextToken(); // Start Array [
        JsonToken jsonToken = jsonParser.nextToken(); // Start Object {

        String designation = null;
        String celestialBodyCategory = null;
        PhysicalCharacteristic physicalCharacteristic = null;
        OrbitalCharacteristic orbitalCharacteristic = null;

        while (jsonToken != JsonToken.END_ARRAY) { // End Array ]
            jsonToken = jsonParser.nextToken();

            if (jsonToken == JsonToken.FIELD_NAME) {
                switch (jsonParser.getCurrentName()) {
                    case "designation":
                        jsonToken = jsonParser.nextToken();
                        designation = jsonParser.getText();
                        break;
                    case "celestialBodyCategory":
                        jsonToken = jsonParser.nextToken();
                        celestialBodyCategory = jsonParser.getText();
                        break;
                    case "celestialBodies":
                        loadCelestialBodies(jsonParser);
                        break;
                    case "orbitalCharacteristic":
                        orbitalCharacteristic = getOrbitalCharacteristic(jsonParser);
                        break;
                    case "physicalCharacteristic":
                        physicalCharacteristic = getPhysicalCharacteristic(jsonParser);
                        break;
                }
            }

            if(jsonToken == JsonToken.END_OBJECT) { // End Object }
                CelestialBody.Builder planet = new CelestialBody.Builder(designation, orbitalCharacteristic, physicalCharacteristic)
                        .setCategory(celestialBodyCategory);

//                planet.addCelestialBodies(loadCelestialBodies((JSONArray)celestialBodyJSONObject.getJSONArray("celestialBodies")));

                celestialBodies.add(planet.build());
            }
        }

        return celestialBodies;
    }

    PhysicalCharacteristic getPhysicalCharacteristic(JsonParser jsonParser) throws IOException {
        double radius = 0d;
        double mass = 0d;
        double axialTilt = 0d;
        boolean isRingSystem = false;

        jsonParser.nextToken(); // Start Object {

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "axialTilt".equalsIgnoreCase(jsonParser.getCurrentName())) {
            if(jsonParser.nextToken() == JsonToken.VALUE_NUMBER_FLOAT)
                axialTilt = jsonParser.getDoubleValue();
        }

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "mass".equalsIgnoreCase(jsonParser.getCurrentName())) {
            if(jsonParser.nextToken() == JsonToken.VALUE_NUMBER_FLOAT)
                mass = jsonParser.getDoubleValue();
        }

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "radius".equalsIgnoreCase(jsonParser.getCurrentName())) {
            if(jsonParser.nextToken() == JsonToken.VALUE_NUMBER_FLOAT)
                radius = jsonParser.getDoubleValue();
        }

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "isRingSystem".equalsIgnoreCase(jsonParser.getCurrentName())) {
            if(jsonParser.nextToken() == JsonToken.VALUE_TRUE)
                isRingSystem = jsonParser.getBooleanValue();
        }

        jsonParser.nextToken(); // End Object }

        return new PhysicalCharacteristic.Builder(radius, mass)
                .setAxialTilt(axialTilt)
                .isRingSystem(isRingSystem)
                .build();
    }

    OrbitalCharacteristic getOrbitalCharacteristic(JsonParser jsonParser) throws Exception {
        double[] semiMajorAxis = new double[4];
        double[] inclinations = new double[4];
        double[] eccentricities = new double[4];
        double[] longitudeAscendingNodes = new double[4];
        double[] longitudesPerihelion = new double[4];
        double[] meanLongitudes = new double[4];

        jsonParser.nextToken(); // Start Object {

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "L".equalsIgnoreCase(jsonParser.getCurrentName()))
            meanLongitudes = loadPolynomialValues(jsonParser);

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "a".equalsIgnoreCase(jsonParser.getCurrentName()))
            semiMajorAxis = loadPolynomialValues(jsonParser);

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "e".equalsIgnoreCase(jsonParser.getCurrentName()))
            eccentricities = loadPolynomialValues(jsonParser);

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "i".equalsIgnoreCase(jsonParser.getCurrentName()))
            inclinations = loadPolynomialValues(jsonParser);

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "Ω".equalsIgnoreCase(jsonParser.getCurrentName()))
            longitudeAscendingNodes = loadPolynomialValues(jsonParser);

        if(jsonParser.nextToken() == JsonToken.FIELD_NAME && "Π".equalsIgnoreCase(jsonParser.getCurrentName()))
            longitudesPerihelion = loadPolynomialValues(jsonParser);

        jsonParser.nextToken(); // End Object }

        return new OrbitalCharacteristic.Builder(semiMajorAxis)
                .setInclinations(inclinations)
                .setEccentricities(eccentricities)
                .setLongitudeAscendingNodes(longitudeAscendingNodes)
                .setLongitudesPerihelion(longitudesPerihelion)
                .setMeanLongitudes(meanLongitudes)
                .build();
    }

    double[] loadPolynomialValues(JsonParser jsonParser) throws Exception {
        JsonToken jsonToken = jsonParser.nextToken(); // Start Array [

        if(jsonToken != JsonToken.START_ARRAY)
            throw new Exception("Not an array");

        double[] data = new double[4];
        int i = 0;

        while (i < 4) {
            jsonToken = jsonParser.nextToken();

            if(jsonToken == JsonToken.VALUE_NUMBER_FLOAT)
                data[i++] = jsonParser.getDoubleValue();
        }

        jsonParser.nextToken(); // End Array ]

        return data;
    }
}
