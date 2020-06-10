package yelp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

// Parsing yelp business data into 3 csv file corresponding to the defined schema
public class BusinessJsonToCsv {

  // Example json data
  // {
  //  "business_id": "f9NumwFMBDn751xgFiRbNA",
  //  "name": "The Range At Lake Norman",
  //  "address": "10913 Bailey Rd",
  //  "city": "Cornelius",
  //  "state": "NC",
  //  "postal_code": "28031",
  //  "latitude": 35.4627242,
  //  "longitude": -80.8526119,
  //  "stars": 3.5,
  //  "review_count": 36,
  //  "is_open": 1,
  //  "attributes": {
  //    "BusinessAcceptsCreditCards": "True",
  //    "BikeParking": "True",
  //    "GoodForKids": "False",
  //    "BusinessParking": "{'garage': False, 'street': False, 'validated': False, 'lot': True, 'valet': False}",
  //    "ByAppointmentOnly": "False",
  //    "RestaurantsPriceRange2": "3"
  //  },
  //  "categories": "Active Life, Gun/Rifle Ranges, Guns & Ammo, Shopping",
  //  "hours": {
  //    "Monday": "10:0-18:0",
  //    "Tuesday": "11:0-20:0",
  //    "Wednesday": "10:0-18:0",
  //    "Thursday": "11:0-20:0",
  //    "Friday": "11:0-20:0",
  //    "Saturday": "11:0-20:0",
  //    "Sunday": "13:0-18:0"
  //  }
  //}

  private static final Gson gson = new Gson();

  private static final String[] businessInfoHeader =
          {"business_id", "name", "address", "city", "state", "postal_code", "latitude",
                  "longitude", "stars", "review_count", "is_open", "categories"};
  private static final String[] businessHourHeader =
          {"uuid", "business_id", "day_of_week", "open_time", "close_time"};
  private static final String[] businessAttributeHeader =
          {"uuid", "business_id", "attribute_name", "attribute_value"};

  private static long counter = 1;

  public static void main(String[] args) throws IOException {

    try (
      FileReader reader = new FileReader(
              "/Users/shengfuzhang/Downloads/dataset/yelp-data" +
                      "/yelp_academic_dataset_business.json");
      BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      // write csv headers
      writeCsvHeader(businessInfoHeader, "businessInfo.csv");
      writeCsvHeader(businessHourHeader, "businessHour.csv");
      writeCsvHeader(businessAttributeHeader, "businessAttribute.csv");

      // Reading Json One by One inline
      String jsonString;
      while ((jsonString = bufferedReader.readLine()) != null) {

        System.out.println(counter++ + " | " + jsonString);

        final JsonObject businessJson = gson.fromJson(jsonString, JsonObject.class);
        // store business_id for sub-table
        final JsonElement businessIdElement = businessJson.get("business_id");
        // write to businessInfo csv
        writeCsv(businessJson, businessInfoHeader, "businessInfo.csv");
        // write to businessHour csv
        final List<JsonObject> hourJsonList =
                buildHourJsonList(businessJson.get("hours"), businessIdElement);
        for (JsonObject hourJson : hourJsonList) {
          writeCsv(hourJson, businessHourHeader, "businessHour.csv");
        }
        // write to businessAttribute csv
        final List<JsonObject> attributeJsonList =
                buildAttributeJsonList(businessJson.get("attributes"), businessIdElement);
        for (JsonObject attributeJson : attributeJsonList) {
          writeCsv(attributeJson, businessAttributeHeader, "businessAttribute.csv");
        }
      }
    }
  }

  private static List<JsonObject> buildHourJsonList(JsonElement hoursJsonElement,
                                                    JsonElement businessIdElement) {

    final List<JsonObject> hourJsonList = new ArrayList<>();
    if (hoursJsonElement.isJsonNull()) {
      return hourJsonList;
    }

    final JsonObject fullHoursJson = gson.fromJson(hoursJsonElement, JsonObject.class);
    // split hours into each day
    for (String dayOfWeek: fullHoursJson.keySet()) {
      final String actualHour = fullHoursJson.get(dayOfWeek).getAsString();
      final String openTime = actualHour.split("-")[0];
      final String closeTime = actualHour.split("-")[1];
      final JsonObject dailyHoursJson = new JsonObject();
      dailyHoursJson.add("business_id", businessIdElement);
      dailyHoursJson.addProperty("uuid", UUID.randomUUID().toString());
      dailyHoursJson.addProperty("day_of_week", dayOfWeek);
      dailyHoursJson.addProperty("open_time", openTime);
      dailyHoursJson.addProperty("close_time", closeTime);
      hourJsonList.add(dailyHoursJson);
    }

    return hourJsonList;
  }

  private static List<JsonObject> buildAttributeJsonList(JsonElement attributesJsonElement,
                                                         JsonElement businessIdElement) {

    final List<JsonObject> attributeJsonList = new ArrayList<>();
    if (attributesJsonElement.isJsonNull()) {
      return attributeJsonList;
    }

    final JsonObject fullAttributesJson = gson.fromJson(attributesJsonElement, JsonObject.class);
    // split hours into each day
    for (String attributeName: fullAttributesJson.keySet()) {
      final JsonObject attributeJson = new JsonObject();
      attributeJson.add("business_id", businessIdElement);
      attributeJson.addProperty("uuid", UUID.randomUUID().toString());
      attributeJson.addProperty("attribute_name", attributeName);
      attributeJson.add("attribute_value", fullAttributesJson.get(attributeName));
      attributeJsonList.add(attributeJson);
    }

    return attributeJsonList;
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
