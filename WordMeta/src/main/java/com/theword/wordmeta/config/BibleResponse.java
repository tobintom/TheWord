package com.theword.wordmeta.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Bibles List Response")
public class BibleResponse implements Serializable{

	private static final long serialVersionUID = -7023026307341536098L;
	
	@JsonProperty("status")
	@ApiModelProperty(notes = "status", example = "success")
    private String status;
	
		
	@JsonProperty("bibles")
	@ApiModelProperty(notes = "Bibles Array", example = "[ { \"id\": \"AFK\", \"language\": \"Afrikaans\", \"direction\": \"LTR\" }, { \"id\": \"ALB\", \"language\": \"Albanian\", \"direction\": \"LTR\" }, { \"id\": \"AMH\", \"language\": \"Amharic\", \"direction\": \"LTR\" }]")
    private String bibles;


}
