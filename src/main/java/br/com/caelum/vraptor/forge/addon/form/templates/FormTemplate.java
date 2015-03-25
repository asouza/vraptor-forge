package br.com.caelum.vraptor.forge.addon.form.templates;

import java.io.IOException;
import java.util.Map;

import freemarker.template.TemplateException;

public interface FormTemplate {

	String process(String templatePath, Map<?, ?> params) throws IOException,
			TemplateException;
	
	boolean exists(String templatePath);

}
