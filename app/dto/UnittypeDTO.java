package dto;

import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.Unittype.ProvisioningProtocol;
import play.data.validation.Constraints;

public class UnittypeDTO {
    public Integer id;
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String vendor;
    @Constraints.Required
    public String description;
    @Constraints.Required
    public ProvisioningProtocol protocol;

    public UnittypeDTO() {
    }

    public UnittypeDTO(Unittype unittype) {
        this.id = unittype.getId();
        this.name = unittype.getName();
        this.vendor = unittype.getVendor();
        this.description = unittype.getDescription();
        this.protocol = unittype.getProtocol();
    }
}
