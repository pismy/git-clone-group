package com.orange.oswe.tools.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GitLabResourceUrlTest {
    @Test
    public void parse_valid_url_should_succeed() {
        GitLabResourceUrl parsed = GitLabResourceUrl.parse("https://gitlab.forge.orange-labs.fr/ubicode/gitlab-voting");
        assertThat(parsed.getGitlabUrl()).isEqualTo("https://gitlab.forge.orange-labs.fr");
        assertThat(parsed.getPath()).isEqualTo("ubicode/gitlab-voting");
    }

    @Test
    public void parse_valid_url2_should_succeed() {
        GitLabResourceUrl parsed = GitLabResourceUrl.parse("https://gitlab.forge.orange-labs.fr/ubicode/gitlab-voting/");
        assertThat(parsed.getGitlabUrl()).isEqualTo("https://gitlab.forge.orange-labs.fr");
        assertThat(parsed.getPath()).isEqualTo("ubicode/gitlab-voting");
    }
}
