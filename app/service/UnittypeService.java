package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import dto.UnittypeDTO;
import com.owera.common.db.NoAvailableConnectionException;
import com.owera.xaps.dbi.Unittype;

@Singleton
public class UnittypeService {

    private
    @Inject
    XAPSLoader xapsLoader;

    public UnittypeDTO getUnittype(Integer id) {
        return new UnittypeDTO(xapsLoader.getXaps().getUnittype(id));
    }

    public UnittypeDTO[] getUnittypes() {
        List<UnittypeDTO> params = new ArrayList<>();
        for (Unittype ut : xapsLoader.getXaps().getUnittypes().getUnittypes()) {
            params.add(new UnittypeDTO(ut));
        }
        return params.toArray(new UnittypeDTO[]{});
    }

    public UnittypeDTO updateUnittype(UnittypeDTO unittype) throws NoAvailableConnectionException, SQLException {
        Unittype ut = xapsLoader.getXaps().getUnittype(unittype.getId());
        ut.setDescription(unittype.getDescription());
        ut.setName(unittype.getName());
        ut.setVendor(unittype.getVendor());
        ut.setProtocol(unittype.getProtocol());
        xapsLoader.getXaps().getUnittypes().addOrChangeUnittype(ut, xapsLoader.getXaps());
        return new UnittypeDTO(ut);
    }

    public UnittypeDTO createeUnittype(UnittypeDTO unittype) throws NoAvailableConnectionException, SQLException {
        Unittype ut = new Unittype(unittype.getName(), unittype.getVendor(), unittype.getDescription(), unittype.getProtocol());
        xapsLoader.getXaps().getUnittypes().addOrChangeUnittype(ut, xapsLoader.getXaps());
        return new UnittypeDTO(ut);
    }

    public void deleteUnittype(Integer id) throws NoAvailableConnectionException, SQLException {
        Unittype ut = xapsLoader.getXaps().getUnittype(id);
        xapsLoader.getXaps().getUnittypes().deleteUnittype(ut, xapsLoader.getXaps(), true);
    }
}
