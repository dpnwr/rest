package dto;

import com.owera.xaps.dbi.Profile;
import play.data.validation.Constraints;

public class ProfileDTO {
    private Integer id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private Integer unittypeId;

    public ProfileDTO() {
    }

    public ProfileDTO(Profile profile) {
        this.setId(profile.getId());
        this.setName(profile.getName());
        this.setUnittypeId(profile.getUnittype().getId());
    }

    public String getName() {
        return name;
    }

    public Integer getUnittypeId() {
        return unittypeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnittypeId(Integer unittypeId) {
        this.unittypeId = unittypeId;
    }
}