package com.aptatek.pkulab.device.bluetooth.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResultResponse {

    private String date;
    private List<ResultData> result;
    private String assay;
    private String valid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ResultData> getResult() {
        return result;
    }

    public void setResult(List<ResultData> results) {
        this.result = results;
    }

    public String getAssay() {
        return assay;
    }

    public void setAssay(String assay) {
        this.assay = assay;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "ResultResponse{" +
                "date='" + date + '\'' +
                ", result=" + result +
                ", assay='" + assay + '\'' +
                ", valid='" + valid + '\'' +
                '}';
    }

    public static class ResultData {

        private String units;
        @SerializedName("numerical_result")
        private float numericalResult;
        private String name;

        public String getUnits() {
            return units;
        }

        public void setUnits(String units) {
            this.units = units;
        }

        public float getNumericalResult() {
            return numericalResult;
        }

        public void setNumericalResult(float numericalResult) {
            this.numericalResult = numericalResult;
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
                    "units='" + units + '\'' +
                    ", numericalResult=" + numericalResult +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
