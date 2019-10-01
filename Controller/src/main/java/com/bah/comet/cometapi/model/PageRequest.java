package com.bah.comet.cometapi.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Pseudo-override the page request object to inject our default config page size
 * and validate the maximum number of items requested.
 */
public final class PageRequest {

    private static ApiSettings apiSettings;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void setApiSettings(ApiSettings apiSettings) {
        PageRequest.apiSettings = apiSettings;
    }

    public static org.springframework.data.domain.PageRequest create(Optional<Integer> page, Optional<Integer> size, Optional<Sort> sort) {
        return org.springframework.data.domain.PageRequest.of(
                page.orElse(0),
                size.filter(s -> s <= apiSettings.getMaxPaginationSize()).orElse(apiSettings.getDefaultPaginationSize()),
                sort.orElse(Sort.unsorted()));
    }

    public static org.springframework.data.domain.PageRequest create(Optional<Integer> page, Optional<Integer> size) {
        return org.springframework.data.domain.PageRequest.of(
                page.orElse(0),
                size.filter(s -> s <= apiSettings.getMaxPaginationSize()).orElse(apiSettings.getDefaultPaginationSize()),
                Sort.unsorted());
    }
}
