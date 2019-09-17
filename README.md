# Git Clone Group

Command-line tool to clone at once a complete GitLab group with projects and sub-groups (recurses).

## Usage

Clone the repository, install Maven, Java, and run:

```bash
mvn clean package
```

Then start the tool by running:

```bash
java -jar target/git-clone-group-1.0.0-SNAPSHOT.jar
```

Once in the tool shell, you can use the following:

```bash
# global help
shell:>help

# help on stats command
shell:>help clone_group 

NAME
	clone_group - Clones locally a GitLab/GitHub group

SYNOPSYS
	clone_group [-D] file  [-U] string  [-T] string  [[-P] protocol]  

OPTIONS
	-D or --dest  file
		Destination directory
		[Mandatory]

	-U or --url  string
		Group url
		[Mandatory]

	-T or --token  string
		API token
		[Mandatory]

	-P or --protocol  protocol
		Protocol to use ('https' or 'ssh')
		[Optional, default = https]

# exit shell
exit
```

Before running the program, obtain a [GitLab API token](https://docs.gitlab.com/ee/user/profile/personal_access_tokens.html).

Finally the program can be run as:

```bash
shell:> clone_group -D /tmp -U https://gitlab.com/my-group -T myApiToken -P ssh
```

## License

This code is under [Apache-2.0 License](LICENSE.txt)
