package com.orange.oswe.tools;

import com.orange.oswe.tools.domain.GitLabResourceUrl;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Group;
import org.gitlab4j.api.models.Project;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.Executors;

@SpringBootApplication
public class GitCloneGroup {

    public static void main(String[] args) {
        SpringApplication.run(GitCloneGroup.class, args);
    }

    @ShellComponent
    public static class Commands {

        private static final DecimalFormat PERCENT = new DecimalFormat("#.##");

        public enum Protocol {https, ssh}

        @ShellMethod("Clones locally a GitLab/GitHub group")
        public void clone_group(
                @ShellOption(value = {"-D", "--dest"}, help = "Destination directory") File dest,
                @ShellOption(value = {"-U", "--url"}, help = "Group url") String groupUrl,
                @ShellOption(value = {"-T", "--token"}, help = "API token") String apiToken,
                @ShellOption(value = {"-P", "--protocol"}, help = "Protocol to use  ('https' or 'ssh')", defaultValue = "https") Protocol protocol

        ) throws GitLabApiException, IOException, InterruptedException {
            GitLabResourceUrl gitLabResourceUrl = GitLabResourceUrl.parse(groupUrl);
            GitLabApi api = new GitLabApi(gitLabResourceUrl.getGitlabUrl(), apiToken);

            Group group = api.getGroupApi().getGroup(gitLabResourceUrl.getPath());
            clone(api, group, new File(dest, group.getPath()), protocol);
            System.out.println("success");
        }

        private void clone(GitLabApi api, Group group, File dest, Protocol protocol) throws GitLabApiException, IOException, InterruptedException {
            System.out.println("cloning group '" + group.getFullPath() + "' into " + dest + "...");

            // create destination directory
            dest.mkdirs();

            // clone each project
            for (Project project : group.getProjects()) {
                File target = new File(dest, project.getPath());
                String projUrl = protocol == Protocol.https ? project.getHttpUrlToRepo() : project.getSshUrlToRepo();
                System.out.println("cloning project '" + project.getPathWithNamespace() + "' (" + projUrl + ") into " + target + "...");
                exec("git", "clone", projUrl, target.getAbsolutePath());
            }

            // recurse on each subgroup
            List<Group> subGroups = api.getGroupApi().getSubGroups(group.getId());
            for (Group sub : subGroups) {
                clone(api, api.getGroupApi().getGroup(sub.getId()), new File(dest, sub.getPath()), protocol);
            }
        }

        private void exec(String... args) throws IOException, InterruptedException {
            Process process = Runtime.getRuntime().exec(args);
            StreamGobbler stdoutGobbler = new StreamGobbler(process.getInputStream(), System.out::println);
            StreamGobbler stderrGobbler = new StreamGobbler(process.getErrorStream(), System.err::println);
            Executors.newSingleThreadExecutor().submit(stdoutGobbler);
            Executors.newSingleThreadExecutor().submit(stderrGobbler);
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Command failed");
                throw new RuntimeException("Command failed with code " + exitCode);
            }
        }
    }
}

