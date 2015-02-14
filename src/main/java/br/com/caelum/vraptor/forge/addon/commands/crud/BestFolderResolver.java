package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jboss.forge.roaster.model.source.JavaClassSource;

import freemarker.template.TemplateException;

public class BestFolderResolver {

	public static void resolve(JavaClassSource classSource,
			String basePathWithoutEndingSlash, String ftlName,
			TemplateResolverFunction<String> function) throws IOException,
			TemplateException {
		String className = StringUtils.uncapitalize(classSource.getName());
		try {
			function.apply(basePathWithoutEndingSlash + "/" + className + "/"
					+ ftlName);
		} catch (Exception e) {
			function.apply(basePathWithoutEndingSlash + "/" + ftlName);
		}

	}

}
