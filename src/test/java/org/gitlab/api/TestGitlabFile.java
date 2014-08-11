package org.gitlab.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabFile;

public class TestGitlabFile extends TestSuite {
	final String URL = "";
	final String TOKEN = "";

	GitlabAPI connection = null;

	@Before
	public void setupConnection() {
		connection = GitlabAPI.connect(URL, TOKEN);

		assertNotNull(connection);
	}

	@Test
	public void testGitlabFile() {
		GitlabFile file = null;
		try {
			file = new GitlabHTTPRequestor(connection)
					.to("projects/8/repository/files?file_path=src/test/groovy/spocktest/geb/page/GebPage.groovy&ref=master-17.2",
							GitlabFile.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(file);
		assertNotNull(file.getFileName());
		assertNotNull(file.getContent());
		a("Found file: " + file.getFileName() + " with content:\n"
				+ file.getContent());
		a("Decoded content:\n" + file.getContetDecoded());

		try {
			List<String> authors = file.getAuthors();
			assertNotNull(authors);
			a("Author of file " + file.getFileName() + " is "
					+ Arrays.toString(authors.toArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
