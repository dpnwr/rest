package dto;

import com.owera.xaps.dbi.UnitParameter;

public class UnitParameterDTO {
    private String unitId;
    private Integer unittypeParameterId;
    private String value;

    public UnitParameterDTO() {
    }

    public UnitParameterDTO(UnitParameter unitParameter) {
        this.setUnitId(unitParameter.getUnitId());
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
