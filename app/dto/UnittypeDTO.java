package dto;

import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.Unittype.ProvisioningProtocol;
import play.data.validation.Constraints;

public class UnittypeDTO {
    private Integer id;
    @Constraints.Required
    private String name;
    @Constraints.Required
    private String vendor;
    @Constraints.Required
    private String description;
    @Constraints.Required
    private ProvisioningProtocol protocol;

    public UnittypeDTO() {
    }

    public UnittypeDTO(Unittype unittype) {
        this.id = unittype.getId();
        this.name = unittype.getName();
        this.vendor = unittype.getVendor();
        this.description = unittype.getDescription();
        this.protocol = unittype.getProtocol();
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

    public ProvisioningProtocol getProtocol() {
        return protocol;
    }
}
