package dto;

import com.owera.xaps.dbi.UnittypeParameter;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UnittypeParameterDTO {
    private Integer id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String flag;
    private UnittypeParameterValuesDTO values;

    public UnittypeParameterDTO(UnittypeParameter param) {
        this.setId(param.getId());
        this.setName(param.getName());
        this.setFlag(param.getFlag().getFlag());
        if (param.getValues() != null) {
            this.setValues(new UnittypeParameterValuesDTO(param.getValues()));
        }
    }
}
