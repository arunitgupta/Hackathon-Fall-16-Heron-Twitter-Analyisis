package com.mnp.herontweets.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DataGeneratorTrends implements Serializable{
	public void dataGenerator(String s)
	{
		String file = "web/flaretrends.csv";
		File f = new File(file);
		BufferedWriter bw = null;
		FileWriter fw = null;
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			fw = new FileWriter(f);
		bw = new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			bw.append("word,count");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonElement jInput = new JsonParser().parse(s);
		JsonObject jObj = jInput.getAsJsonObject();
		JsonArray jArray = jObj.getAsJsonArray("rankings");
		if (jArray.size() != 0)
		{
			Iterator<JsonElement> iterator = jArray.iterator();
			while (iterator.hasNext()) {
				JsonObject tmp1 = iterator.next().getAsJsonObject();
				String t = tmp1.get("object").toString();
				int j = Integer.parseInt(tmp1.get("count").toString());
				try {
					bw.newLine();
				bw.append(t + "," + j);
				System.out.println(t + "," + j);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			bw.flush();
		bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
