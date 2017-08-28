package com.oyster.card.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Created by myousaf on 7/21/17.
 * Ideally I would like to save this information in the database
 */
@Component
public class ZoneUtils {

    private static Map<String, List<String>> stationsInfo = new HashMap<>();

    public static List<String> getZone(String station) {
        return stationsInfo.get(station);
    }

    @PostConstruct
    private void init() {
        stationsInfo.put(Station.Holborn.getStation(), Arrays.asList("1"));
        stationsInfo.put(Station.EarlsCourt.getStation(), Arrays.asList("1", "2"));
        stationsInfo.put(Station.Wimbledon.getStation(), Arrays.asList("3"));
        stationsInfo.put(Station.Hammersmith.getStation(), Arrays.asList("2"));

    }

    public static List<String> convertToList(String zone) {
        return Arrays.asList(StringUtils.split(zone, ","));
    }

    public static String covertToString(List<String> zones) {
        return zones.stream().map(Object::toString).collect(Collectors.joining(","));
    }

    public enum Station {
        Holborn("Holborn"),
        EarlsCourt("Earl's Court"),
        Wimbledon("Wimbledon"),
        Hammersmith("Hammersmith");

        private String station;

        Station(String station) {
            this.station = station;
        }

        public String getStation() {
            return this.station;
        }
    }
}
