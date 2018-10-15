package com.incture.metrodata.dto.opti;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Distance {

@SerializedName("text")
@Expose
private String text;
@SerializedName("value")
@Expose
private Long value;

public String getText() {
return text;
}

public void setText(String text) {
this.text = text;
}

public Long getValue() {
return value;
}

public void setValue(Long value) {
this.value = value;
}

@Override
public String toString() {
	return "Distance [text=" + text + ", value=" + value + "]";
}
}
