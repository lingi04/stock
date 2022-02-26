package com.jh.stock.batch.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class CrawlingService {
    public Document crawlingData(String url, String ticker) throws IOException {
        String fullUrl = String.format(url, ticker);

        return Jsoup.connect(fullUrl).get();
    }
}
