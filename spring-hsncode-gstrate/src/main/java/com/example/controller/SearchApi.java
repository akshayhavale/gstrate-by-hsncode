package com.example.controller;

import static java.util.Objects.nonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.configuration.FileLocationAppConfig;
import com.example.configuration.FileLocationConfig;
import com.example.model.GstModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

@RestController
public class SearchApi {
	
	@Autowired
	private FileLocationConfig config;
	
	@Value("${file_path}")
	private String file_path;
	
	@Autowired
	private FileLocationAppConfig configuration;

	@GetMapping("/gstrate")
	public List<GstModel> getGstModel() {

		JSONParser jsonParser = new JSONParser();
		List<GstModel> list = new ArrayList<>();

		try (FileReader reader = new FileReader("src/main/resources/files/gstrates.json")) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray gstModelList = (JSONArray) obj;
			System.out.println(gstModelList);

			gstModelList.forEach(gstModel -> {
				GstModel model = parseGstModelObject((JSONObject) gstModel);
				list.add(model);
			});
			return list;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return list;
	}

	private GstModel parseGstModelObject(JSONObject gstModel) {

		GstModel model = new GstModel();
		if (nonNull(gstModel.get("code"))) {
			model.setCode(gstModel.get("code").toString());
		}
		if (nonNull(gstModel.get("name"))) {
			model.setName(gstModel.get("name").toString());
		}
		if (nonNull(gstModel.get("SGST Rate (%)"))) {
			model.setSgst(gstModel.get("SGST Rate (%)").toString());
		}
		if (nonNull(gstModel.get("CGST Rate (%)"))) {
			model.setCgst(gstModel.get("CGST Rate (%)").toString());
		}
		if (nonNull(gstModel.get("IGST Rate (%)"))) {
			model.setIgst(gstModel.get("IGST Rate (%)").toString());
		}
		if (nonNull(gstModel.get("UTGST Rate (%)"))) {
			model.setUtgst(gstModel.get("UTGST Rate (%)").toString());
		}
		return model;
	}

	@GetMapping("/gstrate/{hsncode}")
	public String getGstRateForHsnCode(@PathVariable(value = "hsncode") String hsncode) throws IOException {

		Optional<String> igst = getGstModelList().stream()
				.filter(gstModel -> gstModel.getCode().equalsIgnoreCase(hsncode)).map(gstModel -> gstModel.getIgst())
				.findFirst();
		return igst.orElse("Not Availble");
	}

	private List<GstModel> getGstModelList() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, GstModel.class);
		return mapper.readValue(new File(this.getClass().getResource("/files/properjson/gstrates.json").getFile()), listType);
	}

}
