package com.example.lab2;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.util.Locale;

public class cpuInfoController {
    @FXML
    public TabPane cpuTabPane;
    @FXML
    public Tab cpuInfoTab;
    @FXML
    public Tab cpuLoadingTab;
    @FXML
    public Label cpuLoadingLabel;
    @FXML
    public Label cpuInfoLabel;
    private final SystemInfo systemInfo = new SystemInfo();
    private final CentralProcessor processor = systemInfo.getHardware().getProcessor();
    private boolean CPULoadMeasured = false;
    private String lastCPULoad;

    public void initialize(){
        cpuInfoLabel.setText(getCPUInfo());
    }
    @FXML
    private void onCPULoadingTabSelected(Event event) {
        if (cpuTabPane.getSelectionModel().getSelectedItem().getText().equals("CPU loading")) {
            if (!CPULoadMeasured) {
                lastCPULoad = getCPULoad();
                CPULoadMeasured = true;
            }
            cpuLoadingLabel.setText(lastCPULoad);
        }
    }
    public void activateInformationTab() {
        cpuTabPane.getSelectionModel().select(0);
    }
    public void activateCPULoadTab() {
        cpuTabPane.getSelectionModel().select(1);
    }
    private String getCPUInfo() {
        String cpuName = processor.getProcessorIdentifier().getName();
        String cpuModel = processor.getProcessorIdentifier().getModel();
        String cpuVersion = processor.getProcessorIdentifier().getStepping();
        int cpuCores = processor.getLogicalProcessorCount();
        return "Назва: " + cpuName + "\n" +
                "Модель: " + cpuModel + "\n" +
                "Версія: " + cpuVersion + "\n" +
                "Кількість ядер: " + cpuCores;
    }
    private String getCPULoad() {
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        return String.format(Locale.ROOT, "CPU load: %.1f%%",
                processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100);
    }
}