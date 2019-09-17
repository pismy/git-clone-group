package com.orange.oswe.tools.domain;

import lombok.Builder;
import lombok.Value;

import javax.ws.rs.core.UriBuilder;

@Value
@Builder
public class GitLabResourceUrl {
    String gitlabUrl;
    String path;

    public static GitLabResourceUrl parse(String resourcesUrl) {
        return GitLabResourceUrl.builder()
                .gitlabUrl(UriBuilder.fromUri(resourcesUrl).replacePath(null).build().toString())
                .path(norm(UriBuilder.fromUri(resourcesUrl).build().getPath()))
                .build();
    }

    private static String norm(String path) {
        int start = 0;
        if (path.startsWith("/")) {
            start = 1;
        }
        int end = path.length();
        if (path.endsWith("/")) {
            end = path.length() - 1;
        }
        return path.substring(start, end);
    }
}
