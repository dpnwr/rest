package dto;

import java.util.List;

import com.owera.xaps.dbi.UnittypeParameterValues;

public class UnittypeParameterValuesDTO {
    public String type;
    public String pattern;
    public List<String> values;

    public UnittypeParameterValuesDTO() {
    }

    public UnittypeParameterValuesDTO(UnittypeParameterValues values) {
        this.type = values.getType();
        this.pattern = values.getPattern().pattern();
        this.values = values.getValues();
    }

}
