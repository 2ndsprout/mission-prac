package com.example.ms1.note;

import lombok.Getter;
import lombok.Setter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class ParamHandler {

    private Boolean isSearchModal;

    private String keyword;

    private String sort;

    public ParamHandler () {
        this.isSearchModal = false;
        this.keyword = "";
        this.sort = "date";
    }
    public String getQueryParam() {
        return String.format("?keyword=%s&sort=%s", URLEncoder.encode(keyword, StandardCharsets.UTF_8),sort);
    }

    public String getRedirectUrl (String url) {
        return "redirect:" + url + getQueryParam();
    }
}
