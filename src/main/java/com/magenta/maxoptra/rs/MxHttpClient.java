package com.magenta.maxoptra.rs;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import java.io.*;

public class MxHttpClient {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String POST_URL_VEHICLES = "/rest/2/distribution-api/objects/importVehicles";
    private static final String POST_URL_ASSIGN_PERF = "/rest/2/distribution-api/objects/assignPerformersToVehicles";
    private static final String POST_URL_PERF = "/rest/2/distribution-api/objects/importPerformers";

    private static final HttpClient client = HttpClientBuilder.create().build();

    public static void GenerateVehicles(String acc, String usr, String pass, String targetServer, byte[] dataArr) {
        String result;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            stringBuilder.append("<apiRequest>\n");
            stringBuilder.append(String.format("<accountID>%s</accountID>\n", acc));
            stringBuilder.append(String.format("<user>%s</user>\n", usr));
            stringBuilder.append(String.format("<password>%s</password>\n", pass));
            stringBuilder.append("<vehiclesDetails>\n");
            stringBuilder.append("<units />\n");
            stringBuilder.append("<vehicles>\n");
            String line;
            int index = 0;
            try (BufferedReader reader = new BufferedReader(new StringReader(new String(dataArr)))) {
                while ((line = reader.readLine()) != null) {
                    if (index++ == 0)
                        continue;
                    String[] data = line.split("\\;");
                    StringBuilder sb = new StringBuilder();
                    sb.append("<vehicle name=\"").append(data[5]).append("\" active=\"true\" suspended=\"false\" costByDistance=\"").append(data[7]).append("\" maxWeight=\"")
                            .append(data[6]).append("\" maxVolume=\"0\" averageSpeed=\"").append(data[9])
                            .append("\" deviceCode=\"\" color=\"\" allowSpeedCorrectionFactor=\"false\" areaOfControl=\"")
                            .append(data[10])/*.append("\" address=\"").append(data[11])*/.append("\">\n");
                    if (data[8].length() >= 2) {
                        sb.append("<vehicleRequirements>\n<vehicleRequirement name=\"")
                                .append(data[8]).append("\" abbreviation=\"").append(data[8].substring(0, 2)).append("\"/>\n</vehicleRequirements>\n");
                    } else if (data[8].length() < 2) { sb.append("<vehicleRequirements>\n<vehicleRequirement name=\"")
                            .append(data[8]).append("\" abbreviation=\"").append(data[8]).append("\"/>\n</vehicleRequirements>\n");}
                    sb.append("</vehicle>\n");

                    stringBuilder.append(sb.toString());
                }
                stringBuilder.append("</vehicles>\n");
                stringBuilder.append("</vehiclesDetails>\n");
                stringBuilder.append("</apiRequest>\n");
                System.out.println(stringBuilder.toString());
            }
            result = stringBuilder.toString().trim();
            //System.out.println(result);
            String POST_URL_VEHICLES_Sum = "http://" + targetServer + POST_URL_VEHICLES;
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost httpPost = new HttpPost(POST_URL_VEHICLES_Sum);
            httpPost.setHeader("User-Agent", USER_AGENT);
            httpPost.setHeader("Content-Type", "application/xml");
            HttpEntity entity = new ByteArrayEntity(result.getBytes("UTF-8"));
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);
            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + POST_URL_VEHICLES_Sum);
            System.out.println("Response Code : " + responseCode);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void GeneratePerformers(String acc, String usr, String pass, String targetServer, byte[] dataArr) {

        String result;
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
            stringBuilder.append("<apiRequest>");
            stringBuilder.append(String.format("\n<accountID>%s</accountID>", acc));
            stringBuilder.append(String.format("\n<user>%s</user>", usr));
            stringBuilder.append(String.format("\n<password>%s</password>", pass));
            stringBuilder.append("\n<performersDetails>");
            stringBuilder.append("\n<units/>");
            stringBuilder.append("\n<performers>");
            String line;

            int index = 0;
            try (BufferedReader reader = new BufferedReader(new StringReader(new String(dataArr)))) {
                while ((line = reader.readLine()) != null) {
                    if (index++ == 0)
                        continue;
                    String[] data = line.split("\\;");
                    //StringBuilder sb = new StringBuilder();
                    String text = "\n<performer name=\"" + data[0] + "\" email=\"" + data[0].replace(" ", "") + "@com.com\" login=\"" + data[0].replace(" ", "") + "\" password=\"123\" priceForOneHour=\"" +
                            data[2] + "\" allowDailyDrivingLimit=\"false\" allowDailyWorkingLimit=\"false\" allowRunTimeLimit=\"false\" breakShift=\"0\" areaOfControl=\"" + data[10] +
                            "\">\n" +
                            "            <availabilities>\n" +
                            "               <availability weekDay=\"holiday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"wednesday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"monday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"thursday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"sunday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"saturday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"tuesday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "               <availability weekDay=\"friday\" from=\"" + data[3] + "\" to=\"" + data[4] + "\" fixedDayStart=\"false\"/>\n" +
                            "            </availabilities>\n" +
                            "</performer>\n";

                    stringBuilder.append(text);
                }
                    stringBuilder.append("\n</performers>");
                    stringBuilder.append("\n</performersDetails>");
                    stringBuilder.append("\n</apiRequest>");
                    result = stringBuilder.toString();
                    System.out.println(result);
                    String POST_URL_PERFORMERS_Sum = "http://" + targetServer + POST_URL_PERF;
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost(POST_URL_PERFORMERS_Sum);
                    httpPost.setHeader("User-Agent", USER_AGENT);
                    httpPost.setHeader("Content-Type", "application/xml");
                    HttpEntity entity = new ByteArrayEntity(result.getBytes("UTF-8"));
                    httpPost.setEntity(entity);
                    HttpResponse response = client.execute(httpPost);
                    int responseCode = response.getStatusLine().getStatusCode();

                    System.out.println("\nSending 'POST' request to URL : " + POST_URL_PERFORMERS_Sum);
                    System.out.println("Response Code : " + responseCode);
                    System.out.println(EntityUtils.toString(response.getEntity()));



            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }


    public static void GenerateAssignments(String acc, String usr, String pass, String targetServer, byte[] dataArr) {

        String result;
        BufferedReader reader = new BufferedReader(new StringReader(new String(dataArr)));
        StringBuilder stringBuilder = new StringBuilder();


        try {

            stringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            stringBuilder.append("\n<apiRequest>");
            stringBuilder.append(String.format("\n<accountID>%s</accountID>", acc));
            stringBuilder.append(String.format("\n<user>%s</user>", usr));
            stringBuilder.append(String.format("\n<password>%s</password>", pass));
            stringBuilder.append("\n<allocationsDetails>");
            stringBuilder.append("\n<allocations>");
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                if (index++ == 0)
                    continue;
                String[] data = line.split("\\;");
                String text = "\n<allocation vehicle=\"" + data[5] + "\" performer=\"" + data[0] + "\" />";
                stringBuilder.append(text);

            }
            stringBuilder.append("\n</allocations>");
            stringBuilder.append("\n</allocationsDetails>");
            stringBuilder.append("\n</apiRequest>");
            result = stringBuilder.toString();
            System.out.println(result);
            HttpClient client = HttpClientBuilder.create().build();
            String POST_URL_ASSIGNMENT = "http://" + targetServer + POST_URL_ASSIGN_PERF;
            HttpPost httpPost = new HttpPost(POST_URL_ASSIGNMENT);
            httpPost.setHeader("User-Agent", USER_AGENT);
            httpPost.setHeader("Content-Type", "application/xml");
            HttpEntity entity = new ByteArrayEntity(result.getBytes("UTF-8"));
            httpPost.setEntity(entity);
            HttpResponse response = client.execute(httpPost);

            int responseCode = response.getStatusLine().getStatusCode();

            System.out.println("\nSending 'POST' request to URL : " + POST_URL_ASSIGNMENT);
            System.out.println("Response Code : " + responseCode);
            System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }
}


