package com.mnp.herontweets.tools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.codehaus.jackson.schema.JsonSchema;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataGeneratorGeo implements Serializable{
	public void dataGenerator(String jsonInput) {

		File jsonFile = new File("web/flaregeo.json");
		String dest;
		String jsonString, j1;
		try {
			jsonString = FileUtils.readFileToString(jsonFile);
			JsonElement jInput = new JsonParser().parse(jsonInput);
			JsonElement jelement = new JsonParser().parse(jsonString);
			JsonArray jobject = (JsonArray) jelement.getAsJsonArray();
			jobject.add(jInput);
			System.out.println(jobject);
			/*	Iterator<JsonElement> iterator1 = jobject.iterator();
				JsonElement removeIndex = null;
				while (iterator1.hasNext()) {
					JsonObject tmp1 = iterator1.next().getAsJsonObject();
					String removeDest = tmp1.get("name").getAsString();
					if (removeDest.equals(dest))
						removeIndex = tmp1;
				}
				jobject.remove(removeIndex);
				jobject.add(src); */
				Gson gson = new Gson();
				String resultingJson = gson.toJson(jelement);
			//	System.out.println(resultingJson);
				FileUtils.writeStringToFile(jsonFile, resultingJson);
			}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		DataGeneratorGeo g = new DataGeneratorGeo();
		g.dataGenerator("{lat: 24.6408, lng:46.7728}");
	}

}
