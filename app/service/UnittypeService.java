package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.XAPS;
import dto.UnittypeDTO;
import com.owera.common.db.NoAvailableConnectionException;
import com.owera.xaps.dbi.Unittype;

@Singleton
public class UnittypeService {

    public UnittypeDTO getUnittype(String uuid, Integer id) {
        return new UnittypeDTO(SessionCache.getXAPS(uuid).getUnittype(id));
    }

    public UnittypeDTO[] getUnittypes(String uuid) {
        List<UnittypeDTO> params = new ArrayList<>();
        for (Unittype ut : SessionCache.getXAPS(uuid).getUnittypes().getUnittypes()) {
            params.add(new UnittypeDTO(ut));
        }
        return params.toArray(new UnittypeDTO[]{});
    }

    public UnittypeDTO updateUnittype(String uuid, UnittypeDTO unittype) throws NoAvailableConnectionException, SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype ut = xaps.getUnittype(unittype.getId());
        ut.setDescription(unittype.getDescription());
        ut.setName(unittype.getName());
        ut.setVendor(unittype.getVendor());
        ut.setProtocol(unittype.getProtocol());
        xaps.getUnittypes().addOrChangeUnittype(ut, xaps);
        return new UnittypeDTO(ut);
    }

    public UnittypeDTO createeUnittype(String uuid, UnittypeDTO unittype) throws NoAvailableConnectionException, SQLException {
        Unittype ut = new Unittype(unittype.getName(), unittype.getVendor(), unittype.getDescription(), unittype.getProtocol());
        SessionCache.getXAPS(uuid).getUnittypes().addOrChangeUnittype(ut, SessionCache.getXAPS(uuid));
        return new UnittypeDTO(ut);
    }

    public void deleteUnittype(String uuid, Integer id) throws NoAvailableConnectionException, SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype ut = xaps.getUnittype(id);
        xaps.getUnittypes().deleteUnittype(ut, xaps, true);
    }
}
