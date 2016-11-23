package cn.larry.datawash;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by larry on 7/18/2016.
 */
public class XzqhCrawler {

    private static final String URL_BASE = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2014/";

    public static void main(String[] args) throws IOException {
        String rootUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2014/index.html";
        Document doc = Jsoup.parse(new URL(rootUrl), 30000);
        Elements elements = doc.select(".provincetr a");
        List<Province> provinceList = new ArrayList<>();
        elements.forEach(e -> {
            String href = e.attr("href");
            Province province = new Province(href.replace(".html", ""), e.text());
            province.setUrl(getUrlPathPrefix(rootUrl) + href);
            provinceList.add(province);
        });
        provinceList.forEach(XzqhCrawler::getProvinceInfo);
        Path storePath = Paths.get("D:\\home\\data\\xzqh-all.json");
        Gson gson = new Gson();
        List<String> strings = provinceList.stream().map(gson::toJson).collect(Collectors.toList());
        Files.createFile(storePath);
        Files.write(storePath, strings);
    }

    private static String getUrlPathPrefix(String url) {
        String[] ss = url.split("/");
        ss[ss.length - 1] = "";
        return String.join("/", ss);
    }

    private static void getProvinceInfo(Province provice) {
        try {
            Document doc = Jsoup.parse(new URL(provice.getUrl()), 30000);
            Elements elements = doc.select(".citytr");
            elements.forEach(element -> {
                try {
                    String code = element.select("td a").get(0).text();
                    String href = element.select("td a").get(0).attr("href");
                    String name = element.select("td a").get(1).text();
                    City city = new City(code, name);
                    city.setUrl(getUrlPathPrefix(provice.getUrl()) + href);
                    getCityInfo(city);
                    provice.getCities().add(city);
                } catch (Exception e) {
                    System.out.println("*************" + element.html());
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            System.out.println("******************" + provice.toString());
            e.printStackTrace();
        }
    }

    private static void getCityInfo(City city) {
        try {
            Document doc = Jsoup.parse(new URL(city.getUrl()), 30000);
            Elements elements = doc.select(".countytr");
            elements.forEach(element -> {
                try {
                    String code = element.select("td a").get(0).text();
                    String href = element.select("td a").get(0).attr("href");
                    String name = element.select("td a").get(1).text();
                    District district = new District(code, name);
                    district.setUrl(getUrlPathPrefix(city.getUrl()) + href);
                    getDistrictInfo(district);
                    city.getDistricts().add(district);
                } catch (Exception e) {
                    System.out.println("*************" + element.html());
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            System.out.println("******************" + city.toString());
            e.printStackTrace();
        }
    }

    private static void getDistrictInfo(District district) {
        try {
            Document doc = Jsoup.parse(new URL(district.getUrl()), 30000);
            Elements elements = doc.select(".towntr");
            elements.forEach(element -> {
                try {
                    String code = element.select("td a").get(0).text();
                    String href = element.select("td a").get(0).attr("href");
                    String name = element.select("td a").get(1).text();
                    Street street = new Street(code, name);
                    street.setUrl(getUrlPathPrefix(district.getUrl()) + href);
                    getStreetInfo(street);
                    district.getStreets().add(street);
                } catch (Exception e) {
                    System.out.println("*************" + element.html());
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            System.out.println("******************" + district.toString());
            e.printStackTrace();
        }
    }

    private static void getStreetInfo(Street street) {
        try {
            Document doc = Jsoup.parse(new URL(street.getUrl()), 30000);
            Elements elements = doc.select(".villagetr");
            elements.forEach(element -> {
                try {
                    String code = element.select("td").get(0).text();
                    String type = element.select("td").get(1).text();
                    String name = element.select("td").get(2).text();
                    Village village = new Village(code, name, type);
                    street.getVillages().add(village);
                } catch (Exception e) {
                    System.out.println("*************" + element.html());
                    e.printStackTrace();
                }

            });
        } catch (Exception e) {
            System.out.println("****************" + street.toString());
            e.printStackTrace();
        }
    }


}
