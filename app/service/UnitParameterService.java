package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.Unit;
import com.owera.xaps.dbi.UnitParameter;
import com.owera.xaps.dbi.UnittypeParameter;
import com.owera.xaps.dbi.XAPSUnit;
import dto.UnitParameterDTO;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

@Singleton
public class UnitParameterService {
    @Inject
    private XAPSLoader xapsLoader;

    public UnitParameterDTO getUnitParameter(String uuid, String unitId, Integer paramId) throws SQLException {
        Unit unit = Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"))
                .getUnitById(unitId);
        UnittypeParameter unittypeParameter = unit.getUnittype().getUnittypeParameters().getById(paramId);
        String unittypeParameterName = unittypeParameter.getName();
        String parameterValue = unit.getParameterValue(unittypeParameterName);
        return new UnitParameterDTO(new UnitParameter(unittypeParameter, unit.getId(), parameterValue, unit.getProfile()));
    }

    public UnitParameterDTO[] getUnitParameters(String uuid, String unitId) throws SQLException {
        return Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"))
                .getUnitById(unitId)
                .getUnitParameters()
                .values()
                .stream()
                .map(UnitParameterDTO::new)
                .toArray(UnitParameterDTO[]::new);
    }

    public UnitParameterDTO createOrUpdateUnitParameter(String uuid, String unitId, UnitParameterDTO unitParameterDTO) throws SQLException {
        XAPSUnit xapsUnit = Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"));
        Unit unit = xapsUnit.getUnitById(unitId);
        UnittypeParameter unittypeParameter = unit.getUnittype().getUnittypeParameters().getById(unitParameterDTO.getUnittypeParameterId());
        xapsUnit.addOrChangeUnitParameter(unit, unittypeParameter.getName(), unitParameterDTO.getValue());
        return new UnitParameterDTO(unit.getUnitParameters().get(unittypeParameter.getName()));
    }

    public void deleteUnitParameter(String uuid, String unitId, Integer paramId) throws SQLException {
        XAPSUnit xapsUnit = Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"));
        Unit unit = xapsUnit.getUnitById(unitId);
        UnittypeParameter unittypeParameter = unit.getUnittype().getUnittypeParameters().getById(paramId);
        xapsUnit.deleteUnitParameters(Collections.singletonList(unit.getUnitParameters().get(unittypeParameter.getName())));
    }
}
