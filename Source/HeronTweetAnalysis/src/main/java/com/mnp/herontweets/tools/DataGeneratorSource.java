package com.mnp.herontweets.tools;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataGeneratorSource implements Serializable {

	public void dataGenerator(String jsonInput) {

		File jsonFile = new File("web/flaresource.json");
		String dest;
		// Commons-IO
		String jsonString, j1;
		try {
			jsonString = FileUtils.readFileToString(jsonFile);
			JsonElement jInput = new JsonParser().parse(jsonInput);
			JsonObject jObj = jInput.getAsJsonObject();
			// jobject.addProperty("isThisCodeAmazing", Boolean.TRUE);
			JsonArray jArray = jObj.getAsJsonArray("rankings");
			if (jArray.size() != 0) {
				JsonArray tmp = (JsonArray) jArray.get(0).getAsJsonObject().get("fields");
				String str = tmp.get(1).toString();

				if (str.contains("Twitter for iPhone"))
					dest = "ios";
				else if (str.contains("Facebook"))
					dest = "facebook";
				else if (str.contains("Twitter for Android"))
					dest = "Android";
				else
					dest = "web";
				JsonArray trends = new JsonArray();
				Iterator<JsonElement> iterator = jArray.iterator();
				while (iterator.hasNext()) {
					JsonObject tmp1 = iterator.next().getAsJsonObject();
					String t = tmp1.get("object").toString();
					JsonObject tre = new JsonObject();
					tre.addProperty("name", t);
					trends.add(tre);
				}
				JsonObject src = new JsonObject();
				src.addProperty("name", dest);
				src.add("children", trends);
				System.out.println(src);
				JsonElement jelement = new JsonParser().parse(jsonString);
				JsonArray jobject = (JsonArray) jelement.getAsJsonObject().get("children");
				Iterator<JsonElement> iterator1 = jobject.iterator();
				JsonElement removeIndex = null;
				while (iterator1.hasNext()) {
					JsonObject tmp1 = iterator1.next().getAsJsonObject();
					String removeDest = tmp1.get("name").getAsString();
					if (removeDest.equals(dest))
						removeIndex = tmp1;
				}
				jobject.remove(removeIndex);
				jobject.add(src);
				Gson gson = new Gson();
				String resultingJson = gson.toJson(jelement);
				System.out.println(resultingJson);
				FileUtils.writeStringToFile(jsonFile, resultingJson);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
