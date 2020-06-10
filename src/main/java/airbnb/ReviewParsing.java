package airbnb;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

public class ReviewParsing {

  // Listing Headers
  // 0 | listing_id, 1 | id, 2 | date, 3 | reviewer_id, 4 | reviewer_name, 5 | comments

  // Listing table schema
  // 'review_id','user_id','listing_id','comment','date'
  private static final int[] reviewIndex = {1, 3, 0, 5, 2};
  private static boolean isHeaderSet = false;

  private static final String recordPrefixFormat = "^[\\d]+;.*$";
  private static final String recordFormat = "^(([^;]*);){5}([^;]*)$";

  public static void main(String[] args) throws IOException {

    try (
            FileReader reader = new FileReader(
                    "/Users/shengfuzhang/Downloads/dataset/" +
                            "/airbnb-reviews.csv");
            BufferedReader bufferedReader = new BufferedReader(reader);
    ) {
      // Reading Json One by One inline
      StringBuilder csvRecord = new StringBuilder(bufferedReader.readLine() + "\n");
      String tempLine;
      while ((tempLine = bufferedReader.readLine()) != null) { // '/n' get trimmed by readline
        if (tempLine.matches(recordPrefixFormat)) {
          // new record find, process previous and reset record. Store tempLine into record
          csvRecord.deleteCharAt(csvRecord.length() - 1);
          if (csvRecord.toString().matches(recordFormat)) {
            // process valid record
            String[] record = csvRecord.toString().split(";", -1);
            System.out.println(Arrays.toString(record));
            writeCsv(record, reviewIndex, "airbnbReview.csv");
          } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! Invalid Record");
          }
          // reset record and append new tempLine
          csvRecord = new StringBuilder(tempLine + "\n");
        } else {
          // no record find, append tempLine into record
          csvRecord.append(tempLine).append("\n");
        }
      }

      // process last record;
      if (csvRecord.toString().matches(recordFormat)) {
        // process valid record
        String[] record = csvRecord.toString().split(";");
        System.out.println(Arrays.toString(record));
        writeCsv(record, reviewIndex, "airbnbReview.csv");
      }
    }
  }

  private static void writeCsv(String[] record, int[] index, String fileName) throws IOException {
    final StringBuilder sb = new StringBuilder();
    if (!isHeaderSet) {
      for (int i : index) { // original file missing double quotes
        sb.append(record[i]).append(";");
      }
      isHeaderSet = true;
    } else {
      for (int i : index) { // original file missing double quotes
        if (i == 1) {
          sb.append("\"").append(UUID.randomUUID()).append("\"").append(";");
        } else if (i == 3) {
          sb.append("null").append(";");
        } else {
          sb.append("\"").append(record[i]).append("\"").append(";");
        }
      }
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
