package com.theword.wordmeta.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Chapters Response")
public class ChaptersResponse implements Serializable{

	private static final long serialVersionUID = -7023026307341536098L;
	
	@JsonProperty("status")
	@ApiModelProperty(notes = "status", example = "success")
    private String status;
	
	@JsonProperty("id")
	@ApiModelProperty(notes = "Bible Language Code", example = "SPN")
    private String id;
	
	@JsonProperty("number")
	@ApiModelProperty(notes = "Bible Book Code", example = "61")
    private String number;
	
	@JsonProperty("name")
	@ApiModelProperty(notes = "Name of book in selected language", example = "2 SAN PEDRO")
    private String name;
	
	@JsonProperty("english")
	@ApiModelProperty(notes = "Name of book in English", example = "2 Peter")
    private String english;
			
	@JsonProperty("chapters")
	@ApiModelProperty(notes = "Chapters Array", example = "[1,2,3]")
    private String chapters;


}
