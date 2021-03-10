package com.theword.wordcontent.config;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="Bible Passage Response")
public class PassageResponse implements Serializable{

	private static final long serialVersionUID = -247866175179656557L;
	
	@JsonProperty("id")
	@ApiModelProperty(notes = "Bible Language Code", example = "ENG")
    private String id;
	
	@JsonProperty("language")
	@ApiModelProperty(notes = "Bible Language Full Value", example = "English(KJV)")
    private String language;
	
	@JsonProperty("dir")
	@ApiModelProperty(notes = "Text Direction", example = "LTR")
    private String dir;
	
	@JsonProperty("passages")
	@ApiModelProperty(notes = "Passages", example = "[{\"book\":\"17\",\"name\":\"Esther\",\"english\":\"Esther\",\"content\":[{\"chapter\":1,\"verses\":[{\"verse\":\"1\",\"text\":\"Now it came to pass in the days of Ahasuerus, (this is Ahasuerus which reigned, from India even unto Ethiopia, over an hundred and seven and twenty provinces:) \"}]},{\"chapter\":8,\"verses\":[{\"verse\":\"9\",\"text\":\"Then were the king\\u2019s scribes called at that time in the third month, that is, the month Sivan, on the three and twentieth day thereof; and it was written according to all that Mordecai commanded unto the Jews, and to the lieutenants, and the deputies and rulers of the provinces which are from India unto Ethiopia, an hundred twenty and seven provinces, unto every province according to the writing thereof, and unto every people after their language, and to the Jews according to their writing, and according to their language. \"}]}]}]")
    private String passages;

}
