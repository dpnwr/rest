package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.XAPS;
import dto.UnittypeDTO;
import com.owera.xaps.dbi.Unittype;

@Singleton
public class UnittypeService {

    public UnittypeDTO getUnittype(String uuid, Integer id) {
        XAPS xaps = SessionCache.getXAPS(uuid);
        return new UnittypeDTO(xaps.getUnittype(id));
    }

    public UnittypeDTO[] getUnittypes(String uuid) {
        List<UnittypeDTO> params = new ArrayList<>();
        XAPS xaps = SessionCache.getXAPS(uuid);
        for (Unittype ut : xaps.getUnittypes().getUnittypes()) {
            params.add(new UnittypeDTO(ut));
        }
        return params.toArray(new UnittypeDTO[params.size()]);
    }

    public UnittypeDTO updateUnittype(String uuid, UnittypeDTO unittype) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype ut = xaps.getUnittype(unittype.id);
        ut.setDescription(unittype.description);
        ut.setName(unittype.name);
        ut.setVendor(unittype.vendor);
        ut.setProtocol(unittype.protocol);
        xaps.getUnittypes().addOrChangeUnittype(ut, xaps);
        return new UnittypeDTO(ut);
    }

    public UnittypeDTO createeUnittype(String uuid, UnittypeDTO unittype) throws SQLException {
        Unittype ut = new Unittype(unittype.name, unittype.vendor, unittype.description, unittype.protocol);
        XAPS xaps = SessionCache.getXAPS(uuid);
        xaps.getUnittypes().addOrChangeUnittype(ut, xaps);
        return new UnittypeDTO(ut);
    }

    public void deleteUnittype(String uuid, Integer id) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype ut = xaps.getUnittype(id);
        xaps.getUnittypes().deleteUnittype(ut, xaps, true);
    }
}
