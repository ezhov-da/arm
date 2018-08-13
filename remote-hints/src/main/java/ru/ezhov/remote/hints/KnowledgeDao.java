package ru.ezhov.remote.hints;

import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class KnowledgeDao {
    private static final Logger LOG = Logger.getLogger(KnowledgeDao.class.getName());

    public static void main(String[] args) {
        KnowledgeDao knowledgeDao = new KnowledgeDao();
        try {
            DataInfo dataInfo = knowledgeDao.getDataInfo();
            System.out.println(dataInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DataInfo getDataInfo() throws Exception {
        LOG.finest("Получение информации");
        String urlText = "https://prog-tools.ru:64646/knowledges";
        HttpURLConnection httpURLConnection =
                (HttpURLConnection) new URL(urlText).openConnection();
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setDoInput(true);
        InputStream inputStream;
        if (httpURLConnection.getResponseCode() == 200) {
            inputStream = httpURLConnection.getInputStream();
            String text = toText(inputStream);
            Gson gson = new Gson();
            LOG.finest("Информация получена");
            return gson.fromJson(text, DataInfo.class);
        } else {
            inputStream = httpURLConnection.getErrorStream();
            String text = toText(inputStream);
            throw new Exception(text);
        }
    }

    private String toText(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try (Scanner scanner = new Scanner(inputStream, "UTF-8")) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }

    public String getRawData(Knowledge knowledge) throws Exception {
        LOG.finest("Получение информации");
        String urlText = knowledge.getRawUrl();
        HttpURLConnection httpURLConnection =
                (HttpURLConnection) new URL(urlText).openConnection();
        httpURLConnection.setConnectTimeout(60000);
        httpURLConnection.setDoInput(true);
        InputStream inputStream;
        if (httpURLConnection.getResponseCode() == 200) {
            inputStream = httpURLConnection.getInputStream();
            return toText(inputStream);
        } else {
            inputStream = httpURLConnection.getErrorStream();
            return toText(inputStream);
        }
    }
}
