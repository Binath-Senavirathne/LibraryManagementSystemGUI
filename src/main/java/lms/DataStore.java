package lms;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    private List<StringProperty> data;
    private StringProperty error;

    public DataStore(List<String> rawData, String errorMsg) {
        data = new ArrayList<>();
        if (rawData != null) {
            for (String val : rawData) {
                data.add(new SimpleStringProperty(val != null ? val : ""));
            }
        }
        this.error = new SimpleStringProperty(errorMsg != null ? errorMsg : "");
    }

    public StringProperty getColumnProperty(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index);
        }
        return new SimpleStringProperty("");
    }

    public String getColumn(int index) {
        if (index >= 0 && index < data.size()) {
            return data.get(index).get();
        }
        return "";
    }

    public void setColumn(int index, String value) {
        if (index >= 0 && index < data.size()) {
            data.get(index).set(value != null ? value : "");
        }
    }

    public StringProperty errorProperty() {
        return error;
    }

    public String getError() {
        return error.get();
    }

    public void setError(String errorMsg) {
        this.error.set(errorMsg != null ? errorMsg : "");
    }

    public int getColumnCount() {
        return data.size();
    }
}
