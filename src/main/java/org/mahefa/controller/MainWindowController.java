package org.mahefa.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import org.mahefa.common.constants.SolarSystemTextures;
import org.mahefa.common.utils.StringUtils;
import org.mahefa.common.utils.TextureUtils;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.common.utils.math.astronomy.AstroMath;
import org.mahefa.common.utils.math.geometry.angle.Angle;
import org.mahefa.controller.javafx.Camera;
import org.mahefa.data.oracle.Xform;
import org.mahefa.data.meeus.jean.Coordinates;
import org.mahefa.data.view.DataView;
import org.mahefa.service.application.javafx.animation.AnimationAppService;
import org.mahefa.service.application.javafx.object.galaxy.GalaxyAppService;
import org.mahefa.service.application.javafx.physic.motion.MotionAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MainWindowController {

    @FXML AnchorPane anchorPane;
    @FXML HBox hBox;
    @FXML Button planetView, innerView, outerView;
    @FXML Label currentDate, currentTime;
    @FXML TableView tableView;
    @FXML SubScene subScene;

    @Autowired GalaxyAppService galaxyAppService;
    @Autowired MotionAppService motionAppService;
    @Autowired AnimationAppService animationAppService;
    @Autowired TextureUtils textureUtils;
    @Autowired Camera camera;

    @Value("${mouse.speed}") double mouseSpeed;
    @Value("${rotation.speed}") double rotationSpeed;
    @Value("${track.speed}") double trackSpeed;
    @Value("${date.format}") String dateFormat;
    @Value("${time.format}") String timFormat;

    final Group root = new Group();
    final Xform world = new Xform();

    private double mouseOldX, mouseOldY, mousePosY, mousePosX, deltaX, deltaY;
    private Node currentPivot;

    @FXML
    private void initialize() {
        // Bind managed property
        planetView.managedProperty().bind(planetView.visibleProperty());
        outerView.managedProperty().bind(outerView.visibleProperty());
        innerView.managedProperty().bind(innerView.visibleProperty());
        tableView.managedProperty().bind(tableView.visibleProperty());

        root.getChildren().addAll(world, camera.build());
        root.setDepthTest(DepthTest.ENABLE);

        world.getChildren().add(galaxyAppService.buildGalaxy());

        showCurrentDateTime();
        loadAstronomicalCoordinates();

        subScene.setRoot(root);
        subScene.setCamera(camera.getPerspectiveCamera());

        subScene.widthProperty().bind(anchorPane.widthProperty());
        subScene.heightProperty().bind(anchorPane.heightProperty());

        // Init event control
        nodeEventControl();
        initEventControls();
    }

    @FXML
    private void enterInnerView(ActionEvent event) {
        innerView.setVisible(false);
        planetView.setVisible(false);
        outerView.setVisible(true);
    }

    @FXML
    private void enterOuterView(ActionEvent event) {
        outerView.setVisible(false);
        planetView.setVisible(false);
        innerView.setVisible(true);

        camera.reset();

        resetPivot();
    }

    @FXML
    private void showAstronomicalCoordinates(ActionEvent event) {
        tableView.setVisible(!tableView.visibleProperty().getValue());
    }

    private void showCurrentDateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();

            currentDate.setText(localDate.format(DateTimeFormatter.ofPattern(dateFormat)));
            currentTime.setText(localTime.format(DateTimeFormatter.ofPattern(timFormat)));
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void loadAstronomicalCoordinates() {
        final List<Coordinates> coordinatesList =  galaxyAppService.getCurrentCoordinates();
        ObservableList<DataView> data = FXCollections.observableArrayList();
        DataView dataView = new DataView();

        coordinatesList.forEach(coordinates -> {
            try {
                final String designation = StringUtils.capitalize(coordinates.getDesignation());

                // Add columns
                TableColumn<DataView, String> tableColumn = new TableColumn<>(designation);
                TableColumn<DataView, String> nestedTableColumn1 = new TableColumn<>("R.A");
                TableColumn<DataView, String> nestedTableColumn2 = new TableColumn<>("Decl");

                final Method methodGet = DataView.class.getMethod(String.format("get%s", designation));

                tableColumn.getColumns().addAll(nestedTableColumn1, nestedTableColumn2);
                nestedTableColumn1.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> ((DataView.Data) methodGet.invoke(cellData.getValue())).getRa()));
                nestedTableColumn2.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> ((DataView.Data) methodGet.invoke(cellData.getValue())).getDecl()));

                tableView.getColumns().addAll(tableColumn);

                // Disable sorting
                tableColumn.setSortable(false);
                nestedTableColumn1.setSortable(false);
                nestedTableColumn2.setSortable(false);

                // Put data
                final String ra = Angle.convertDecimalDegreeToDMS(coordinates.getRightAscension());
                final String decl = String.format("%s°", AstroMath.round(coordinates.getDeclination(), 1e2));

                final Method methodSet = DataView.class.getMethod(String.format("set%s", designation), DataView.Data.class);
                methodSet.invoke(dataView, dataView.new Data(ra, decl));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        data.add(dataView);
        tableView.setItems(data);
    }

    private void nodeEventControl() {
        List<Node> nodes = getNodes(0);

        if(nodes != null && !nodes.isEmpty()) {
            nodes.forEach(node -> {
                node.setOnMouseClicked(event -> {
                    innerView.setVisible(false);
                    outerView.setVisible(false);
                    planetView.setVisible(true);
                    planetView.setText(node.getId().toUpperCase());
                    planetView.setId(node.getId().toLowerCase());

                    // Get special class
                    planetView.getStyleClass().removeAll("isRingSystem");

                    if(node.getStyleClass() != null && node.getStyleClass().size() > 0) {
                        final String specialClass = node.getStyleClass().get(0);
                        planetView.setStyle(specialClass);
                    }

                    // Reset previous pivot
                    resetPivot();

                    // Load texture
                    textureUtils.setMaps(SolarSystemTextures.getDiffuseMap(node.getId().toLowerCase()), null, null, null);
                    ((Sphere)node).setMaterial(textureUtils.getPhongMaterial());

                    // Lock on pivot
                    currentPivot = node;

                    camera.gettingCloser();

                    final LocalDateTime localDateTime = LocalDateTime.now();
                    final double JDE = JulianDay.getJulianDay(
                            localDateTime.getYear(), localDateTime.getMonth().getValue(), localDateTime.getDayOfMonth(),
                            localDateTime.getHour(), localDateTime.getMinute(), localDateTime.getSecond()
                    );
                    final double T = JulianDay.inJulianCenturies(JDE);
                    final double a = AstroMath.horner(T, galaxyAppService.getCelestialBody(node.getId()).getOrbitalCharacteristic().getSemiMajorAxis());
                    final double n = Angle.normalize(0.9856076686 / (a * Math.sqrt(a))) * 1e-1;

                    animationAppService.add("rotate", motionAppService.rotate(node, n));
                    animationAppService.add("focus", camera.lock(currentPivot));
                });

                node.setOnMouseEntered(event -> subScene.setCursor(Cursor.HAND));
                node.setOnMouseExited(event -> subScene.setCursor(Cursor.DEFAULT));
            });
        }
    }

    private void initEventControls() {
        subScene.addEventHandler(ScrollEvent.SCROLL, event -> {
            PerspectiveCamera perspectiveCamera = camera.getPerspectiveCamera();
            double delta = event.getDeltaY();
            double z = perspectiveCamera.getTranslateZ();

            perspectiveCamera.setTranslateZ(z + delta * mouseSpeed);

            // TODO: update scale
        });

        subScene.setOnMousePressed(event -> {
            mousePosX = event.getSceneX();
            mousePosY = event.getSceneY();
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        });

        subScene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = event.getX();
            mousePosY = event.getY();
            deltaX = mousePosX - mouseOldX;
            deltaY = mousePosY - mouseOldY;

            Xform cameraXform = camera.getCameraXform();
            Xform cameraXform2 = camera.getCameraXform2();

            if(event.isPrimaryButtonDown()) {
                cameraXform.ry.setAngle(cameraXform.ry.getAngle() + deltaX * mouseSpeed * rotationSpeed);
                cameraXform.rx.setAngle(cameraXform.rx.getAngle() + deltaY * mouseSpeed * rotationSpeed);
            } else if(event.isSecondaryButtonDown()) {
                cameraXform2.t.setX(cameraXform2.t.getX() + deltaX * mouseSpeed * trackSpeed);
                cameraXform2.t.setY(cameraXform2.t.getY() + deltaY * mouseSpeed * trackSpeed);
            }
        });
    }

    /**
     * Reset pivot
     */
    void resetPivot() {
        if(currentPivot != null) {
            final String id = currentPivot.getId();

            animationAppService.remove("focus", "rotate");
            camera.movingFarAway();

            if(id.equalsIgnoreCase("sun")) {
                final String diffuseMap = SolarSystemTextures.getDiffuseMap(id.toLowerCase());
                textureUtils.setMaps(diffuseMap, null, null, diffuseMap);
            } else {
                textureUtils.setColor(SolarSystemTextures.getColor(id.toLowerCase()));
            }

            ((Sphere) currentPivot).setMaterial(textureUtils.getPhongMaterial());
        }
    }

    List<Node> getNodes(int index) {
        return ((Xform) ((Xform) ((Xform) world.getChildren().get(0)).getChildren().get(0)).getChildren().get(index)).getChildren();
    }
}
