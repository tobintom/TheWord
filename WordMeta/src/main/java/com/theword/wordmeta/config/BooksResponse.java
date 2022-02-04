package com.theword.wordmeta.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Bible Books Response")
public class BooksResponse implements Serializable{

	private static final long serialVersionUID = -7023026307341536098L;
	
	@JsonProperty("status")
	@ApiModelProperty(notes = "status", example = "success")
    private String status;
	
	@JsonProperty("id")
	@ApiModelProperty(notes = "Bible Language Code", example = "SPN")
    private String id;
	
	@JsonProperty("language")
	@ApiModelProperty(notes = "Bible Language Full Value", example = "Spanish")
    private String language;
	
	@JsonProperty("dir")
	@ApiModelProperty(notes = "Text Direction", example = "LTR")
    private String dir;
	
			
	@JsonProperty("books")
	@ApiModelProperty(notes = "Books Array", example = "[{\"number\":\"01\",\"name\":\"G\\u00c9NESIS\",\"english\":\"Genesis\"},{\"number\":\"02\",\"name\":\"\\u00c9XODO\",\"english\":\"Exodus\"},{\"number\":\"03\",\"name\":\"LEV\\u00cdTICO\",\"english\":\"Leviticus\"}]")
    private String books;


}
