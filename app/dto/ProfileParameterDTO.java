package dto;

import com.owera.xaps.dbi.ProfileParameter;

public class ProfileParameterDTO {
    private Integer id;

    private Integer profileId;

    private Integer unittypeParameterId;

    private String value;

    public ProfileParameterDTO(ProfileParameter param) {
        this.setId(param.getId());
        this.setProfileId(param.getProfile().getId());
        this.setUnittypeParameterId(param.getUnittypeParameter().getId());
        this.setValue(param.getValue());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
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
