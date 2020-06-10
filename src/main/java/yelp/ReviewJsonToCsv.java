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

// Parsing yelp review data into csv file corresponding to the defined schema
public class ReviewJsonToCsv {

  // Example review json data
  // {
  //  "review_id": "xQY8N_XvtGbearJ5X4QryQ",
  //  "user_id": "OwjRMXRC0KyPrIlcjaXeFQ",
  //  "business_id": "-MhfebM0QIsKt87iDN-FNw",
  //  "stars": 2,
  //  "useful": 5,
  //  "funny": 0,
  //  "cool": 0,
  //  "text": "As someone who has worked with many museums, I was eager to visit this gallery on my most recent trip to Las Vegas. When I saw they would be showing infamous eggs of the House of Faberge from the Virginia Museum of Fine Arts (VMFA), I knew I had to go!\n\nTucked away near the gelateria and the garden, the Gallery is pretty much hidden from view. It's what real estate agents would call \"cozy\" or \"charming\" - basically any euphemism for small.\n\nThat being said, you can still see wonderful art at a gallery of any size, so why the two *s you ask? Let me tell you:\n\n* pricing for this, while relatively inexpensive for a Las Vegas attraction, is completely over the top. For the space and the amount of art you can fit in there, it is a bit much.\n* it's not kid friendly at all. Seriously, don't bring them.\n* the security is not trained properly for the show. When the curating and design teams collaborate for exhibitions, there is a definite flow. That means visitors should view the art in a certain sequence, whether it be by historical period or cultural significance (this is how audio guides are usually developed). When I arrived in the gallery I could not tell where to start, and security was certainly not helpful. I was told to \"just look around\" and \"do whatever.\" \n\nAt such a *fine* institution, I find the lack of knowledge and respect for the art appalling.",
  //  "date": "2015-04-15 05:21:16"
  //}

  private static final Gson gson = new Gson();

  private static final String[] businessReviewHeader =
          {"uuid", "user_id", "business_id", "stars", "useful", "text", "date"};

  private static long counter = 1;

  public static void main(String[] args) throws IOException {

    // parse businessReview
    try (
            FileReader reader = new FileReader(
                    "/Users/shengfuzhang/Downloads/dataset/yelp-data" +
                            "/yelp_academic_dataset_review.json");
            BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      // write csv headers
      writeCsvHeader(businessReviewHeader, "businessReview.csv");
      // Reading Json One by One inline
      String jsonString;
      while ((jsonString = bufferedReader.readLine()) != null) {

        System.out.println(counter++ + " | " + jsonString);

        final JsonObject fullReviewJson = gson.fromJson(jsonString, JsonObject.class);
        fullReviewJson.addProperty("uuid", UUID.randomUUID().toString());
        fullReviewJson.add("user_id", JsonNull.INSTANCE);
        writeCsv(fullReviewJson, businessReviewHeader, "businessReview.csv");
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
