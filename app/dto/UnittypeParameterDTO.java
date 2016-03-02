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
        this.setId(param.getId());
        this.setName(param.getName());
        this.setFlag(param.getFlag().getFlag());
        if (param.getValues() != null) {
            this.setValues(new UnittypeParameterValuesDTO(param.getValues()));
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public UnittypeParameterValuesDTO getValues() {
        return values;
    }

    public void setValues(UnittypeParameterValuesDTO values) {
        this.values = values;
    }
}
