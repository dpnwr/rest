package dto;

import com.owera.xaps.dbi.ProfileParameter;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
public class ProfileParameterDTO {
    private Integer id;
    @Constraints.Required
    private Integer profileId;
    @Constraints.Required
    private Integer unittypeParameterId;
    @Constraints.Required
    private String value;

    public ProfileParameterDTO(ProfileParameter param) {
        this.setId(param.getId());
        this.setProfileId(param.getProfile().getId());
        this.setUnittypeParameterId(param.getUnittypeParameter().getId());
        this.setValue(param.getValue());
    }
}
