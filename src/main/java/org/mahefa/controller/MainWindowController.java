package org.mahefa.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.util.Duration;
import org.mahefa.common.constants.CelestialBodyCategory;
import org.mahefa.common.constants.TabColumns;
import org.mahefa.common.utils.TextureUtils;
import org.mahefa.common.utils.calendar.julian_day.JulianDay;
import org.mahefa.controller.javafx.Camera;
import org.mahefa.data.CelestialBody;
import org.mahefa.data.UserData;
import org.mahefa.data.oracle.Xform;
import org.mahefa.data.view.DataView;
import org.mahefa.service.application.javafx.animation.Animation;
import org.mahefa.service.application.javafx.object.galaxy.Galaxy;
import org.mahefa.service.application.javafx.physic.motion.Motion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MainWindowController {

    @FXML AnchorPane anchorPane;
    @FXML HBox controlButtonBox;
    @FXML VBox descriptionBox;
    @FXML Button planetView, innerView, outerView;
    @FXML Label currentDate, currentTime, title;
    @FXML TableView<DataView> tableView;
    @FXML SubScene subScene;
    @FXML Pane imagePanel;

    @Autowired Galaxy galaxy;
    @Autowired Motion motion;
    @Autowired Animation animation;
    @Autowired Camera camera;

    @Value("${mouse.speed}") double mouseSpeed;
    @Value("${rotation.speed}") double rotationSpeed;
    @Value("${track.speed}") double trackSpeed;
    @Value("${date.format}") String dateFormat;
    @Value("${time.format}") String timFormat;
    @Value("${camera.initial.distance}") double initialDistance;
    @Value("${camera.close.range}") double closeRangeDistance;

    final Group root = new Group();
    final Xform world = new Xform();

    private double mouseOldX, mouseOldY, mousePosY, mousePosX, deltaX, deltaY;
    private Node currentPivot;
    List<Object> controls = new ArrayList<>();

    @FXML
    private void initialize() {
        // Bind managed property
        managedControl();

        root.getChildren().addAll(world, camera.build());
        root.setDepthTest(DepthTest.ENABLE);

        world.getChildren().add( galaxy.buildGalaxy(JulianDay.getJDEAt()));

        showCurrentDateTime();

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
        if(descriptionBox.visibleProperty().getValue())
            animation.fadeOut(descriptionBox);

        if(tableView.visibleProperty().getValue())
            animation.fadeOut(tableView);

        if(innerView.visibleProperty().getValue())
            animation.fadeOut(innerView);

        if(planetView.visibleProperty().getValue())
            animation.fadeOut(planetView);

        animation.fadeIn(outerView);
    }

    @FXML
    private void enterOuterView(ActionEvent event) {
        if(descriptionBox.visibleProperty().getValue())
            animation.fadeOut(descriptionBox);

        if(tableView.visibleProperty().getValue())
            animation.fadeOut(tableView);

        if(outerView.visibleProperty().getValue())
            animation.fadeOut(outerView);

        if(planetView.visibleProperty().getValue())
            animation.fadeOut(planetView);

        animation.fadeIn(innerView);

        camera.release();
        resetPivot();
    }

    @FXML
    private void showDescriptionBox(ActionEvent event) {
        if(!descriptionBox.visibleProperty().getValue()) {
            animation.fadeIn(descriptionBox);
            tableView.setVisible(false);

            if(currentPivot != null) {
                final String pivotOn = currentPivot.getId();
                title.setText(pivotOn.toUpperCase());
                imagePanel.getStyleClass().add(pivotOn.toLowerCase());
            }
        }
    }

    @FXML
    private void showAstronomicalCoordinates(ActionEvent event) {
        loadAstronomicalCoordinates();
        closeDescriptionBox(event);

        if(tableView.visibleProperty().getValue()) {
            animation.fadeOut(tableView);
            return;
        }

        animation.fadeIn(tableView);
    }

    @FXML
    private void closeDescriptionBox(ActionEvent event) {
        animation.fadeOut(descriptionBox);
    }

    private void showCurrentDateTime() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime localTime = LocalTime.now();
            LocalDate localDate = LocalDate.now();

            currentDate.setText(localDate.format(DateTimeFormatter.ofPattern(dateFormat)));
            currentTime.setText(localTime.format(DateTimeFormatter.ofPattern(timFormat)));
        }), new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    private void loadAstronomicalCoordinates() {
        final List<DataView> dataViews = galaxy.getInfo(JulianDay.getJDEAt());
        ObservableList<DataView> data = FXCollections.observableArrayList();

        // Add columns
        if(CollectionUtils.isEmpty(tableView.getColumns())) {
            TableColumn<DataView, String> col0 = new TableColumn<>(TabColumns._BLANK.toValue());
            col0.setCellValueFactory(new PropertyValueFactory<>("designation"));

            TableColumn<DataView, String> col1 = new TableColumn<>(TabColumns.RIGHT_ASCENSION.toValue());
            col1.setCellValueFactory(new PropertyValueFactory<>("ra"));

            TableColumn<DataView, String> col2 = new TableColumn<>(TabColumns.DECLINATION.toValue());
            col2.setCellValueFactory(new PropertyValueFactory<>("decl"));

            TableColumn<DataView, String> col3 = new TableColumn<>(TabColumns.MAGNITUDE.toValue());
            col3.setCellValueFactory(new PropertyValueFactory<>("magnitude"));

            TableColumn<DataView, String> col4 = new TableColumn<>(TabColumns.SIZE.toValue());
            col4.setCellValueFactory(new PropertyValueFactory<>("size"));

            TableColumn<DataView, String> col5 = new TableColumn<>(TabColumns.PHASE.toValue());
            col5.setCellValueFactory(new PropertyValueFactory<>("phase"));

            TableColumn<DataView, String> col6 = new TableColumn<>(TabColumns.RISE.toValue());
            col6.setCellValueFactory(new PropertyValueFactory<>("rise"));

            TableColumn<DataView, String> col7 = new TableColumn<>(TabColumns.TRANSIT.toValue());
            col7.setCellValueFactory(new PropertyValueFactory<>("transit"));

            TableColumn<DataView, String> col8 = new TableColumn<>(TabColumns.SET.toValue());
            col8.setCellValueFactory(new PropertyValueFactory<>("set"));

            tableView.getColumns().addAll(col0, col1, col2, col3, col4, col5, col6, col7, col8);

            // Disable sorting
            col0.setSortable(false);
            col1.setSortable(false); col2.setSortable(false); col3.setSortable(false); col4.setSortable(false);
            col5.setSortable(false); col6.setSortable(false); col7.setSortable(false); col8.setSortable(false);
        }

        // Put data
        data.addAll(dataViews);
        tableView.setItems(data);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void nodeEventControl() {
        List<Node> nodes = ((Xform) ((Xform) ((Xform) world.getChildren().get(0)).getChildren().get(0)).getChildren().get(0)).getChildren();

        if(nodes != null && !nodes.isEmpty()) {
            nodes.forEach(node -> {
                node.setOnMouseClicked(event -> {
                    innerView.visibleProperty().setValue(false);
                    outerView.visibleProperty().setValue(false);
                    tableView.visibleProperty().setValue(false);
                    planetView.visibleProperty().setValue(true);

                    // Get special class
                    planetView.getGraphic().getStyleClass().remove("isRingSystem");

                    final UserData userData = (UserData) node.getUserData();
                    final CelestialBody celestialBody = userData.getCelestialBody();
                    final String id = celestialBody.getDesignation().toLowerCase();

                    planetView.setText(id.toUpperCase());
                    planetView.setId(id);

                    if(celestialBody.getPhysicalCharacteristic().isRingSystem())
                        planetView.getGraphic().getStyleClass().add("isRingSystem");

                    // Reset previous pivot
                    resetPivot();

                    // Load texture
                    ((Sphere)node).setMaterial(TextureUtils.getTexture(id));

                    // Lock on pivot
                    camera.lockOn(node);

                    // Animate node
                    AnimationTimer rotateAnimation = animation.rotate(node, 0.005);
                    userData.setRotateAnimation(rotateAnimation);

                    currentPivot = node;
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
            final double value = z + delta * mouseSpeed;

            if(value < initialDistance || value > closeRangeDistance)
                return;

            perspectiveCamera.setTranslateZ(value);

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
     * Stop and destroy animation (rotation)
     * Revert texture to default color
     * Remove style class on the image panel
     */
    void resetPivot() {
        if(currentPivot != null) {
            final UserData userData = (UserData) currentPivot.getUserData();
            final CelestialBody celestialBody = userData.getCelestialBody();
            AnimationTimer rotate = userData.getRotateAnimation();

            if(rotate != null) {
                rotate.stop();
                userData.setRotateAnimation(null);
            }

            final String id = celestialBody.getDesignation().toLowerCase();
            PhongMaterial phongMaterial = (celestialBody.getCelestialBodyCategory().equals(CelestialBodyCategory.STAR))
                    ? TextureUtils.getTexture(id)
                    : TextureUtils.getTextureColor(id);

            ((Sphere) currentPivot).setMaterial(phongMaterial);
            imagePanel.getStyleClass().remove(id);
        }
    }

    void managedControl() {
        planetView.managedProperty().bind(planetView.visibleProperty());
        outerView.managedProperty().bind(outerView.visibleProperty());
        innerView.managedProperty().bind(innerView.visibleProperty());
        tableView.managedProperty().bind(tableView.visibleProperty());
        descriptionBox.managedProperty().bind(descriptionBox.visibleProperty());
    }
}
