package airbnb;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

// Splitting airbnb listing data into 7 csv file corresponding to the defined schema
public class ListingSplit {

  // Listing Headers
  // 0 | ID, 1 | Listing Url, 2 | Scrape ID, 3 | Last Scraped, 4 | Name, 5 | Summary, 6 | Space,
  // 7 | Description, 8 | Experiences Offered, 9 | Neighborhood Overview, 10 | Notes, 11 | Transit,
  // 12 | Access, 13 | Interaction, 14 | House Rules, 15 | Thumbnail Url, 16 | Medium Url,
  // 17 | Picture Url, 18 | XL Picture Url, 19 | Host ID, 20 | Host URL, 21 | Host Name,
  // 22 | Host Since, 23 | Host Location, 24 | Host About, 25 | Host Response Time,
  // 26 | Host Response Rate, 27 | Host Acceptance Rate, 28 | Host Thumbnail Url,
  // 29 | Host Picture Url, 30 | Host Neighbourhood, 31 | Host Listings Count,
  // 32 | Host Total Listings Count, 33 | Host Verifications, 34 | Street, 35 | Neighbourhood,
  // 36 | Neighbourhood Cleansed, 37 | Neighbourhood Group Cleansed, 38 | City, 39 | State,
  // 40 | Zipcode, 41 | Market, 42 | Smart Location, 43 | Country Code, 44 | Country,
  // 45 | Latitude, 46 | Longitude, 47 | Property Type, 48 | Room Type, 49 | Accommodates,
  // 50 | Bathrooms, 51 | Bedrooms, 52 | Beds, 53 | Bed Type, 54 | Amenities, 55 | Square Feet,
  // 56 | Price, 57 | Weekly Price, 58 | Monthly Price, 59 | Security Deposit, 60 | Cleaning Fee,
  // 61 | Guests Included, 62 | Extra People, 63 | Minimum Nights, 64 | Maximum Nights,
  // 65 | Calendar Updated, 66 | Has Availability, 67 | Availability 30, 68 | Availability 60,
  // 69 | Availability 90, 70 | Availability 365, 71 | Calendar last Scraped, 72 | Number of Reviews,
  // 73 | First Review, 74 | Last Review, 75 | Review Scores Rating, 76 | Review Scores Accuracy,
  // 77 | Review Scores Cleanliness, 78 | Review Scores Checkin, 79 | Review Scores Communication,
  // 80 | Review Scores Location, 81 | Review Scores Value, 82 | License, 83 | Jurisdiction Names,
  // 84 | Cancellation Policy, 85 | Calculated host listings count, 86 | Reviews per Month,
  // 87 | Geolocation, 88 | Features

  // Listing table schema
  // 'listing_id','host_id','name','summary','space','description','neighborhood_overview',
  // 'notes','transit','access','interaction','house_rules'
  private static final int[] listingInfoIndex = {0, 19, 4, 5, 6, 7, 9, 10, 11, 12, 13, 14};
  // Reservation table schema
  // 'listing_id','daily_price','weekly_price','monthly_price','deposit','cleaning_fee',
  // 'minimum_nights','maximum_nights','cancellation_policy'
  private static final int[] reservationInfoIndex = {0, 56, 57, 58, 59, 60, 63, 64, 84};
  // Location table schema
  // 'listing_id','street','city','state','zip_code','country_code','country','latitude','longitude'
  private static final int[] locationInfoIndex = {0, 34, 38, 39, 40, 43, 44, 45, 46};
  // Room table schema
  // 'listing_id','property_type','room_type','accommodates','bathroom_count','bedroom_count',
  // 'bed_count','bed_type','amenities','features'
  private static final int[] roomInfoIndex = {0, 47, 48, 49, 50, 51, 52, 53, 54, 88};
  // Review table schema
  // 'listing_id','review_count','rating_score','accuracy_score','cleanliness_score',
  // 'checkin_score','communication_score','location_score','value_score'
  private static final int[] reviewInfoIndex = {0, 72, 75, 76, 77, 78, 79, 80, 81};
  // URL table schema
  // 'listing_id','listing_url','thumbnail_url','medium_url','picture_url','xl_picture_url'
  private static final int[] urlInfoIndex = {0, 1, 15, 16, 17, 18};
  // Host table schema
  // 'host_id','host_url','name','since','location','about','response_time','response_rate',
  // 'listing_count','verification'
  private static final int[] hostInfoIndex = {19, 20, 21, 22, 23, 24, 25, 26, 31, 33};

  private static final String recordPrefixFormat = "^[\\d]+;.*$";
  private static final String recordFormat = "^(([^;]*);){88}([^;]*)$";

  private static long successCount = 0;
  private static long failureCount = 0;

  public static void main(String[] args) throws IOException {

    try (
            FileReader reader = new FileReader(
              "/Users/shengfuzhang/Downloads/dataset/" +
                      "/airbnb-listings.csv");
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
            // write to listing csv
            writeCsv(record, listingInfoIndex, "airbnbListing.csv");
            // write to reservation csv
            writeCsv(record, reservationInfoIndex, "airbnbListingReservation.csv");
            // write to location csv
            writeCsv(record, locationInfoIndex, "airbnbListingLocation.csv");
            // write to room csv
            writeCsv(record, roomInfoIndex, "airbnbListingRoom.csv");
            // write to review csv
            writeCsv(record, reviewInfoIndex, "airbnbListingReview.csv");
            // write to url csv
            writeCsv(record, urlInfoIndex, "airbnbListingUrl.csv");
            // write to host csv
            writeCsv(record, hostInfoIndex, "airbnbHost.csv");
            successCount++;
          } else {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! Invalid Record !!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(Arrays.toString(csvRecord.toString().split(";", -1)).length());
            System.out.println(Arrays.toString(csvRecord.toString().split(";", -1)));
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!! Invalid Record !!!!!!!!!!!!!!!!!!!!!!!");
            failureCount++;
          }
          // reset record and append new tempLine
          csvRecord = new StringBuilder(tempLine + "\n");
          System.out.println("successCount: " + successCount + " | failureCount: " + failureCount);
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
        // write to listing csv
        writeCsv(record, listingInfoIndex, "airbnbListing.csv");
        // write to reservation csv
        writeCsv(record, reservationInfoIndex, "airbnbListingReservation.csv");
        // write to location csv
        writeCsv(record, locationInfoIndex, "airbnbListingLocation.csv");
        // write to room csv
        writeCsv(record, roomInfoIndex, "airbnbListingRoom.csv");
        // write to review csv
        writeCsv(record, reviewInfoIndex, "airbnbListingReview.csv");
        // write to url csv
        writeCsv(record, urlInfoIndex, "airbnbListingUrl.csv");
        // write to host csv
        writeCsv(record, hostInfoIndex, "airbnbHost.csv");
        successCount++;
      } else {
        failureCount++;
      }
      System.out.println("successCount: " + successCount + " | failureCount: " + failureCount);
    }
  }

  private static void writeCsv(String[] record, int[] index, String fileName) throws IOException {
    final StringBuilder sb = new StringBuilder();
    for (int i : index) { // original file missing double quotes
      if ("".equals(record[i].trim())) { // solving "null" string type error issue
        sb.append("null").append(";");
      } else {
        sb.append("\"").append(record[i]).append("\"").append(";");
      }
    }
    sb.deleteCharAt(sb.length() - 1).append("\r\n");

    final String fullPath = "src/main/resources/" + fileName;
    try (FileOutputStream fos = new FileOutputStream(fullPath, true); // append
         OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
    ) {
      writer.append(sb);
    }
  }
}