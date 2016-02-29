package dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.JsonNode;
import com.owera.xaps.dbi.Unittype;
import com.owera.xaps.dbi.Unittype.ProvisioningProtocol;
import play.libs.F;

public class UnittypeDTO {
	private Integer id;
	@NotNull
	private String name;
	@NotNull
	private String vendor;
	@NotNull
	private String description;
	@NotNull
	private ProvisioningProtocol protocol;
	
	public UnittypeDTO() {}
	
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
