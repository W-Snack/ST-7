package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class Task3 {
    public static void Task3Start() {
        try {
            String apiUrl = "https://api.open-meteo.com/v1/forecast?"
                    + "latitude=56&longitude=44"
                    + "&hourly=temperature_2m,rain"
                    + "&current=cloud_cover"
                    + "&timezone=Europe%2FMoscow"
                    + "&forecast_days=1"
                    + "&wind_speed_unit=ms";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject json = new JSONObject(response.toString());
                JSONObject hourly = json.getJSONObject("hourly");

                JSONArray timeArray = hourly.getJSONArray("time");
                JSONArray tempArray = hourly.getJSONArray("temperature_2m");
                JSONArray rainArray = hourly.getJSONArray("rain");

                System.out.println("№\tДата/время\t\tТемпература\tОсадки (мм)");
                System.out.println("-------------------------------------------------------");

                for (int i = 0; i < timeArray.length(); i++) {
                    String time = timeArray.getString(i).replace("T", " ");
                    double temperature = tempArray.getDouble(i);
                    double rain = rainArray.getDouble(i);

                    System.out.printf("%2d\t%-16s\t%6.1f°C\t%9.2f\n",
                            i + 1,
                            time,
                            temperature,
                            rain);
                }
            } else {
                System.out.println("Ошибка HTTP: " + conn.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
