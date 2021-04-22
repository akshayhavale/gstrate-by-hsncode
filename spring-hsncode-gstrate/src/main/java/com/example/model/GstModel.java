package com.example.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class GstModel {
	
	private String code;
	private String name;
	private String sgst;
	private String cgst;
	private String igst;
	private String utgst;

}
