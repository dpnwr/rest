package dto;

import com.owera.xaps.dbi.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class UnitDTO {
    @Constraints.Required
    private String id;
    private Integer unittypeId;
    @Constraints.Required
    private Integer profileId;

    public UnitDTO(String id, Integer unittypeId, Integer profileId) {
        this.setId(id);
        this.setProfileId(profileId);
        this.setUnittypeId(unittypeId);
    }

    public UnitDTO(Unit unit) {
        this.setId(unit.getId());
        this.setUnittypeId(unit.getUnittype().getId());
        this.setProfileId(unit.getProfile().getId());
    }
}
