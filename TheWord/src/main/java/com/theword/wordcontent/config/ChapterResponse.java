package com.theword.wordcontent.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Bible Text Response")
public class ChapterResponse implements Serializable{

	private static final long serialVersionUID = -5145886769606530735L;
	
	@JsonProperty("id")
	@ApiModelProperty(notes = "Bible Language Code", example = "ENG")
    private String id;
	
	@JsonProperty("language")
	@ApiModelProperty(notes = "Bible Language Full Value", example = "English(KJV)")
    private String language;
	
	@JsonProperty("dir")
	@ApiModelProperty(notes = "Text Direction", example = "LTR")
    private String dir;
	
	@JsonProperty("number")
	@ApiModelProperty(notes = "Bible Book Code", example = "01")
    private String number;
	
	@JsonProperty("name")
	@ApiModelProperty(notes = "Bible Book name in chosen Bible Language", example = "Genesis")
    private String name;
	
	@JsonProperty("english")
	@ApiModelProperty(notes = "Bible Book name equivalent in English", example = "Genesis")
    private String english;
	
	@JsonProperty("chapter")
	@ApiModelProperty(notes = "Chapter Number", example = "1")
    private String chapter;
	
	@JsonProperty("verses")
	@ApiModelProperty(notes = "Verses Array", example = "[{\"verse\":\"1\",\"text\":\" In the beginning God created the heaven and the earth. \"}]")
    private String verses;
	
	 
}
