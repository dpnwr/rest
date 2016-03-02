package dto;

import com.owera.xaps.dbi.UnittypeParameter;
import play.data.validation.Constraints;

public class UnittypeParameterDTO {
    public Integer id;
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String flag;
    public UnittypeParameterValuesDTO values;

    public UnittypeParameterDTO() {
    }

    public UnittypeParameterDTO(UnittypeParameter param) {
        this.id = param.getId();
        this.name = param.getName();
        this.flag = param.getFlag().getFlag();
        if (param.getValues() != null) {
            this.values = new UnittypeParameterValuesDTO(param.getValues());
        }
    }
}
