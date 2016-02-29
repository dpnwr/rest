package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import dto.UnittypeParameterDTO;
import com.owera.common.db.NoAvailableConnectionException;
import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.UnittypeParameter;
import com.owera.xaps.dbi.UnittypeParameterFlag;
import com.owera.xaps.dbi.UnittypeParameterValues;
import com.owera.xaps.dbi.UnittypeParameters;

public class UnittypeParameterService {
    private
    @Inject
    XAPSLoader xapsLoader;

    public UnittypeParameterDTO getUnittypeParameter(Integer unittypeId, Integer paramId) {
        return new UnittypeParameterDTO(xapsLoader.getXaps().getUnittypes().getById(unittypeId).getUnittypeParameters().getById(paramId));
    }

    public UnittypeParameterDTO[] getUnittypeParameters(Integer unittypeId) {
        List<UnittypeParameterDTO> dtoParams = new ArrayList<>();
        Unittype unittype = xapsLoader.getXaps().getUnittype(unittypeId);
        UnittypeParameters params = unittype.getUnittypeParameters();
        for (UnittypeParameter param : params.getUnittypeParameters()) {
            dtoParams.add(new UnittypeParameterDTO(param));
        }
        return dtoParams.toArray(new UnittypeParameterDTO[]{});
    }

    public UnittypeParameterDTO createeUnittypeParameter(Integer unittypeId, UnittypeParameterDTO unittypeParam) throws NoAvailableConnectionException, SQLException {
        Unittype unittype = xapsLoader.getXaps().getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = new UnittypeParameter(unittype, unittypeParam.getName(), new UnittypeParameterFlag(unittypeParam.getFlag()));
        unittype.getUnittypeParameters().addOrChangeUnittypeParameter(unittypeParameter, xapsLoader.getXaps());
        return new UnittypeParameterDTO(unittypeParameter);
    }

    public UnittypeParameterDTO updateUnittypeParameter(Integer unittypeId, UnittypeParameterDTO param) throws NoAvailableConnectionException, SQLException {
        Unittype unittype = xapsLoader.getXaps().getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = unittype.getUnittypeParameters().getById(param.getId());
        unittypeParameter.setName(param.getName());
        unittypeParameter.setFlag(new UnittypeParameterFlag(param.getFlag()));
        if (param.getValues() != null) {
            UnittypeParameterValues values = new UnittypeParameterValues();
            values.setPattern(param.getValues().getPattern());
            values.setValues(param.getValues().getValues());
            unittypeParameter.setValues(values);
        }
        unittype.getUnittypeParameters().addOrChangeUnittypeParameter(unittypeParameter, xapsLoader.getXaps());
        return new UnittypeParameterDTO(unittypeParameter);
    }

    public void deleteUnittypeParameter(Integer unittypeId, Integer paramId) throws NoAvailableConnectionException, SQLException {
        Unittype unittype = xapsLoader.getXaps().getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = unittype.getUnittypeParameters().getById(paramId);
        unittype.getUnittypeParameters().deleteUnittypeParameter(unittypeParameter, xapsLoader.getXaps());
    }
}
