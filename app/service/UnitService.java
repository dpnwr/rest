package service;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.common.db.ConnectionProperties;
import com.owera.xaps.dbi.*;
import dto.UnitDTO;

import java.sql.SQLException;
import java.util.*;

@Singleton
public class UnitService {

    private XAPSUnit getXapsUnit(XAPS xaps) throws SQLException {
        Syslog syslog = xaps.getSyslog();
        ConnectionProperties properties = xaps.getConnectionProperties();
        return new XAPSUnit(properties, xaps, syslog);
    }

    public UnitDTO getUnit(String uuid, String unitId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit unitService = getXapsUnit(xaps);
        Unit unit = unitService.getUnitById(unitId);
        if (unit != null) {
            return new UnitDTO(unit);
        }
        return null;
    }

    public UnitDTO[] searchForUnits(String uuid, Integer unittypeId, Integer profileId, String searchTerms) throws SQLException {
        List<UnitDTO> units = new ArrayList<>();
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit unitService = getXapsUnit(xaps);
        Collection<Unit> results = unitService.getUnits(searchTerms, xaps.getUnittype(unittypeId), xaps.getProfile(profileId), null).values();
        for (Unit unit : results) {
            units.add(new UnitDTO(unit));
        }
        return units.toArray(new UnitDTO[units.size()]);
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

    public UnitDTO createUnit(String uuid, String unitId, Integer profileId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit xapsUnit = getXapsUnit(xaps);
        Profile xapsProfile = xaps.getProfile(profileId);
        xapsUnit.addUnits(Collections.singletonList(unitId), xapsProfile);
        return new UnitDTO(unitId, xapsProfile.getUnittype().getId(), xapsProfile.getId());
    }

    public void deleteUnit(String uuid, String id) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        XAPSUnit xapsUnit = getXapsUnit(xaps);
        xapsUnit.deleteUnit(xapsUnit.getUnitById(id));
    }
}
