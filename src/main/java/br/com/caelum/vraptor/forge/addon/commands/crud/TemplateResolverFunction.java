package br.com.caelum.vraptor.forge.addon.commands.crud;

import java.io.IOException;

import freemarker.template.TemplateException;


public interface TemplateResolverFunction<I> {
	  void apply(I input) throws IOException,TemplateException;
}
