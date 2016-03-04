package dto;

import java.util.List;

import com.owera.xaps.dbi.UnittypeParameterValues;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UnittypeParameterValuesDTO {
    private String type;
    private String pattern;
    private List<String> values;

    public UnittypeParameterValuesDTO(UnittypeParameterValues values) {
        this.setType(values.getType());
        this.setPattern(values.getPattern().pattern());
        this.setValues(values.getValues());
    }
}
