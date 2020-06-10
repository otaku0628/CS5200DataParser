package yelp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Parsing yelp check-in data into csv file corresponding to the defined schema
public class CheckinJsonToCsv {

  // Example checkin json data
  // {
  //  "business_id": "--1UhMGODdWsrMastO9DZw",
  //  "date": "2016-04-26 19:49:16, 2016-08-30 18:36:57, 2016-10-15 02:45:18, 2016-11-18 01:54:50, 2017-04-20 18:39:06, 2017-05-03 17:58:02, 2019-03-19 22:04:48"
  //}

  private static final Gson gson = new Gson();

  private static final String[] businessCheckinHeader =
          {"uuid", "user_id", "business_id", "checkin_time"};

  private static long counter = 1;

  public static void main(String[] args) throws IOException {

    try (
            FileReader reader = new FileReader(
                    "/Users/shengfuzhang/Downloads/dataset/yelp-data" +
                            "/yelp_academic_dataset_checkin.json");
            BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      // write csv headers
      writeCsvHeader(businessCheckinHeader, "businessCheckin.csv");

      // Reading Json One by One inline
      String jsonString;
      while ((jsonString = bufferedReader.readLine()) != null) {

        System.out.println(counter++ + " | " + jsonString);

        final JsonObject fullCheckinJson = gson.fromJson(jsonString, JsonObject.class);
        // store business_id for sub-table
        final JsonElement businessIdElement = fullCheckinJson.get("business_id");
        // write to businessCheckin csv
        final JsonElement dateElement = fullCheckinJson.get("date");
        if (dateElement.isJsonNull()) {
          continue;
        }

        final List<JsonObject> checkinJsonList = new ArrayList<>();
        final String[] checkinTimes = dateElement.getAsString().split(", ");
        for (String checkinTime : checkinTimes) {
          final JsonObject checkinJson = new JsonObject();
          checkinJson.addProperty("uuid", UUID.randomUUID().toString());
          checkinJson.add("user_id", JsonNull.INSTANCE);
          checkinJson.add("business_id", businessIdElement);
          checkinJson.addProperty("checkin_time", checkinTime);
          checkinJsonList.add(checkinJson);
        }

        for (JsonObject checkinJson : checkinJsonList) {
          writeCsv(checkinJson, businessCheckinHeader, "businessCheckin.csv");
        }
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
