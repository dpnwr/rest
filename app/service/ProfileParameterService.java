package service;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.*;
import dto.ProfileParameterDTO;
import util.NotFoundException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static util.LambdaExceptionUtil.*;

@Singleton
public class ProfileParameterService {

    public ProfileParameterDTO getProfileParameter(String uuid, Integer profileId, Integer paramId) {
        return Optional.ofNullable(SessionCache.getXAPS(uuid).getProfile(profileId).getProfileParameters().getById(paramId))
                .map(ProfileParameterDTO::new)
                .orElse(null);
    }

    public ProfileParameterDTO[] getProfileParameters(String uuid, Integer profileId) {
        return Arrays.asList(SessionCache.getXAPS(uuid)
                .getProfile(profileId)
                .getProfileParameters().getProfileParameters())
                .stream()
                .map(ProfileParameterDTO::new)
                .toArray(ProfileParameterDTO[]::new);
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
        return Optional.ofNullable(profile.getProfileParameters().getById(param.getId()))
                .map(rethrowFunction(profileParameter -> {
                    profileParameter.setValue(param.getValue());
                    profile.getProfileParameters().addOrChangeProfileParameter(profileParameter, xaps);
                    return new ProfileParameterDTO(profileParameter);
                }))
                .orElseThrow(() -> new NotFoundException("Profile parameter " + param.getId() + " does not exist"));
    }

    public void deleteProfileParameter(String uuid, Integer profileId, Integer paramId) {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Profile profile = xaps.getProfile(profileId);
        Optional.ofNullable(profile.getProfileParameters().getById(paramId))
                .ifPresent(rethrowConsumer(profileParameter ->
                        profile.getProfileParameters().deleteProfileParameter(profileParameter, xaps)));
    }
}
