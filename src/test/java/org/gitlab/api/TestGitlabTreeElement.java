package org.gitlab.api;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.http.GitlabHTTPRequestor;
import org.gitlab.api.models.GitlabTreeElement;
import org.junit.Before;
import org.junit.Test;

public class TestGitlabTreeElement extends TestSuite {
	
	final String URL = "";
	final String TOKEN = "";

	GitlabAPI connection = null;

	@Before
	public void setupConnection() {
		connection = GitlabAPI.connect(URL, TOKEN);

		assertNotNull(connection);
	}

	@Test
	public void testGitlabTree() {		
		GitlabTreeElement[] tree = {};
		try {
			tree = new GitlabHTTPRequestor(connection).to("projects/8/repository/tree", GitlabTreeElement[].class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		assertTrue(tree.length > 0);
	}
}
