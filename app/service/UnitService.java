package service;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.*;
import dto.UnitDTO;

import java.sql.SQLException;
import java.util.*;

import static util.LambdaExceptionUtil.*;

@Singleton
public class UnitService {

    private XAPSUnit getXapsUnit(XAPS xaps) throws SQLException {
        return new XAPSUnit(xaps.getConnectionProperties(), xaps, xaps.getSyslog());
    }

    public UnitDTO getUnit(String uuid, String unitId) throws SQLException {
        return Optional.ofNullable(getXapsUnit(SessionCache.getXAPS(uuid))
                .getUnitById(unitId))
                .map(UnitDTO::new)
                .orElse(null);
    }

    public UnitDTO[] searchForUnits(String uuid, Integer unittypeId, Integer profileId, String searchTerms) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        return getXapsUnit(xaps).getUnits(searchTerms, xaps.getUnittype(unittypeId), xaps.getProfile(profileId), null)
                .values()
                .stream()
                .map(UnitDTO::new)
                .toArray(UnitDTO[]::new);
    }

    public UnitDTO updateUnit(String uuid, UnitDTO unit) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit xapsUnit = getXapsUnit(xaps);
        Profile oldProfile = xapsUnit.getUnitById(unit.getId()).getProfile();
        if (oldProfile.getId().intValue() != unit.getProfileId().intValue()) {
            xapsUnit.moveUnits(Collections.singletonList(unit.getId()), xaps.getProfile(unit.getProfileId()));
        }
        return unit;
    }

    public UnitDTO createUnit(String uuid, UnitDTO unit) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit xapsUnit = getXapsUnit(xaps);
        Profile xapsProfile = xaps.getProfile(unit.getProfileId());
        xapsUnit.addUnits(Collections.singletonList(unit.getId()), xapsProfile);
        return new UnitDTO(unit.getId(), xapsProfile.getUnittype().getId(), xapsProfile.getId());
    }

    public int deleteUnit(String uuid, String id) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit xapsUnit = getXapsUnit(xaps);
        return Optional.ofNullable(xapsUnit.getUnitById(id))
                .map(rethrowFunction(xapsUnit::deleteUnit))
                .orElseThrow(() -> new IllegalArgumentException("Unit " + id + " does not exist"));
    }
}