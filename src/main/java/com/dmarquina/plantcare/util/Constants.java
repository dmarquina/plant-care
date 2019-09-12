package com.dmarquina.plantcare.util;

public class Constants {
  public static final String END_POINT_URL = "https://s3.us-west-2.amazonaws.com";
  public static final String AWS_BUCKET_NAME = "dmarquina-plants";

  public static final int MAX_QUANTITY_PLANTS_DEFAULT = 6;

  public static String getImageURL(String fileName) {
    return END_POINT_URL + "/" + AWS_BUCKET_NAME + "/" + fileName;
  }

  public static String getImageNameFromURL(String imageURL) {
    return imageURL.replace(END_POINT_URL + "/" + AWS_BUCKET_NAME + "/", "");
  }
}
