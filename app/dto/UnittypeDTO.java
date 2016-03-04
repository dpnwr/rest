package dto;

import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.Unittype.ProvisioningProtocol;
import lombok.Data;
import lombok.NoArgsConstructor;
import play.data.validation.Constraints;

@Data
@NoArgsConstructor
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

    public UnittypeDTO(Unittype unittype) {
        this.setId(unittype.getId());
        this.setName(unittype.getName());
        this.setVendor(unittype.getVendor());
        this.setDescription(unittype.getDescription());
        this.setProtocol(unittype.getProtocol());
    }
}
