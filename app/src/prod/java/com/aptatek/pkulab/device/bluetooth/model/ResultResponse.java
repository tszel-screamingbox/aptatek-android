package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultResponse {

    private String date;
    @SerializedName("overall_result")
    private String overallResult;
    private String temperature;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("product_serial")
    private String productSerial;
    private String humidity;
    @SerializedName("hardware_version")
    private String hardwareVersion;
    @SerializedName("firmware_version")
    private String firmwareVersion;
    @SerializedName("config_hash")
    private String configHash;
    @SerializedName("assay_hash")
    private String assayHash;
    private CassetteData cassette;
    @SerializedName("assay_version")
    private Long assayVersion;
    @SerializedName("software_version")
    private String softwareVersion;
    private String mode;
    private List<ResultData> result;
    private String assay;
    private boolean valid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOverallResult() {
        return overallResult;
    }

    public void setOverallResult(String overallResult) {
        this.overallResult = overallResult;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHardwareVersion() {
        return hardwareVersion;
    }

    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public String getConfigHash() {
        return configHash;
    }

    public void setConfigHash(String configHash) {
        this.configHash = configHash;
    }

    public String getAssayHash() {
        return assayHash;
    }

    public void setAssayHash(String assayHash) {
        this.assayHash = assayHash;
    }

    public CassetteData getCassette() {
        return cassette;
    }

    public void setCassette(CassetteData cassette) {
        this.cassette = cassette;
    }

    public Long getAssayVersion() {
        return assayVersion;
    }

    public void setAssayVersion(Long assayVersion) {
        this.assayVersion = assayVersion;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public List<ResultData> getResult() {
        return result;
    }

    public void setResult(List<ResultData> result) {
        this.result = result;
    }

    public String getAssay() {
        return assay;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "ResultResponse{" +
                "date='" + date + '\'' +
                ", overallResult='" + overallResult + '\'' +
                ", temperature='" + temperature + '\'' +
                ", endDate='" + endDate + '\'' +
                ", productSerial='" + productSerial + '\'' +
                ", humidity='" + humidity + '\'' +
                ", hardwareVersion='" + hardwareVersion + '\'' +
                ", firmwareVersion='" + firmwareVersion + '\'' +
                ", configHash='" + configHash + '\'' +
                ", assayHash='" + assayHash + '\'' +
                ", cassette=" + cassette +
                ", assayVersion=" + assayVersion +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", mode='" + mode + '\'' +
                ", result=" + result +
                ", assay='" + assay + '\'' +
                ", valid=" + valid +
                '}';
    }

    public static class CassetteData {

        private String expiry;
        private String date;
        private Long lot;

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Long getLot() {
            return lot;
        }

        public void setLot(Long lot) {
            this.lot = lot;
        }

        @Override
        public String toString() {
            return "CassetteData{" +
                    "expiry='" + expiry + '\'' +
                    ", date='" + date + '\'' +
                    ", lot=" + lot +
                    '}';
        }
    }

    public static class ResultData {

        @SerializedName("assay_name")
        private String assayName;
        private String units;
        @SerializedName("numerical_result")
        private Float numericalResult;
        @SerializedName("text_result")
        private String textResult;
        private String name;

        public String getAssayName() {
            return assayName;
        }

        public void setAssayName(String assayName) {
            this.assayName = assayName;
        }

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public Float getNumericalResult() {
            return numericalResult;
        }

        public void setNumericalResult(Float numericalResult) {
            this.numericalResult = numericalResult;
        }

        public String getTextResult() {
            return textResult;
        }

        public void setTextResult(String textResult) {
            this.textResult = textResult;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "ResultData{" +
                    "assayName='" + assayName + '\'' +
                    ", units='" + units + '\'' +
                    ", numericalResult=" + numericalResult +
                    ", textResult='" + textResult + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
