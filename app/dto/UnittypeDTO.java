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
        this.setId(unittype.getId());
        this.setName(unittype.getName());
        this.setVendor(unittype.getVendor());
        this.setDescription(unittype.getDescription());
        this.setProtocol(unittype.getProtocol());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProvisioningProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(ProvisioningProtocol protocol) {
        this.protocol = protocol;
    }
}
