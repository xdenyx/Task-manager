package com.example.lab2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class SystemInfoController {
    @FXML
    public MenuItem cpuInfoMenuItem;
    @FXML
    public MenuItem cpuLoadMenuItem;
    @FXML
    private Label SystemInfoLabel;
    private Timeline timeline;



    @FXML
    protected void onGetMenuOSInfoClick(ActionEvent actionEvent){
        String systemInfo = GetSystemInfo();
        SystemInfoLabel.setText(systemInfo);
    }
    @FXML
    protected void onGetMemoryInfoClick(ActionEvent actionEvent) {
        updateMemoryInfo();

        // Create a timeline to update the memory info every second
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateMemoryInfo();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    private void updateMemoryInfo() {
        SystemInfo si = new SystemInfo();
        HardwareAbstractionLayer hal = si.getHardware();
        SystemInfoLabel.setText(printMemory(hal.getMemory()));
    }
    @FXML
    protected void onGetMenuCPUInfoClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("cpuInfo.fxml"));
        Scene scene = new Scene(fxmlloader.load());

        cpuInfoController cpuInfoController = fxmlloader.getController();
        MenuItem menuItem = (MenuItem) actionEvent.getSource();

        if (menuItem.getText().equals("Information")) {
            cpuInfoController.activateInformationTab();
        } else if (menuItem.getText().equals("CPU loading")) {
            cpuInfoController.activateCPULoadTab();
        }
        Stage stage = new Stage();
        stage.setTitle("CPU Information");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    protected void onGetProcInfoClick(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("processesInfo.fxml"));
        Scene scene = new Scene(fxmlloader.load());
        Stage stage = new Stage();
        stage.setTitle("Processes Information");
        stage.setScene(scene);
        stage.show();
    }
    private String GetSystemInfo(){

        SystemInfo systemInfo = new SystemInfo();

        OperatingSystem operatingSystem = systemInfo.getOperatingSystem();

        String osName = operatingSystem.getFamily();
        String osVersion = operatingSystem.getVersionInfo().toString();
        String osBuildNumber = operatingSystem.getVersionInfo().getBuildNumber();
        Instant osBooted = Instant.ofEpochSecond(operatingSystem.getSystemBootTime());
        String osUptime = FormatUtil.formatElapsedSecs(operatingSystem.getSystemUptime());

        return "OS Name: " + osName + "\nOS Version: " + osVersion + "\nOS Build Number: " + osBuildNumber + "\nOS Booted: "  + osBooted + "\nOS Uptime: " + osUptime;
    }
    private static String printMemory(GlobalMemory memory) {
        List<String> oshi = new ArrayList<>();
        oshi.add("Physical Memory: \n " + memory.toString());
        VirtualMemory vm = memory.getVirtualMemory();
        oshi.add("\nVirtual Memory: \n " + vm.toString());
        List<PhysicalMemory> pmList = memory.getPhysicalMemory();
        if (!pmList.isEmpty()) {
            oshi.add("\nPhysical Memory: ");
            for (PhysicalMemory pm : pmList) {
                oshi.add(" \n" + pm.toString());
            }
        }
        return oshi.toString();
    }
}