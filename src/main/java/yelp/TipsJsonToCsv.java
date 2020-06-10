package yelp;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

// Parsing yelp tips data into csv file corresponding to the defined schema
public class TipsJsonToCsv {

  // Example tips json data
  // {
  //  "user_id": "hf27xTME3EiCp6NL6VtWZQ",
  //  "business_id": "UYX5zL_Xj9WEc_Wp-FrqHw",
  //  "text": "Here for a quick mtg",
  //  "date": "2013-11-26 18:20:08",
  //  "compliment_count": 0
  //}

  private static final Gson gson = new Gson();

  private static final String[] businessTipsHeader =
          {"uuid", "user_id", "business_id", "text", "date", "compliment_count"};

  private static long counter = 1;

  public static void main(String[] args) throws IOException {

    // parse businessTips
    try (
            FileReader reader = new FileReader(
                    "/Users/shengfuzhang/Downloads/dataset/yelp-data" +
                            "/yelp_academic_dataset_tip.json");
            BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      // write csv headers
      writeCsvHeader(businessTipsHeader, "businessTip.csv");
      // Reading Json One by One inline
      String jsonString;
      while ((jsonString = bufferedReader.readLine()) != null) {

        System.out.println(counter++ + " | " + jsonString);

        final JsonObject fullTipsJson = gson.fromJson(jsonString, JsonObject.class);
        fullTipsJson.addProperty("uuid", UUID.randomUUID().toString());
        fullTipsJson.add("user_id", JsonNull.INSTANCE);
        writeCsv(fullTipsJson, businessTipsHeader, "businessTip.csv");
      }
    }
  }

  private static void writeCsv(JsonObject record, String[] headers, String fileName) throws IOException {
    final StringBuilder sb = new StringBuilder();
    for (String header : headers) {
      sb.append(record.get(header)).append(";");
    }
    sb.deleteCharAt(sb.length() - 1).append("\n");

    final String fullPath = "src/main/resources/" + fileName;
    try (FileOutputStream fos = new FileOutputStream(fullPath, true); // append
         OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    ) {
      writer.append(sb);
    }
  }

  private static void writeCsvHeader(String[] headers, String fileName) throws IOException {

    final StringBuilder sb = new StringBuilder();
    for (String header : headers) {
      sb.append(header).append(";");
    }
    sb.deleteCharAt(sb.length() - 1).append("\n");

    final String fullPath = "src/main/resources/" + fileName;
    try (FileOutputStream fos = new FileOutputStream(fullPath, true); // append
         OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    ) {
      writer.append(sb);
    }
  }
}
