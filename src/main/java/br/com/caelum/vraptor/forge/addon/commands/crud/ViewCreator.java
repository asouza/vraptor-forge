package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.IOException;
import java.util.Map;

import javax.enterprise.inject.Vetoed;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.addon.projects.facets.WebResourcesFacet;
import org.jboss.forge.addon.resource.FileResource;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import br.com.caelum.vraptor.forge.addon.form.templates.FormTemplate;
import freemarker.template.TemplateException;

@Vetoed
public class ViewCreator {

	private FormTemplate formTemplate;
	private WebResourcesFacet webFacet;

	public ViewCreator(FormTemplate formTemplate, WebResourcesFacet webFacet) {
		this.formTemplate = formTemplate;
		this.webFacet = webFacet;
	}

	public void create(String templatePath, JavaClassSource classSource,
			Map<Object, Object> params) throws IOException, TemplateException {
		String jsp = formTemplate.process(templatePath, params);
		
		String uncapitalizeClassName = StringUtils.uncapitalize(classSource
				.getName());
		
		String fileTemplateName = FilenameUtils.getBaseName(templatePath);
		FileResource<?> webResource = webFacet.getWebResource("WEB-INF/jsp/"
				+ uncapitalizeClassName + "/" + fileTemplateName + ".jsp");
		
		webResource.setContents(jsp).createNewFile();
	}

}
