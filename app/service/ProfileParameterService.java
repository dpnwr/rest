package service;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.*;
import dto.ProfileParameterDTO;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProfileParameterService {

    public ProfileParameterDTO getProfileParameter(String uuid, Integer profileId, Integer paramId) {
        XAPS xaps = SessionCache.getXAPS(uuid);
        ProfileParameter param = xaps.getProfile(profileId).getProfileParameters().getById(paramId);
        if (param != null) {
            return new ProfileParameterDTO(param);
        }
        return null;
    }

    public ProfileParameterDTO[] getProfileParameters(String uuid, Integer profileId) {
        List<ProfileParameterDTO> dtoParams = new ArrayList<>();
        XAPS xaps = SessionCache.getXAPS(uuid);
        Profile profile = xaps.getProfile(profileId);
        ProfileParameters params = profile.getProfileParameters();
        for (ProfileParameter param : params.getProfileParameters()) {
            dtoParams.add(new ProfileParameterDTO(param));
        }
        return dtoParams.toArray(new ProfileParameterDTO[dtoParams.size()]);
    }

    public ProfileParameterDTO createProfileParameter(String uuid, Integer profileId, ProfileParameterDTO profileParam) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Profile profile = xaps.getProfile(profileId);
        Unittype unittype = profile.getUnittype();
        UnittypeParameter utp = unittype.getUnittypeParameters().getById(profileParam.getUnittypeParameterId());
        ProfileParameter profileParameter = new ProfileParameter(profile, utp, profileParam.getValue());
        profile.getProfileParameters().addOrChangeProfileParameter(profileParameter, xaps);
        return new ProfileParameterDTO(profileParameter);
    }

    public ProfileParameterDTO updateProfileParameter(String uuid, Integer profileId, ProfileParameterDTO param) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Profile profile = xaps.getProfile(profileId);
        ProfileParameter profileParameter = profile.getProfileParameters().getById(param.getId());
        if (profileParameter != null) {
            profileParameter.setValue(param.getValue());
            profile.getProfileParameters().addOrChangeProfileParameter(profileParameter, xaps);
            return new ProfileParameterDTO(profileParameter);
        }
        return null;
    }

    public void deleteProfileParameter(String uuid, Integer profileId, Integer paramId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Profile profile = xaps.getProfile(profileId);
        ProfileParameter profileParameter = profile.getProfileParameters().getById(paramId);
        profile.getProfileParameters().deleteProfileParameter(profileParameter, xaps);
    }
}
