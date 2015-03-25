package br.com.caelum.vraptor.forge.addon.form;

public class DefaultInputInfo implements InputInfo{

	private Class<?> type;
	private String inputName;

	public DefaultInputInfo(Class<?> type, String inputName) {
		this.type = type;
		this.inputName = inputName;
	}
	
	public String getTemplateName() {
		return type.getSimpleName();
	}
	
	public String getInputName() {
		return inputName;
	}

}
