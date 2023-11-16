package ufrn.client;

public class ObjectIDs {

	/** Tópico no qual se recebe a informação de presença ou não de pessoas no ambiente. */
	public static final String TOPICO_PRESENCA = "presence";
	
	/** Tópico no qual se recebe a informação sobre o estado do ar-condicionado (ligado ou desligado). */
	public static final String TOPICO_ESTADO = "deviceState";
	
	/** Tópico no qual se recebe a informação sobre a temperatura do ambiente. */
	public static final String TOPICO_TEMPERATURA = "temperature";
	
	/** Tópico no qual a aplicação envia comandos para alteração do estado do ar-condicionado. */
	public static final String COMMAND_STATE = "commandState";
	
	/** Tópico no qual a aplicação envia comandos para alteração da temperatura do ar-condicionado. */
	public static final String COMMAND_TEMPERATURE = "commandTemperature";
	
}
