package dto;

import com.owera.xaps.dbi.Unit;
import play.data.validation.Constraints;

public class UnitDTO {
    @Constraints.Required
    private String id;
    @Constraints.Required
    private Integer unittypeId;
    @Constraints.Required
    private Integer profileId;

    public UnitDTO() {
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUnittypeId() {
        return unittypeId;
    }

    public void setUnittypeId(Integer unittypeId) {
        this.unittypeId = unittypeId;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }
}
