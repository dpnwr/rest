package service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.owera.xaps.dbi.*;
import dto.UnitParameterDTO;
import util.NotFoundException;
import javax.validation.constraints.NotNull;
import java.sql.SQLException;
import static java.util.Collections.singletonList;
import java.util.Optional;
import java.util.function.Function;

import static util.LambdaExceptionUtil.*;

@Singleton
public class UnitParameterService {
    @Inject
    private XAPSLoader xapsLoader;

    public UnitParameterDTO getUnitParameter(String uuid, String unitId, Integer paramId) throws SQLException {
        Unit unit = Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"))
                .getUnitById(unitId);
        return getUnitParameter(unit,
                getUnittypeParameter(paramId, unit.getUnittype(), UnittypeParameter::getName),
                UnitParameterDTO::new);
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
        Unit unit = Optional.ofNullable(xapsUnit.getUnitById(unitId))
                .map(rethrowFunction(u -> u))
                .orElseThrow(() -> new NotFoundException("Unit " + unitId + " was not found"));
        return Optional.ofNullable(unit.getUnittype().getUnittypeParameters().getById(unitParameterDTO.getUnittypeParameterId()))
                .map(rethrowFunction(unittypeParameter -> {
                    xapsUnit.addOrChangeUnitParameter(unit, unittypeParameter.getName(), unitParameterDTO.getValue());
                    return new UnitParameterDTO(unit.getUnitParameters().get(unittypeParameter.getName()));
                }))
                .orElseThrow(() -> new NotFoundException("UnittypeParameter " + unitParameterDTO.getUnittypeParameterId() + " does not exist"));
    }

    public Integer deleteUnitParameter(String uuid, String unitId, Integer paramId) throws SQLException {
        XAPSUnit xapsUnit = Optional.of(xapsLoader.getXAPSUnit(uuid))
                .orElseThrow(() -> new IllegalStateException("XAPSUnit could not be created"));
        return Optional.ofNullable(xapsUnit.getUnitById(unitId))
                .map(rethrowFunction(u -> xapsUnit.deleteUnitParameters(
                        singletonList(getUnitParameter(u, getUnittypeParameter(paramId, u.getUnittype()))))))
                .orElseThrow(() -> new NotFoundException("Unit " + unitId + " was not found"));
    }

    private @NotNull UnittypeParameter getUnittypeParameter(Integer paramId, Unittype ut) {
        return getUnittypeParameter(paramId, ut, utp -> utp);
    }


    private <R> R getUnittypeParameter(Integer paramId, Unittype ut, Function<UnittypeParameter, R> unittypeParameterConverter) {
        return Optional.ofNullable(ut.getUnittypeParameters().getById(paramId))
                .map(unittypeParameterConverter)
                .orElseThrow(() -> new NotFoundException("UnittypeParameter " + paramId + " does not exist"));
    }

    private @NotNull UnitParameter getUnitParameter(Unit u, UnittypeParameter utp) {
        return getUnitParameter(u, utp.getName(), up -> up);
    }


    private <R> R getUnitParameter(Unit u, String utp, Function<UnitParameter, R> unitParameterConverter) {
        return Optional.ofNullable(u.getUnitParameters().get(utp))
                .map(unitParameterConverter)
                .orElseThrow(() -> new NotFoundException("Unit parameter " + utp + " was not found"));
    }
}
