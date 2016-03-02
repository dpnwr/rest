package dto;

import com.owera.xaps.dbi.UnittypeParameter;
import play.data.validation.Constraints;

public class UnittypeParameterDTO {
    private Integer id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String flag;
    private UnittypeParameterValuesDTO values;

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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public UnittypeParameterValuesDTO getValues() {
        return values;
    }


}
