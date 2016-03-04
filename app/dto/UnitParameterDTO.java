package dto;

import com.owera.xaps.dbi.UnitParameter;
import play.data.validation.Constraints;

import java.util.Optional;

public class UnitParameterDTO {
    private String unitId;
    @Constraints.Required
    private Integer unittypeParameterId;
    @Constraints.Required
    private String value;

    public UnitParameterDTO() {
    }

    public UnitParameterDTO(UnitParameter unitParameter) {
        this.setUnitId(Optional.of(unitParameter.getUnitId()).orElse(null));
        this.setUnittypeParameterId(unitParameter.getParameter().getUnittypeParameter().getId());
        this.setValue(unitParameter.getParameter().getValue());
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String id) {
        this.unitId = id;
    }

    public Integer getUnittypeParameterId() {
        return unittypeParameterId;
    }

    public void setUnittypeParameterId(Integer unittypeParameterId) {
        this.unittypeParameterId = unittypeParameterId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
