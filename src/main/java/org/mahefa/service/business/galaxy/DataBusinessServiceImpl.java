package org.mahefa.service.business.galaxy;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mahefa.common.constants.OrbitalElements;
import org.mahefa.common.utils.JsonUtils;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.Galaxy;
import org.mahefa.data.OrbitalCharacteristic;
import org.mahefa.data.PhysicalCharacteristic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataBusinessServiceImpl implements DataBusinessService {

    private static final String ORBITAL_CHARACTERISTIC = "orbitalCharacteristic";
    private static final String PHYSICAL_CHARACTERISTIC = "physicalCharacteristic";

    @Autowired
    private JsonUtils jsonUtils;

    @Value("${galaxy.location.file}")
    private String galaxyLocationFile;

    @Override
    public List<Galaxy> loadGalaxies() {
        JSONObject jsonObject = jsonUtils.readJsonFile(galaxyLocationFile);
        List<Galaxy> galaxies = new ArrayList<>();

        if(jsonObject != null) {
            JSONArray galaxyJSONArray = (JSONArray)jsonObject.get("galaxies");

            // Load galaxy
            for(int gl = 0; gl < galaxyJSONArray.length(); gl++) {
                JSONObject galaxyJSONObject = galaxyJSONArray.getJSONObject(gl);
                Galaxy.Builder galaxy = new Galaxy.Builder(galaxyJSONObject.getString("designation"));

                // Load star
                galaxy.addStars(loadCelestialBodies(galaxyJSONObject.getJSONArray("stars")));

                // Load planet
                galaxy.addPlanets(loadCelestialBodies(galaxyJSONObject.getJSONArray("planets")));

                galaxies.add(galaxy.build());
            }
        }

        return galaxies;
    }

    List<CelestialBody> loadCelestialBodies(JSONArray celestialBodyJSONArray) {
        List<CelestialBody> celestialBodies = new ArrayList<>();

        for(int cb = 0; cb < celestialBodyJSONArray.length(); cb++) {
            JSONObject celestialBodyJSONObject = celestialBodyJSONArray.getJSONObject(cb);
            JSONObject orbitalCharacteristicJSONObject = celestialBodyJSONObject.getJSONObject(ORBITAL_CHARACTERISTIC);
            JSONObject physicalCharacteristicJSONObject = celestialBodyJSONObject.getJSONObject(PHYSICAL_CHARACTERISTIC);

            final String designation = celestialBodyJSONObject.getString("designation");
            final double[] semiMajorAxis = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.SEMI_MAJOR_AXIS.toValue()));
            final double[] eccentricities = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.ECCENTRICITY.toValue()));
            final double[] inclinations = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.INCLINATION.toValue()));
            final double[] longitudeAscendingNodes = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.LONGITUDE_OF_ASCENDING_NODE.toValue()));
            final double[] longitudesPerihelion = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.LONGITUDE_OF_PERIHELION.toValue()));
            final double[] meanLongitudes = loadPolynomialValues(orbitalCharacteristicJSONObject.getJSONArray(OrbitalElements.MEAN_LONGITUDE.toValue()));

            PhysicalCharacteristic physicalCharacteristic = new PhysicalCharacteristic.Builder(
                    physicalCharacteristicJSONObject.getDouble("radius"), physicalCharacteristicJSONObject.getDouble("mass")
            )
                    .setAxialTilt(physicalCharacteristicJSONObject.getDouble("axialTilt"))
                    .isRingSystem(physicalCharacteristicJSONObject.getBoolean("isRingSystem"))
                    .build();

            OrbitalCharacteristic orbitalCharacteristic = new OrbitalCharacteristic.Builder(semiMajorAxis)
                    .setInclinations(inclinations)
                    .setEccentricities(eccentricities)
                    .setLongitudeAscendingNodes(longitudeAscendingNodes)
                    .setLongitudesPerihelion(longitudesPerihelion)
                    .setMeanLongitudes(meanLongitudes)
                    .build();

            CelestialBody.Builder planet = new CelestialBody.Builder(designation, orbitalCharacteristic, physicalCharacteristic)
                    .setCategory(celestialBodyJSONObject.getString("celestialBodyCategory"));

            planet.addCelestialBodies(loadCelestialBodies((JSONArray)celestialBodyJSONObject.getJSONArray("celestialBodies")));

            celestialBodies.add(planet.build());
        }

        return celestialBodies;
    }

    double[] loadPolynomialValues(JSONArray orbitalElements) {
        double[] polynomials = new double[4];

        for(int i = 0; i < polynomials.length; i++) {
            polynomials[i] = orbitalElements.getDouble(i);
        }

        return polynomials;
    }
}
