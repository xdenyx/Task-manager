package com.example.lab2;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ProcessesInfoController {
    @FXML
    private TableView<ProcessInfo> processTableView;
    @FXML
    private TableColumn<ProcessInfo, Integer> pidColumn;
    @FXML
    private TableColumn<ProcessInfo, String> nameColumn;

    private List<ProcessInfo> getProcessData() {
        SystemInfo systemInfo = new SystemInfo();
        OperatingSystem os = systemInfo.getOperatingSystem();

        List<ProcessInfo> processList = new ArrayList<>();

        List<OSProcess> processes = os.getProcesses(OperatingSystem.ProcessFiltering.ALL_PROCESSES, OperatingSystem.ProcessSorting.CPU_DESC, 50);

        for (OSProcess process : processes) {
            processList.add(new ProcessInfo(process.getProcessID(), process.getName()));
        }

        return processList;
    }
    public void initialize() {
        pidColumn.setCellValueFactory(new PropertyValueFactory<>("pid"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        processTableView.setItems(FXCollections.observableArrayList(getProcessData()));

        Duration duration = Duration.seconds(1);
        KeyFrame keyFrame = new KeyFrame(duration, event -> processTableView.setItems(FXCollections.observableArrayList(getProcessData())));
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public class ProcessInfo {
        private SimpleIntegerProperty pid;
        private SimpleStringProperty name;
        public ProcessInfo(int pid, String name) {
            this.pid = new SimpleIntegerProperty(pid);
            this.name = new SimpleStringProperty(name);
        }
        public int getPid() {
            return pid.get();
        }
        public SimpleIntegerProperty pidProperty() {
            return pid;
        }
        public String getName() {
            return name.get();
        }
        public SimpleStringProperty nameProperty() {
            return name;
        }
    }
}
