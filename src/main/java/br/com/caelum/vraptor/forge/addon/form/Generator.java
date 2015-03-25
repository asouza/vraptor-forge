package br.com.caelum.vraptor.forge.addon.form;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import br.com.caelum.vraptor.forge.addon.form.templates.FormTemplate;
import freemarker.template.TemplateException;

public class Generator {

	private FormTemplate template;

	public Generator(FormTemplate template) {
		super();
		this.template = template;
	}

	public String generate(InputInfo inputInfo,Map<Object, Object> params,String basePath) {
		params.put("inputInfo", inputInfo);
		try {
			String output = template.process(basePath+"/"+StringUtils.uncapitalize(inputInfo.getTemplateName()+".ftl"),params);
			return output;
		} catch (IOException | TemplateException e) {
			throw new RuntimeException(e);
		}
	}


}
