package dto;

import com.owera.xaps.dbi.UnitParameter;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

import java.util.Optional;

@Data
@NoArgsConstructor
public class UnitParameterDTO {
    private String unitId;
    @Constraints.Required
    private Integer unittypeParameterId;
    @Constraints.Required
    private String value;

    public UnitParameterDTO(UnitParameter unitParameter) {
        this.setUnitId(Optional.of(unitParameter.getUnitId()).orElse(null));
        this.setUnittypeParameterId(unitParameter.getParameter().getUnittypeParameter().getId());
        this.setValue(unitParameter.getParameter().getValue());
    }
}
