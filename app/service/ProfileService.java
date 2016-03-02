package service;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.Profile;
import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.XAPS;
import dto.ProfileDTO;
import dto.UnittypeDTO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ProfileService {

    public ProfileDTO getProfile(String uuid, Integer id, Integer unittypeId) {
        XAPS xaps = SessionCache.getXAPS(uuid);
        return new ProfileDTO(xaps.getUnittype(unittypeId).getProfiles().getById(id));
    }

    public ProfileDTO[] getProfiles(String uuid, Integer unittypeId) {
        List<ProfileDTO> profiles = new ArrayList<>();
        XAPS xaps = SessionCache.getXAPS(uuid);
        for (Profile profile : xaps.getUnittype(unittypeId).getProfiles().getProfiles()) {
            profiles.add(new ProfileDTO(profile));
        }
        return profiles.toArray(new ProfileDTO[profiles.size()]);
    }

    public ProfileDTO updateProfile(String uuid, ProfileDTO profile, Integer unittypeId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        Profile toUpdate = xaps.getProfile(profile.id);
        toUpdate.setName(profile.name);
        unittype.getProfiles().addOrChangeProfile(toUpdate, xaps);
        return new ProfileDTO(toUpdate);
    }

    public ProfileDTO createeProfile(String uuid, ProfileDTO profile, Integer unittypeId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        Profile toCreate = new Profile(profile.name, unittype);
        unittype.getProfiles().addOrChangeProfile(toCreate, xaps);
        return new ProfileDTO(toCreate);
    }

    public void deleteProfile(String uuid, Integer id, Integer unittypeId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        Profile toDelete = xaps.getProfile(id);
        unittype.getProfiles().deleteProfile(toDelete, xaps, true);
    }
}
