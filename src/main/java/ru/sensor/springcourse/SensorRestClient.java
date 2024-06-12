package ru.sensor.springcourse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.sensor.springcourse.dto.MeasurementDTO;
import ru.sensor.springcourse.dto.SearchDTO;
import ru.sensor.springcourse.model.Sensor;
import ru.sensor.springcourse.util.MeasurementResponse;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

//TODO: 1) + разобраться как передавать SearchDTO + чтобы поля dateFrom/dateTo были LocalDate
//      2) добавить валидацию на вводимые параметры dateFrom/dateTo

public class SensorRestClient implements WeatherChart<CategoryChart> {
    static RestTemplate restTemplate = new RestTemplate();
    static List<Date> dateTimes = new ArrayList<>();
    static List<Float> temperatures = new ArrayList<>();
    static String urlGetMeasurements = "http://localhost:8080/measurements";

    static String urlGetMeasurementsBetweenDates = "http://localhost:8080/findMeasurements/{dateFrom}/{dateTo}";

    static String responseForGet = restTemplate.getForObject(urlGetMeasurements, String.class);

    static String dateFrom = "2024-02-01";

    static String dateTo = "2024-06-01";

    static String responseBetweenDates = restTemplate.getForObject(urlGetMeasurementsBetweenDates, String.class, dateFrom, dateTo);

    public static void main(String[] args) throws JsonProcessingException {

//        getAllSensors(restTemplate);
        registerSensor(restTemplate);
//        addMeasurement(restTemplate);
//        getAllMeasurements();
//        showVisualisation(restTemplate);
//        getRainyDaysCount(restTemplate);
//        addSingleMeasurement(restTemplate);

        /**
         * пример создания диаграммы с использованием интерфейса WeatherChart
         */
//        WeatherChart<CategoryChart> exampleChart = new SensorRestClient();
//        CategoryChart chart = exampleChart.getChart(restTemplate);
//        new SwingWrapper<>(chart).displayChart();

        /**
         * Без интерфейса - просто статический метод
         */

//        new SwingWrapper<>(getChartNew()).displayChart();

//        getMeasurementsBetweenDates(restTemplate);

//        getMeasurementsBetweenDatesWithSearchDTO(restTemplate);
//        getAllMeasurements();
    }

    // регистрация нового сенсора
    public static void registerSensor(RestTemplate restTemplate) {
        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", "sensorK");
        String urlPost = "http://localhost:8080/sensor/registration";

        makePostRequestWithJSONData(restTemplate,jsonData,urlPost);
    }

    public static void getAllSensors(RestTemplate restTemplate) {
        String urlForSensor = "http://localhost:8080/allSensors";
        String responseForSensor = restTemplate.getForObject(urlForSensor, String.class);
        System.out.println(responseForSensor);
    }

    // получение всех измерений
    public static void getAllMeasurements() {
        String urlGetMeasurements = "http://localhost:8080/measurements";
        MeasurementResponse responseForGet = restTemplate.getForObject(urlGetMeasurements, MeasurementResponse.class);

        assert responseForGet != null;
        responseForGet.getMeasurementDTOS().forEach(System.out::println);

        /**
         * После того как сделал отдельный класс MeasurementResponse на сервере
         * данный способ перестал работать
         */
        // TODO: Причина была в том, что отсутствовал "конструктор по умолчанию" в MeasurementResponse
        // TODO: сделать об этом запись

        // получение ответа с помощью ResponseEntity
//        ResponseEntity<MeasurementDTO[]> response = restTemplate.getForEntity(urlGetMeasurements, MeasurementDTO[].class);
//        MeasurementDTO[] measurementDTOs = response.getBody();
//        Arrays.stream(measurementDTOs).toList().stream()
//                .forEach(measurementDTO -> System.out.println(measurementDTO));

//        ResponseEntity<MeasurementResponse> response = restTemplate.getForEntity(urlGetMeasurements, MeasurementResponse.class);
//        MeasurementResponse measurementDTOs = response.getBody();
//        measurementDTOs.getMeasurementDTOS().stream()
//                .forEach(measurementDTO -> System.out.println(measurementDTO));

    }

    // метод работает с переменными из PathVariable
    public static void getMeasurementsBetweenDates(RestTemplate restTemplate) {

        String url = "http://localhost:8080/findMeasurements/{dateFrom}/{dateTo}";
        String response = restTemplate.getForObject(url, String.class, dateFrom, dateTo);

        System.out.println(response);

    }

    //В данном случе мы работаем с PostMapping вместо GetMapping
    public static void getMeasurementsBetweenDatesWithSearchDTO(RestTemplate restTemplate) {

        String url = "http://localhost:8080/findMeasurements";
        SearchDTO searchDTO = new SearchDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        searchDTO.setDateTo(LocalDate.parse("2024-05-31", formatter));
        searchDTO.setDateFrom(LocalDate.parse("2024-02-01", formatter));

        String response = restTemplate.postForObject(url, searchDTO, String.class);
        System.out.println(response);

        //работа с ResponseEntity вместо String
        ResponseEntity<MeasurementDTO[]> responseEntity = restTemplate.postForEntity(url, searchDTO, MeasurementDTO[].class);
        MeasurementDTO[] measurementDTOs = responseEntity.getBody();
        Arrays.stream(measurementDTOs).toList().stream()
                .forEach(measurementDTO -> System.out.println(measurementDTO));

    }

    // получение количества дождливых дней
    public static void getRainyDaysCount(RestTemplate restTemplate) {
        String urlGet = "http://localhost:8080/measurements/getRainyDaysCount";
        String responseForGet = restTemplate.getForObject(urlGet, String.class);
        System.out.println(responseForGet);
    }

    // добавление измерений
    public static void addSingleMeasurement(RestTemplate restTemplate) {
        String urlPost = "http://localhost:8080/measurements/add";
        Random random = new Random();

        float generatedTemperature = random.nextFloat(-100, 100);
        float roundTemperature = (float) (Math.round(generatedTemperature * 10.0) / 10.0);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("raining", random.nextBoolean());
        jsonData.put("value", roundTemperature);
        jsonData.put("sensor", Map.of("name", "sensor"));

        makePostRequestWithJSONData(restTemplate, jsonData, urlPost);
    }

    public static void makePostRequestWithJSONData(RestTemplate restTemplate, Map<String, Object> jsonData, String urlPost) {
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<>(jsonData);
        try {
            String responseForPost = restTemplate.postForObject(urlPost, httpEntity, String.class);
            System.out.println(responseForPost + " - Запрос успешно отправлен");
        } catch (HttpClientErrorException e) {
            System.out.println("Ошибка");
            System.out.println(e.getMessage());
        }
    }

    // добавление измерений
    public static void addMeasurement(RestTemplate restTemplate) {
        List<Sensor> sensorList = createSensorList();
        String urlPost = "http://localhost:8080/measurements/add";

        Random random = new Random();
        for (int i = 0; i < 10; i++) {

            float generatedTemperature = random.nextFloat(-100, 100);
            float roundTemperature = (float) (Math.round(generatedTemperature * 10.0) / 10.0);

            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("raining", random.nextBoolean());
            jsonData.put("value", roundTemperature);
            jsonData.put("sensor", sensorList.get(random.nextInt(sensorList.size())));

            makePostRequestWithJSONData(restTemplate,jsonData,urlPost);
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
