package dto;

import java.util.List;

import com.owera.xaps.dbi.UnittypeParameterValues;

public class UnittypeParameterValuesDTO {
    private String type;
    private String pattern;
    private List<String> values;

    public UnittypeParameterValuesDTO() {
    }

    public UnittypeParameterValuesDTO(UnittypeParameterValues values) {
        this.setType(values.getType());
        this.setPattern(values.getPattern().pattern());
        this.setValues(values.getValues());
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
