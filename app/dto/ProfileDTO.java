package dto;

import com.owera.xaps.dbi.Profile;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class ProfileDTO {
    private Integer id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private Integer unittypeId;

    public ProfileDTO(Profile profile) {
        this.setId(profile.getId());
        this.setName(profile.getName());
        this.setUnittypeId(profile.getUnittype().getId());
    }
}