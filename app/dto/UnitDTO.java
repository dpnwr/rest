package dto;

import com.owera.xaps.dbi.Unit;
import play.data.validation.Constraints;

public class UnitDTO {
    @Constraints.Required
    public String id;
    @Constraints.Required
    public Integer unittypeId;
    @Constraints.Required
    public Integer profileId;

    public UnitDTO() {
    }

    public UnitDTO(String id, Integer unittypeId, Integer profileId) {
        this.id = id;
        this.profileId = profileId;
        this.unittypeId = unittypeId;
    }

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.unittypeId = unit.getUnittype().getId();
        this.profileId = unit.getProfile().getId();
    }
}
