package org.customer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModelProperty;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonPropertyOrder({"id", "name"})
@SequenceGenerator(name = "custSeqGen", sequenceName = "custSeq", initialValue = 1, allocationSize = 5)
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="custSeqGen")
    @ApiModelProperty(name = "id", value ="Customer id")
	private Long id;

    @NotBlank(message = "Name is mandatory")
    @ApiModelProperty(name = "name", value = "Customer name")
	private String name;


	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

    public String toString() {
        return new StringBuilder().append("Customer[id=").append(id).append(", name=").append(name).append("]").toString();
    }
}
