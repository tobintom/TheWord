package com.theword.wordmeta.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Verses Response")
public class VersesResponse implements Serializable{

	private static final long serialVersionUID = -7023026307341536098L;
	
	@JsonProperty("status")
	@ApiModelProperty(notes = "status", example = "success")
    private String status;
	
	@JsonProperty("id")
	@ApiModelProperty(notes = "Bible Language Code", example = "SPN")
    private String id;
	
	@JsonProperty("number")
	@ApiModelProperty(notes = "Bible Book Code", example = "19")
    private String number;
	
	@JsonProperty("name")
	@ApiModelProperty(notes = "Name of book in selected language", example = "SALMOS")
    private String name;
	
	@JsonProperty("chapter")
	@ApiModelProperty(notes = "Chapter of the book", example = "117")
    private String chapter;
			
	@JsonProperty("verses")
	@ApiModelProperty(notes = "Verses Array", example = "[1,2]")
    private String verses;


}
