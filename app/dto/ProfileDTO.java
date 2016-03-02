package dto;

import com.owera.xaps.dbi.Profile;
import play.data.validation.Constraints;

public class ProfileDTO {
    public Integer id;
    @Constraints.Required
    public String name;
    @Constraints.Required
    public Integer unittypeId;

    public ProfileDTO() {
    }

    public ProfileDTO(Profile profile) {
        this.id = profile.getId();
        this.name = profile.getName();
        this.unittypeId = profile.getUnittype().getId();
    }
}