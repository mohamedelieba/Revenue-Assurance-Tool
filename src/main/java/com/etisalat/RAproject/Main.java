package com.etisalat.RAproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Main extends Application {

    private final TextField dateEntry = new TextField();
    private final HashMap<String,String> alarmsMapping = new HashMap<>();
    private final String [] alarms = {
            "The MSISDN exists in SDP but not in BSCS",
            "The MSISDN exists in SDP but not in CUDB",
            "The MSISDN exists in BSCS but not in CUDB",
            "The MSISDN exists in CUDB but not in BSCS",
            "The MSISDN exists in CUDB but not in SDP",
            "The MSISDN exists in BSCS but not in SDP",
            "BC Alarms",
            "Tap-KPIs"
    };

    private final ComboBox<String> alarmChooser = new ComboBox<>(FXCollections.observableArrayList(alarms));
    @Override
    public void start(Stage stage) throws IOException {
        alarmsMapping.put("The MSISDN exists in BSCS but not in SDP","BSCS-MDN-YvsSDP-MDN-N");
        alarmsMapping.put("The MSISDN exists in BSCS but not in CUDB","BSCS-MDN-YvsCUDB-MDN-N");
        alarmsMapping.put("The MSISDN exists in SDP but not in BSCS","BSCS-MDN-NvsSDP-MDN-Y");
        alarmsMapping.put("The MSISDN exists in CUDB but not in BSCS","BSCS-MDN-NvsCUDB-MDN-Y");
        alarmsMapping.put("The MSISDN exists in CUDB but not in SDP","CUDB-MDN-YvsSDP-MDN-N");
        alarmsMapping.put("The MSISDN exists in SDP but not in CUDB","CUDB-MDN-NvsSDP-MDN-Y");
        GridPane grid = new GridPane();
        FileChooser fileChooser = new FileChooser();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(dateEntry,1,1);
        Scene scene = new Scene(grid, 600, 400);
        stage.setScene(scene);
        stage.setTitle("RA Data Extraction Tool");
        Text sceneTitle = new Text("please Choose alarm type then Enter the date of your data!");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);
        Button button = new Button("Load Data");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(button);
        grid.add(hbBtn, 1, 4);
        final Text actionTarget = new Text();
        alarmChooser.setValue("Choose Alarm Type to load");
        grid.add(alarmChooser,0,1);

        button.setOnAction(e -> {
            if (alarmChooser.getValue().equals("Choose Alarm Type to load")) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "You must Choose alarm type", ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                    return;
                }
            }
            grid.add(actionTarget,1,6);
            actionTarget.setFill(Color.FIREBRICK);
            actionTarget.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR,24));
            actionTarget.setText("Please wait while we load your data");
            File file = fileChooser.showSaveDialog(stage);
            String query = null;
            if (alarmChooser.getValue().equals("BC Alarms")){
                try {
                    query = DataBase.composeQuery("BC_alarms.sql");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(query);
                DataBase.executeSingleQuery(file, query, dateEntry.getText(),"BC");
            }

            else if (alarmChooser.getValue().equals("Tap-KPIs")) {
                try {
                    query = DataBase.composeQuery("tap_query.sql");

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(query);
                DataBase.executeSingleQuery(file, query, dateEntry.getText(), "TAP");
            } else {
                try {
                    query = DataBase.composeQuery("PI_alarms.sql");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println(query);
                DataBase.executeSingleQuery(file, query, dateEntry.getText()+":"+alarmsMapping.get(alarmChooser.getValue()), "PI");
            }
            grid.getChildren().remove(actionTarget);
        });
        stage.show();
        DataBaseConfig.restoreDatabaseConfig();
    }

    public static void main(String[] args) {
        launch();
    }
}
