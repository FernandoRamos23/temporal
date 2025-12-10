package pe.confianza.preevaluadorapi.util;


public interface Parameter {
	
	String HEADER_CONTENT_TYPE = "Content-Type";
	String HEADER_GX_EMAIL = "Gx_email";
	String HEADER_GX_KEY = "Gx_Key";
	String HEADER_GX_USER = "Gx_usuario";
	String HEADER_CLIENT_ID = "Client_id";
	String HEADER_CLIENT_SECRET = "Client_secret";
	
	String PARAMETER_ID = "id";
	String PARAMETER_ACCOUNTID = "accountId";
	String PARAMETER_TEMPLATEID = "templateId";
	String PARAMETER_CREATEDAT = "createdAt";
	String PARAMETER_STARTEDAT = "startedAt";
	String PARAMETER_TITLE = "title";
	String PARAMETER_SHORTCODE = "shortCode";
	String PARAMETER_METADATA = "metadata";
	String PARAMETER_ASESOREMAIL = "asesoremail";
	String PARAMETER_REFERENCE = "reference";
	String PARAMETER_STATUS = "status";
	String PARAMETER_ORGANIZATIONID = "organizationId";
	String PARAMETER_STATUSCODE = "statusCode";
	String PARAMETER_STATUSCODEEXCEP = "code";
	String PARAMETER_MESSAGE = "message";
	String PARAMETER_ITEMSCOUNT = "itemsCount";
	String PARAMETER_NOTOKEN = "No se pudo obtener token";
	String PARAMETER_ERROR_TYPE = "errorType";
	String PARAMETER_ERROR_MESSAGE = "message";
	
	String PARAMETER_WS_00 = "Todo OK";
	String PARAMETER_WS_01 = "Usuario o contraseña incorrecta";
	String PARAMETER_WS_02 = "Servicio inválido";
	String PARAMETER_WS_03 = "Documento inválido (no existe)";
	String PARAMETER_WS_04 = "No tiene autorización a ver dicho CPT";
	String PARAMETER_WS_06 = "El usuario no tiene permiso de consultar nuevos documentos";
	String PARAMETER_WS_07 = "El servicio está suspendido";
	String PARAMETER_WS_08 = "El usuario está suspendido";
	String PARAMETER_WS_09 = "El usuario está bloqueado";
	String PARAMETER_WS_10 = "El servicio no tiene disponible este producto";
	String PARAMETER_WS_12 = "Usuario no se encuentra en servicio";
	String PARAMETER_WS_14 = "El consultado se encuentra fallecido";
	String PARAMETER_WS_30 = "No cuenta con consultas disponibles";
	String PARAMETER_WS_96 = "Consulta concurrente";
	String PARAMETER_WS_98 = "Error en credenciales";
	String PARAMETER_WS_99 = "Error en funcionamiento del web Service";
	String PARAMETER_WS_XX = "Servicio no disponible s/c";
	String PARAMETER_WS_999 = "999";

	
	String NULL = null;
	String EMPTY = "";
	String SPACE = " ";
	String SLASH = "/";
	String RESULT_OK = "OK";
	
	String COD_ERROR_100 = "100";
	Integer COD_ERROR_400 = 400;
	
	String MSJ_USERNAME = "No se encontró nombre de Usuario";
	String MSJ_PASS = "No se encontró Pass de Usuario";
	String MSJ_TIPDOC = "No se encontró tipo de documento";
	String MSJ_NUMDOC = "No se encontró número de documento";
	String MSJ_API = "No se encontró url Api";
	String MSJ_OAUTH = "No se encontró url Oauth";
	String MSJ_FC = "No se encontró url FC";
	String MSJ_EMAIL = "No se encontró Email";
	String MSJ_KEY = "No se encontró valor Key";
	String MSJ_USER = "No se encontró valor User";
	String MSJ_VALORID = "No se encontró valor ID";
	String MSJ_SECRET = "No se encontró valor Secret";
	
	
	
	String MSJ_APIKEY = "No se encontró apiKey";
	String MSJ_TOKEN = "No se encontró token";
	String MSJ_URLKEYNUA = "No se encontró urlKeynua";
	String MSJ_TITLE = "No se encontró title";
	String MSJ_DESCRIPTION = "No se encontró description";
	String MSJ_LANGUAGE = "No se encontró language";
	String MSJ_TEMPLATEID = "No se encontró templateId";
	String MSJ_EXPIRATIONDATETIME = "No se encontró expirationDatetime";
	String MSJ_DOCUMENTS = "No se encontró documents";
	String MSJ_DOCUMENT_NAME = "No se encontró name document";
	String MSJ_DOCUMENT_BASE64 = "No se encontró base64 document";
	String MSJ_USERS = "No se encontró users";
	String MSJ_USER_NAME = "No se encontró name user";
	String MSJ_USER_GROUPS = "No se encontró groups user";
	String MSJ_USER_PHONE = "No se encontró phone user";
	String MSJ_USER_PREFILLEDITEMS = "No se encontró prefilledItems user";
	String MSJ_USER_PREFILLEDITEM_TARGET = "No se encontró target prefilledItem user";
	String MSJ_USER_PREFILLEDITEM_VALUE = "No se encontró value prefilledItem user";
	String MSJ_USER_PREFILLEDITEM_VALUE_TEXT = "No se encontró text value prefilledItem user";
	String MSJ_FLAGS = "No se encontró flags";
	String MSJ_FLAG_CHOSENNOTIFICATIONOPTIONS = "No se encontró chosenNotificationOptions flag";
	String MSJ_FLAG_REMINDERSDATA = "No se encontró remindersData flag";
	String MSJ_METADATA = "No se encontró metadata";
	String MSJ_REFERENCE = "No se encontró reference";
	String MSJ_ID = "No se encontró id";
	String MSJ_CODEV = "No se encontró código a verificar con Proveedor";
	String MSJ_DN = "No se encontró número de documento titular";
	String MSJ_UP = "No se encontró número de teléfono titular";
	String MSJ_FN = "No se encontró nombres de titular";
	String MSJ_TITLEB = "No se encontró título biometria";
	String MSJ_TYPE = "No se encontró tipo de biometria";
	String MSJ_DT = "No se encontró tipo de documento titular";
	String MSJ_VD = "No se encontró validate documento titular";
	String MSJ_TSC = "No se encontró número de teléfono envío código secreto";
	
	
	int ZERO = 0;

}
