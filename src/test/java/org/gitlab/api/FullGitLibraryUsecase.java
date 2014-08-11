package org.gitlab.api;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.gitlab.api.GitlabAPI;
import org.gitlab.api.models.GitlabFile;
import org.gitlab.api.models.GitlabProject;
import org.gitlab.api.models.GitlabTreeElement;

public class FullGitLibraryUsecase extends TestSuite{
	final String URL = "";
	final String PROJECT = "";
	final String TOKEN = "";
	final String BRANCH = "";

	
	GitlabAPI connection = null;
	
	@Before 
	public void setupConnection(){
		connection = GitlabAPI.connect(
				URL, TOKEN);

		assertNotNull(connection);
	}

	@Test
	public  void testWalkthrough() throws IOException {
		final String GEB_PAGES_PATH = "";

		int projectId = -1;
		for (GitlabProject project : connection.getProjects()) {
			if (project.getName().equals(PROJECT)) {
				projectId = project.getId();
			}
		}

		GitlabProject mainProject = null;
		if (projectId > -1) {
			mainProject = connection.getProject(projectId);
		}
		assertNotNull(mainProject);

		List<GitlabTreeElement> tree = null;
		List<GitlabTreeElement> pagesTree = null;
		if (mainProject != null) {
			tree = mainProject.getRepositoryTree();
			pagesTree = mainProject
					.getRepositoryTree(GEB_PAGES_PATH, BRANCH);
		}
		assertNotNull(tree);
		assertNotNull(pagesTree);
		
		if (tree != null) {
			for (GitlabTreeElement treeElement : tree) {
				a(treeElement.getName());
			}
		}
		if (pagesTree != null) {			
			//try to get subfolders of air
			List<GitlabTreeElement> airPagesTree = null;
			assertTrue(pagesTree.get(0).isDirectory());
			if(pagesTree.get(0).isDirectory()){
				airPagesTree = pagesTree.get(0).listFiles();
				assertEquals(2, airPagesTree.size());
			}
			
			assertTrue(pagesTree.get(6).isFile());
			if(pagesTree.get(6).isFile()){
				a(pagesTree.get(6).getName() + " is a file.");
				
				GitlabFile file = pagesTree.get(6).getGitlabFile();
				assertNotNull(file);
				if(file != null){
					a(pagesTree.get(6).getName() + " was retreived.");
					List<String> authors = file.getAuthors();
					assertNotNull(authors);
					assertFalse(authors.isEmpty());
					a("Authors of file " +file.getFileName()+ " are "  + Arrays.toString(authors.toArray()));
				}
			}
		}

	}
}
