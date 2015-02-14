package br.com.caelum.vraptor.forge.addon.commands.plugins.pannetone;

import br.com.caelum.vraptor.forge.addon.commands.VRaptorSetupCommand;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.projects.ui.AbstractProjectCommand;
import org.jboss.forge.addon.resource.DirectoryResource;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

import javax.inject.Inject;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class VRaptorPanettoneSetupCommand extends AbstractProjectCommand {

	@Inject
	private ProjectFactory projectFactory;


	@Override
	public Result execute(UIExecutionContext context) throws Exception {
		String jarPath = "https://oss.sonatype.org/service/local/repositories/releases/content/br/com/caelum/vraptor/vraptor-panettone/4.0.0-RC8/vraptor-panettone-4.0.0-RC8.jar";

		URL url = new URL(jarPath);
		InputStream is = url.openStream();
		byte[] jar = IOUtils.toByteArray(is);

		ResourcesFacet resources = getSelectedProject(context).getFacet(
				ResourcesFacet.class);

		resources.getResourceDirectory()
				.getOrCreateChildDirectory("build");
		FileResource<?> resource = (FileResource<?>) resources
				.getResource("build/vraptor-panettone.jar");
		OutputStream os = resource.getResourceOutputStream();

		IOUtils.write(jar, os);
		return Results
				.success("Command 'VRaptor-panettone-setup: Setup' successfully executed!");
	}

	@Override
	protected boolean isProjectRequired() {
		return true;
	}

	@Override
	protected ProjectFactory getProjectFactory() {
		return projectFactory;
	}

	@Override
	public void initializeUI(UIBuilder uiBuilder) throws Exception {

	}

	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(VRaptorSetupCommand.class)
				.name("VRaptor: panettone")
				.category(Categories.create("VRaptor"));
	}
}
