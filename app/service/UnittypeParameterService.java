package service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cache.SessionCache;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.*;
import dto.UnittypeParameterDTO;

@Singleton
public class UnittypeParameterService {

    public UnittypeParameterDTO getUnittypeParameter(String uuid, Integer unittypeId, Integer paramId) {
        XAPS xaps = SessionCache.getXAPS(uuid);
        UnittypeParameter param = xaps.getUnittypes().getById(unittypeId).getUnittypeParameters().getById(paramId);
        if (param != null) {
            return new UnittypeParameterDTO(param);
        }
        return null;
    }

    public UnittypeParameterDTO[] getUnittypeParameters(String uuid, Integer unittypeId) {
        List<UnittypeParameterDTO> dtoParams = new ArrayList<>();
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        UnittypeParameters params = unittype.getUnittypeParameters();
        for (UnittypeParameter param : params.getUnittypeParameters()) {
            dtoParams.add(new UnittypeParameterDTO(param));
        }
        return dtoParams.toArray(new UnittypeParameterDTO[dtoParams.size()]);
    }

    public UnittypeParameterDTO createeUnittypeParameter(String uuid, Integer unittypeId, UnittypeParameterDTO unittypeParam) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = new UnittypeParameter(unittype, unittypeParam.getName(), new UnittypeParameterFlag(unittypeParam.getFlag()));
        unittype.getUnittypeParameters().addOrChangeUnittypeParameter(unittypeParameter, xaps);
        return new UnittypeParameterDTO(unittypeParameter);
    }

    public UnittypeParameterDTO updateUnittypeParameter(String uuid, Integer unittypeId, UnittypeParameterDTO param) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = unittype.getUnittypeParameters().getById(param.getId());
        if (unittypeParameter != null) {
            unittypeParameter.setName(param.getName());
            unittypeParameter.setFlag(new UnittypeParameterFlag(param.getFlag()));
            if (param.getValues() != null) {
                UnittypeParameterValues values = new UnittypeParameterValues();
                values.setPattern(param.getValues().getPattern());
                values.setValues(param.getValues().getValues());
                unittypeParameter.setValues(values);
            }
            unittype.getUnittypeParameters().addOrChangeUnittypeParameter(unittypeParameter, xaps);
            return new UnittypeParameterDTO(unittypeParameter);
        }
        return null;
    }

    public void deleteUnittypeParameter(String uuid, Integer unittypeId, Integer paramId) throws SQLException {
        XAPS xaps = SessionCache.getXAPS(uuid);
        Unittype unittype = xaps.getUnittype(unittypeId);
        UnittypeParameter unittypeParameter = unittype.getUnittypeParameters().getById(paramId);
        unittype.getUnittypeParameters().deleteUnittypeParameter(unittypeParameter, xaps);
    }
}
