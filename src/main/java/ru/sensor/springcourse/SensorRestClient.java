package ru.sensor.springcourse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sensor.springcourse.model.Sensor;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

//TODO: 1) разобраться как передавать SearchDTO + чтобы поля dateFrom/dateTo были LocalDate
//      2) добавить валидацию на вводимые параметры dateFrom/dateTo

public class SensorRestClient implements WeatherChart<CategoryChart> {
    static RestTemplate restTemplate = new RestTemplate();
    static List<Date> dateTimes = new ArrayList<>();
    static List<Float> temperatures = new ArrayList<>();
    static String urlGetMeasurements = "http://localhost:8080/measurements";

    static String responseForGet = restTemplate.getForObject(urlGetMeasurements, String.class);

    static String urlGetMeasurementsBetweenDates = "http://localhost:8080/findMeasurements/{dateFrom}/{dateTo}";

    static String dateFrom = "2024-02-01";

    static String dateTo = "2024-06-01";

    static String responseBetweenDates = restTemplate.getForObject(urlGetMeasurementsBetweenDates, String.class, dateFrom, dateTo);

    public static void main(String[] args) throws JsonProcessingException {

//        getAllSensors(restTemplate);
//        registerSensor(restTemplate);
//        addMeasurement(restTemplate);
//        getAllMeasurements();
//        showVisualisation(restTemplate);
//        getRainyDaysCount(restTemplate);

        /**
         * пример создания диаграммы с использованием интерфейса WeatherChart
         */
//        WeatherChart<CategoryChart> exampleChart = new SensorRestClient();
//        CategoryChart chart = exampleChart.getChart(restTemplate);
//        new SwingWrapper<>(chart).displayChart();

        /**
         * Без интерфейса - просто статический метод
         */

        new SwingWrapper<>(getChartNew()).displayChart();

        getMeasurementsBetweenDates(restTemplate);
    }

    // регистрация нового сенсора
    public static void registerSensor(RestTemplate restTemplate) {
        Map<String, String> request = new HashMap<>();
        request.put("name", "sensorC");
        String urlPost = "http://localhost:8080/sensor/registration";
        try {
            HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(request);
            String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
            System.out.println(responseForPost);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void getAllSensors(RestTemplate restTemplate) {
        String urlForSensor = "http://localhost:8080/allSensors";
        String responseForSensor = restTemplate.getForObject(urlForSensor, String.class);
        System.out.println(responseForSensor);
    }

    // получение всех измерений
    public static void getAllMeasurements() {
        System.out.println(responseForGet);
    }

    // получение количества дождливых дней
    public static void getRainyDaysCount(RestTemplate restTemplate) {
        String urlGet = "http://localhost:8080/measurements/getRainyDaysCount";
        String responseForGet = restTemplate.getForObject(urlGet, String.class);
        System.out.println(responseForGet);
    }

    // добавление измерений
    public static void addMeasurement(RestTemplate restTemplate) {
        List<Sensor> sensorList = createSensorList();

        List<Boolean> listBoolean = new ArrayList<>();
        listBoolean.add(true);
        listBoolean.add(false);

        Random random = new Random();
        for (int i = 0; i < 10; i++) {

            float generatedTemperature = random.nextFloat(-100, 100);
            float roundTemperature = (float) (Math.round(generatedTemperature * 10.0) / 10.0);

            Map<String, Object> request = new HashMap<>();
            request.put("raining", listBoolean.get(random.nextInt(listBoolean.size())));
            request.put("value", roundTemperature);
            request.put("sensor", sensorList.get(random.nextInt(sensorList.size())));
            String urlPost = "http://localhost:8080/measurements/add";
            try {
                HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(request);
                String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
                System.out.println(responseForPost);
            } catch (HttpClientErrorException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public static List<Sensor> createSensorList() {
        List<Sensor> sensorList = new ArrayList<>();
        Sensor sensorA = new Sensor();
        sensorA.setName("sensorA");
        Sensor sensorB = new Sensor();
        sensorB.setName("sensorB");
        Sensor sensorC = new Sensor();
        sensorC.setName("sensorC");
        sensorList.add(sensorA);
        sensorList.add(sensorB);
        sensorList.add(sensorC);
        return sensorList;
    }

    // данный метод использую, если не имплементируется интерфейс WeatherChart
    // нужен для построения графика
    public static CategoryChart getChartNew() throws JsonProcessingException {
        fillXAndYForChart();

        CategoryChart chart = new CategoryChartBuilder().width(1300).height(700).build();

        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setDatePattern("dd-MMM");
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        chart.addSeries("Погода", dateTimes, temperatures);
        return chart;
    }

    // заполняю массивы данных (Х - температура, Y - даты)
    public static void fillXAndYForChart() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(responseBetweenDates);

        Iterator<JsonNode> jsonNodeIterator = jsonNode.iterator();
        while (jsonNodeIterator.hasNext()) {
            JsonNode jsonNodeInside = jsonNodeIterator.next();
            float value = jsonNodeInside.get("value").floatValue();
            String measurementDate = jsonNodeInside.get("measurementDate").textValue();
            Date out = Date.from(LocalDateTime.parse(measurementDate).atZone(ZoneId.systemDefault()).toInstant());
            dateTimes.add(out);
            Collections.sort(dateTimes);
            temperatures.add(value);
        }
    }

    // метод работает с переменными из PathVariable
    //TODO: разобраться как передать через restTemplate RequestBody (SearchDTO)
    public static void getMeasurementsBetweenDates(RestTemplate restTemplate) {

        String url = "http://localhost:8080/findMeasurements/{dateFrom}/{dateTo}";
        String response = restTemplate.getForObject(url, String.class, dateFrom, dateTo);

        System.out.println(response);

    }

    // можно работать с серией диаграмм
    public static void showVisualisation(RestTemplate restTemplate) throws JsonProcessingException {
        int numCharts = 1;

        List<XYChart> charts = new ArrayList<>();

        for (int i = 0; i < numCharts; i++) {
            XYChart chart = new XYChartBuilder().xAxisTitle("X").yAxisTitle("Y").width(1400).height(600).build();
            chart.getStyler().setYAxisMin(-100.0);
            chart.getStyler().setYAxisMax(100.0);

            XYSeries series = chart.addSeries("Погода" + i, dateTimes, temperatures);
            series.setMarkerColor(Color.ORANGE);
            series.setMarker(SeriesMarkers.RECTANGLE);
            charts.add(chart);
        }
        new SwingWrapper<>(charts).displayChartMatrix();
    }

    // если работать с интерфейсом и его имплементировать, то использую данный метод
    @Override
    public CategoryChart getChart() {
        CategoryChart chart = new CategoryChartBuilder().width(1300).height(700).title("Stick").build();

        chart.getStyler().setChartTitleVisible(true);
        chart.getStyler().setDatePattern("dd-MMM");
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        chart.getStyler().setDefaultSeriesRenderStyle(CategorySeries.CategorySeriesRenderStyle.Line);
        chart.addSeries("Погода", dateTimes, temperatures);

        return chart;
    }

}
