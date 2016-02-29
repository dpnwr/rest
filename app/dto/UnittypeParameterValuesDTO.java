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
        this.type = values.getType();
        this.pattern = values.getPattern().pattern();
        this.values = values.getValues();
    }

    public String getType() {
        return type;
    }

    public String getPattern() {
        return pattern;
    }

    public List<String> getValues() {
        return values;
    }

}
