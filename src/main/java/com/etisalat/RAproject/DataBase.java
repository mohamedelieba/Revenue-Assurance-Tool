package com.etisalat.RAproject;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.*;
import java.sql.*;

public class DataBase {
   public static String sqlQuery = "";

    public static String composeQuery(String fileName) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        }
        sqlQuery = sb.toString();
    }
    return sqlQuery;
    }

    public static void executeSingleQuery(File file, String query, String parameter, String queryType) {
        try (Connection connection = DriverManager.getConnection(DataBaseConfig.url, DataBaseConfig.userName, DataBaseConfig.decryptedPassword);
             PreparedStatement statement = connection.prepareStatement(query);
        ) {
            if (queryType.equals("TAP") || queryType.equals("BC")) {
                statement.setString(1, parameter);
            }
            else{
                System.out.println(parameter);
                String[] parts = parameter.split(":");
                String dateEntry = parts[0];
                String alarmType = parts[1];
                System.out.println(parts);
                statement.setString(1,dateEntry);
                statement.setString(2,alarmType);
            }
            ResultSet resultSet = statement.executeQuery();
            if (file != null) {
                BufferedWriter tap_out;
                StringBuilder result = new StringBuilder("");
                if (queryType.equals("TAP")) {
                    tap_out = new BufferedWriter(new FileWriter(file + ".csv"));
                    tap_out.write("Monitoring Point, Start date, Origin Address, Record Type, SUM(Event Duration), SUM(CDR Count), SUM(Data Volume)\n");
                    while (resultSet.next()) {
                        result = new StringBuilder(resultSet.getString(1));
                        result.append(",");
                        result.append(resultSet.getString(2));
                        result.append(",");
                        result.append(resultSet.getString(3));
                        result.append(",");
                        result.append(resultSet.getString(4));
                        result.append(",");
                        result.append(resultSet.getString(5));
                        result.append(",");
                        result.append(resultSet.getString(6));
                        result.append(",");
                        result.append(resultSet.getString(7));
                        result.append("\n");
                        tap_out.write(result.toString());
                    }
                } else {
                    tap_out = new BufferedWriter(new FileWriter(file + ".txt"));
                    tap_out.write("MSISDN, Alarm Description\n");
                    while (resultSet.next()) {
                        result = new StringBuilder(resultSet.getString(1));
                        result.append(",");
                        result.append(resultSet.getString(2));
                        result.append("\n");
                        tap_out.write(result.toString());
                    }
                }
                tap_out.close();
            }
        } catch (SQLException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Connection to DB has been closed", ButtonType.OK);
            alert.showAndWait();
            ex.printStackTrace();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }
}
