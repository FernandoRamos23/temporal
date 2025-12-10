package pe.confianza.preevaluadorapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.ClientResponse.Status;


import pe.confianza.preevaluadorapi.util.Parameter;
import pe.confianza.preevaluadorapi.util.Util;
import pe.confianza.preevaluadorapi.bean.ApiResponse;
import pe.confianza.preevaluadorapi.bean.InfBas;
import pe.confianza.preevaluadorapi.bean.Principal;
import pe.confianza.preevaluadorapi.bean.Respuesta;
import pe.confianza.preevaluadorapi.bean.ApiBody;
import pe.confianza.preevaluadorapi.bean.ApiBodyAutoriza;
import pe.confianza.preevaluadorapi.bean.ApiRequest;
import pe.confianza.preevaluadorapi.service.PreEvaluadorService;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.core.HttpHeaders;


@Tag(name = "Pre Evaluador", description = "API Pre Eval")
@RestController
@RequestMapping("/apipreevaluador")
@CrossOrigin(origins = "*", methods = { RequestMethod.POST })
public class PreEvaluadorController {

    private static Logger logger = LoggerFactory.getLogger(PreEvaluadorController.class);

    public PreEvaluadorController(@Autowired PreEvaluadorService preEvaluadorService) {
        super();
    }
    
    @Operation(summary = "Pre  Evaluador Api - Historial")
	@PostMapping(value = "/historial", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> consultarhistorial(@RequestBody ApiRequest detalleBody) {
		return consultahistoria(detalleBody);
	}
    
	private ResponseEntity<?> consultahistoria(ApiRequest apiRequest) {
        logger.info("[Inicio] - En proceso PreEval Controller ...");
        //logger.info("consulta Preval => {}", new Gson().toJson(apiRequest).toString());
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        //Capturamos los datos de fecha y hora de inicio del proceso
        DateTimeFormatter dth = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strFecIniServer = dtf.format(LocalDateTime.now(ZoneId.of("America/Lima")));
        String strHorIniServer = dth.format(LocalDateTime.now(ZoneId.of("America/Lima")));
        //Componentes de respuesta
		ApiResponse apiResponse = new ApiResponse();
		Principal principal = new Principal();
		//Agrega datos de inicio a componente de respuesta
		principal.setFechaInicio(strFecIniServer);
		principal.setHoraInicio(strHorIniServer);
		//Valida datos de entrada
		String msj = validaDetalleBody(apiRequest);
		//Respuesta si falta algún dato de entrada
		if(!Parameter.EMPTY.equals(msj)) {
			apiResponse = getApiResponse(Parameter.COD_ERROR_100, msj);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		}
		//Trama input de servicio historial
		ApiBody apiBody = new ApiBody();
		apiBody.setTipDoc(apiRequest.getTipDoc());
		apiBody.setNroDoc(apiRequest.getNroDoc());
		//Obtiene token de consulta historial
		String token = consultaAutorizador(apiRequest);
		//Continúa si tiene token
		if (!token.isEmpty()) {
			logger.info("Token autorizador : OK");
			String result = "";
			try {
				WebResource webResourceDL = Client.create().resource(apiRequest.getUrlApi());
			    ClientResponse clientResponseDL = webResourceDL.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
						.type(javax.ws.rs.core.MediaType.APPLICATION_JSON)
						.header(Parameter.HEADER_CONTENT_TYPE, "application/json")
						.header(Parameter.HEADER_GX_EMAIL, apiRequest.getEmail())
						.header(Parameter.HEADER_GX_KEY, apiRequest.getKey())
						.header(Parameter.HEADER_GX_USER, apiRequest.getUser())
						.header(Parameter.HEADER_CLIENT_ID, apiRequest.getId())
						.header(Parameter.HEADER_CLIENT_SECRET, apiRequest.getSecret())
						.header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
						.post(ClientResponse.class, new Gson().toJson(apiBody).toString());
	
				System.out.print(clientResponseDL.getClientResponseStatus());
				System.out.print(clientResponseDL.getStatus());
				
	 	  //comentario
				logger.info("ResponseStatus historial: {}",clientResponseDL.getClientResponseStatus());
				logger.info("Status historial: {}", clientResponseDL.getStatus());
				
				if (clientResponseDL.getClientResponseStatus() == Status.OK) {
					logger.info("Status historial: OK");
					result = clientResponseDL.getEntity(String.class);// code, message
					logger.info("Result" + result);
					
					
					JsonElement parsed = new JsonParser().parse(result);
					JsonObject asJsonObject = parsed.getAsJsonObject();
					Integer codigoWS = asJsonObject.get("CodigoWS").getAsInt();
					principal.setCodigoWS(codigoWS.toString());
					if ( codigoWS == 0) {
						principal.setCodigoWS(codigoWS.toString());
						principal.setMensajeWS(Parameter.PARAMETER_WS_00);
						
						JsonObject asJsonObjrespuesta = asJsonObject.get("respuesta").getAsJsonObject();
						JsonObject asJsonObjinfBas = asJsonObjrespuesta.get("InfBas").getAsJsonObject();
						
						//Información básica
						InfBas infBas = new InfBas();
						Respuesta respuesta = new Respuesta();
						
						infBas.setTDoc(asJsonObjinfBas.get("TDoc").getAsString());
						infBas.setNDoc(asJsonObjinfBas.get("NDoc").getAsString());
						infBas.setApePat(asJsonObjinfBas.get("ApePat").getAsString());
						infBas.setApeMat(asJsonObjinfBas.get("ApeMat").getAsString());
						infBas.setNom(asJsonObjinfBas.get("Nom").getAsString());
						infBas.setRazSoc(asJsonObjinfBas.get("RazSoc").getAsString());
						infBas.setNomCom(asJsonObjinfBas.get("NomCom").getAsString());
						infBas.setTipCon(asJsonObjinfBas.get("TipCon").getAsString());
						infBas.setIniAct(asJsonObjinfBas.get("IniAct").getAsString());
						infBas.setCIIU(asJsonObjinfBas.get("CIIU").getAsString());
						infBas.setActEco(asJsonObjinfBas.get("ActEco").getAsString());
						infBas.setFchInsRRPP(asJsonObjinfBas.get("FchInsRRPP").getAsString());
						infBas.setNumParReg(asJsonObjinfBas.get("NumParReg").getAsString());
						infBas.setFol(asJsonObjinfBas.get("Fol").getAsString());
						infBas.setAsi(asJsonObjinfBas.get("Asi").getAsString());
						infBas.setAgeRet(asJsonObjinfBas.get("AgeRet").getAsString());
						infBas.setEstConC(asJsonObjinfBas.get("EstConC").getAsString());
						infBas.setEstCon(asJsonObjinfBas.get("EstCon").getAsString());
						infBas.setEstDomC(asJsonObjinfBas.get("EstDomC").getAsString());
						infBas.setEstDom(asJsonObjinfBas.get("EstDom").getAsString());
						infBas.setRelTDoc(asJsonObjinfBas.get("RelTDoc").getAsString());
						infBas.setRelNDoc(asJsonObjinfBas.get("RelNDoc").getAsString());
						
						respuesta.setInfBas(infBas);
						
						apiResponse.setRespuesta(respuesta);
						
			            principal.setCodigo(String.valueOf(Status.OK.getStatusCode()));
			            principal.setMensaje(Parameter.RESULT_OK);
					}else {
						principal.setCodigoWS(codigoWS.toString());
						switch (codigoWS) {
					    case 1:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_01);
					        break;
					    case 2:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_02);
					        break;
					    case 3:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_03);
					        break;
					    case 4:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_04);
					        break;
					    case 6:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_06);
					        break;
					    case 7:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_07);
					        break;
					    case 8:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_08);
					        break;
					    case 9:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_09);
					        break;
					    case 10:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_10);
					        break;
					    case 12:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_12);
					        break;
					    case 14:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_14);
					        break;
					    case 30:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_30);
					        break;
					    case 96:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_96);
					        break;
					    case 98:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_98);
					        break;
					    case 99:
				            principal.setMensajeWS(Parameter.PARAMETER_WS_99);
					        break;
					    default:
					    	principal.setCodigoWS(Parameter.PARAMETER_WS_999);
				            principal.setMensajeWS(Parameter.PARAMETER_WS_XX);
						}
					}
					

					
					
					/*
					apiResponse.setCodError(String.valueOf(Status.OK.getStatusCode()));
					apiResponse.setMsjError(Parameter.RESULT_OK);
					
					apiResponse.setId(asJsonObject.get(Parameter.PARAMETER_ID).getAsString());
					apiResponse.setAccountId(asJsonObject.get(Parameter.PARAMETER_ACCOUNTID).getAsString());
					apiResponse.setTemplateId(asJsonObject.get(Parameter.PARAMETER_TEMPLATEID).getAsString());
					apiResponse.setCreatedAt(asJsonObject.get(Parameter.PARAMETER_CREATEDAT).getAsString());
					apiResponse.setStartedAt(asJsonObject.get(Parameter.PARAMETER_STARTEDAT).getAsString());
					apiResponse.setTitle(asJsonObject.get(Parameter.PARAMETER_TITLE).getAsString());
					apiResponse.setShortCode(asJsonObject.get(Parameter.PARAMETER_SHORTCODE).getAsString());
					*/
					
	/*
					apiResponse.setDescription(asJsonObject.get("description").getAsString());
					apiResponse.setFinishedAt(asJsonObject.get("finishedAt").getAsString());
					apiResponse.setDeletedAt(asJsonObject.get("deletedAt").getAsString());
					apiResponse.setLanguage(asJsonObject.get("language").getAsString());
	*/				
					
					/*
					JsonObject asJsonObjMetadata = asJsonObject.get(Parameter.PARAMETER_METADATA).getAsJsonObject();
					Metadata metadata = new Metadata();
					if(!asJsonObjMetadata.get(Parameter.PARAMETER_ASESOREMAIL).isJsonNull()) {
						metadata.setAsesoremail(asJsonObjMetadata.get(Parameter.PARAMETER_ASESOREMAIL).getAsString());
					}else {
						metadata.setAsesoremail(Parameter.EMPTY);
					}
					apiResponse.setMetadata(metadata);
					
					if(!asJsonObject.get(Parameter.PARAMETER_REFERENCE).isJsonNull()) {
						apiResponse.setReference(asJsonObject.get(Parameter.PARAMETER_REFERENCE).getAsString());
					}else {
						apiResponse.setReference(Parameter.EMPTY);
					}
					*/
					
					
	/*				
					apiResponse.setExpirationInHours(asJsonObject.get("expirationInHours").getAsInt());
					apiResponse.setExpired(asJsonObject.get("expired").getAsBoolean());
					apiResponse.setItemsCount(asJsonObject.get("itemsCount").getAsInt());
					
					JsonArray jsonArrayGroups = asJsonObject.get("groups").getAsJsonArray();
					List<Group> groupList = new ArrayList<Group>();
					for (int i = 0; i < jsonArrayGroups.size(); i++) {
						JsonObject jsonObjectG = jsonArrayGroups.get(i).getAsJsonObject();
						Group group = new Group();
						group.setId(jsonObjectG.get("id").getAsString());
						group.setName(jsonObjectG.get("name").getAsString());
						group.setDescription(jsonObjectG.get("description").getAsString());
						group.setIcon(jsonObjectG.get("icon").getAsString());
						group.setDigitalSignature(jsonObjectG.get("digitalSignature").getAsBoolean());
						group.setBulk(jsonObjectG.get("bulk").getAsBoolean());
						group.setApprover(jsonObjectG.get("isApprover").getAsBoolean());
						groupList.add(group);
					}
					apiResponse.setGroups(groupList);
					
					JsonArray jsonArrayUsers = asJsonObject.get("users").getAsJsonArray();
					List<User2> userList = new ArrayList<User2>();
					for (int i = 0; i < jsonArrayUsers.size(); i++) {
						JsonObject jsonObjectU = jsonArrayUsers.get(i).getAsJsonObject();
						User2 user = new User2();
						user.setId(jsonObjectU.get("id").getAsInt());
						user.setRef(jsonObjectU.get("ref").getAsString());
						user.setName(jsonObjectU.get("name").getAsString());
						user.setEmail(jsonObjectU.get("email").getAsString());
						user.setPhone(jsonObjectU.get("phone").getAsString());
						user.setToken(jsonObjectU.get("token").getAsString());
						
						JsonArray jsonArrayGU = jsonObjectU.get("groups").getAsJsonArray();
						List<String> groupUserList = new ArrayList<String>();
						for (int j=0;j<jsonArrayGU.size();j++){ 
							groupUserList.add(jsonArrayGU.get(j).getAsString());
						} 
						user.setGroups(groupUserList);
						userList.add(user);
					}
					apiResponse.setUsers(userList);
					
					JsonArray jsonArrayDocuments = asJsonObject.get("documents").getAsJsonArray();
					List<Documento2> documentoList = new ArrayList<Documento2>();
					for (int i = 0; i < jsonArrayDocuments.size(); i++) {
						JsonObject jsonObjectD = jsonArrayDocuments.get(i).getAsJsonObject();
						Documento2 document = new Documento2();
						document.setId(jsonObjectD.get("id").getAsInt());
						document.setName(jsonObjectD.get("name").getAsString());
						document.setExt(jsonObjectD.get("ext").getAsString());
						document.setSha(jsonObjectD.get("sha").getAsString());
						document.setSize(jsonObjectD.get("size").getAsInt());
						document.setType(jsonObjectD.get("type").getAsString());
						document.setUrl(jsonObjectD.get("url").getAsString());
						documentoList.add(document);
					}
					apiResponse.setDocuments(documentoList);
					
					JsonArray jsonArrayItems = asJsonObject.get("items").getAsJsonArray();
					List<Item> itemList = new ArrayList<Item>();
					for (int i = 0; i < jsonArrayItems.size(); i++) {
						JsonObject jsonObjectI = jsonArrayItems.get(i).getAsJsonObject();
						Item item = new Item();
						item.setId(jsonObjectI.get("id").getAsInt());
						item.setVersion(jsonObjectI.get("version").getAsInt());
						item.setState(jsonObjectI.get("state").getAsString());
						item.setUserId(jsonObjectI.get("userId").getAsInt());
						item.setReference(jsonObjectI.get("reference").getAsString());
						item.setTitle(jsonObjectI.get("title").getAsString());
						item.setType(jsonObjectI.get("type").getAsString());
						item.setStageIndex(jsonObjectI.get("stageIndex").getAsInt());
						item.setAllowsManualUpdate(jsonObjectI.get("allowsManualUpdate").getAsBoolean());
						item.setAllowsRetry(jsonObjectI.get("allowsRetry").getAsBoolean());
						item.setHideOnWebApp(jsonObjectI.get("hideOnWebApp").getAsBoolean());
						itemList.add(item);
					}
					apiResponse.setItems(itemList);
	*/
					
					/*
					apiResponse.setStatus(asJsonObject.get(Parameter.PARAMETER_STATUS).getAsString());
					apiResponse.setOrganizationId(asJsonObject.get(Parameter.PARAMETER_ORGANIZATIONID).getAsString());
					*/
		            //capturamos la fecha y hora de fin del proceso
		            String strFecFinServer = dtf.format(LocalDateTime.now(ZoneId.of("America/Lima")));
		            String strHorFinServer = dth.format(LocalDateTime.now(ZoneId.of("America/Lima")));
		            
		            principal.setFechaFin(strFecFinServer);
		            principal.setHoraFin(strHorFinServer);
		            principal.setCodigo(String.valueOf(Status.OK.getStatusCode()));
		            principal.setMensaje(Parameter.RESULT_OK);
		            apiResponse.setPrincipal(principal);
					
					return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
					
				} else {
					logger.info("Status historial: noOK");
					result = clientResponseDL.getEntity(String.class);// code, message
					logger.info("Result historial: " + result);

					JsonElement parsed = new JsonParser().parse(result);
					JsonObject asJsonObject = parsed.getAsJsonObject();
					
					principal.setCodigo(asJsonObject.get(Parameter.PARAMETER_ERROR_TYPE).getAsString());
					principal.setMensaje(asJsonObject.get(Parameter.PARAMETER_ERROR_MESSAGE).getAsString());
					apiResponse.setPrincipal(principal);
					return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
				}
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("Exception historial: " + e.getMessage());
				e.printStackTrace();
				
				JsonElement parsed = new JsonParser().parse(result);
				JsonObject asJsonObject = parsed.getAsJsonObject();
				
				principal.setCodigo(asJsonObject.get(Parameter.PARAMETER_ERROR_TYPE).getAsString());
				principal.setMensaje(asJsonObject.get(Parameter.PARAMETER_ERROR_MESSAGE).getAsString());
				apiResponse.setPrincipal(principal);
				return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
			}
		}
		else {
			logger.error("No se pudo obtener token autorizador...");
			principal.setCodigo(Parameter.COD_ERROR_400.toString());
			principal.setMensaje(Parameter.PARAMETER_NOTOKEN);
			apiResponse.setPrincipal(principal);
			return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.OK);
		}

	}
	
	private String validaDetalleBody(ApiRequest apiRequest) {
		
		logger.info("isValidContractInput => {}", new Gson().toJson(apiRequest.toString()));
		String msj = Parameter.EMPTY;
		
		if(Util.getDefaultString(apiRequest.getUsername()) == null) {
			return msj = Parameter.MSJ_USERNAME;
		}
		if(Util.getDefaultString(apiRequest.getPassword()) == null) {
			return msj = Parameter.MSJ_PASS;
		}
		if(Util.getDefaultString(apiRequest.getTipDoc()) == null) {
			return msj = Parameter.MSJ_TIPDOC;
		}
		if(Util.getDefaultString(apiRequest.getNroDoc()) == null) {
			return msj = Parameter.MSJ_NUMDOC;
		}
		if(Util.getDefaultString(apiRequest.getUrlApi()) == null) {
			return msj = Parameter.MSJ_API;
		}
		if(Util.getDefaultString(apiRequest.getUrlOauth()) == null) {
			return msj = Parameter.MSJ_OAUTH;
		}
		if(Util.getDefaultString(apiRequest.getEmail()) == null) {
			return msj = Parameter.MSJ_EMAIL;
		}
		if(Util.getDefaultString(apiRequest.getKey()) == null) {
			return msj = Parameter.MSJ_KEY;
		}
		if(Util.getDefaultString(apiRequest.getUser()) == null) {
			return msj = Parameter.MSJ_USER;
		}
		if(Util.getDefaultString(apiRequest.getId()) == null) {
			return msj = Parameter.MSJ_VALORID;
		}
		if(Util.getDefaultString(apiRequest.getSecret()) == null) {
			return msj = Parameter.MSJ_SECRET;
		}
		if(Util.getDefaultString(apiRequest.getUrlOauthFc()) == null) {
			return msj = Parameter.MSJ_FC;
		}
		
		return msj;
	}
	
	private String consultaAutorizador(ApiRequest apiRequest) {
		
		logger.info("consultaAutorizador => {}", new Gson().toJson(apiRequest.toString()));
		String token = Parameter.EMPTY;
		//Objeto de respuesta
		ApiResponse apiResponse = new ApiResponse();
		//Trama INPUT de servicio autorizador
		ApiBodyAutoriza apiBodyAutoriza = new ApiBodyAutoriza();
		apiBodyAutoriza.setUsername(apiRequest.getUsername());
		apiBodyAutoriza.setPassword(apiRequest.getPassword());
		apiBodyAutoriza.setUrlApiOauth(apiRequest.getUrlOauth());
		apiBodyAutoriza.setClientID(apiRequest.getId());
		apiBodyAutoriza.setClientSecred(apiRequest.getSecret());
		
		//LLamada servicio autorizador
		String result = "";
		try {
			WebResource webResourceDL = Client.create().resource(apiRequest.getUrlOauthFc());
		    ClientResponse clientResponseDL = webResourceDL.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON)
					.header(Parameter.HEADER_CONTENT_TYPE, "application/json")
					.post(ClientResponse.class, new Gson().toJson(apiBodyAutoriza).toString());
	
			logger.info("ResponseStatus AUTORIZADOR: {}",clientResponseDL.getClientResponseStatus());
			logger.info("Status AUTORIZADOR: {}", clientResponseDL.getStatus());
			
			if (clientResponseDL.getClientResponseStatus() == Status.OK) {
				logger.info("Status AUTORIZADOR: OK");
				result = clientResponseDL.getEntity(String.class);// code, message
				//logger.info("Result AUTORIZADOR" + result);
				//Respuesta to JSON
				JsonElement parsed = new JsonParser().parse(result);
				JsonObject asJsonObject = parsed.getAsJsonObject();
				if(asJsonObject.get("access_token").getAsString() == null) {
					String codigo =	asJsonObject.get("codigo").getAsString();
					String mensaje =	asJsonObject.get("mensaje").getAsString();
					logger.error("Exception, código autorizador : " + codigo);
					logger.error("Exception, mensaje autorizador : " + mensaje);
				}else {
					token =	asJsonObject.get("access_token").getAsString();
				}
				//Asigna respuesta
				//apiResponse.setCodigo(String.valueOf(Status.OK.getStatusCode()));
				//apiResponse.setMensaje(Parameter.RESULT_OK);
				logger.info("Obtiene token de autorizador");
				return token;
				
			} else {
				logger.info("Status AUTORIZADOR: noOK");
				result = clientResponseDL.getEntity(String.class);// code, message
				logger.info("Result" + result);

				JsonElement parsed = new JsonParser().parse(result);
				JsonObject asJsonObject = parsed.getAsJsonObject();
				
				//apiResponse.setCodigo(asJsonObject.get(Parameter.PARAMETER_STATUSCODEEXCEP).getAsString());
				//apiResponse.setMensaje(asJsonObject.get(Parameter.PARAMETER_MESSAGE).getAsString());
				
				return token;
			}
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Exception Autorizador: " + e.getMessage());
			logger.error("Exception response: " + result);
			e.printStackTrace();
			//apiResponse.setCodigo(String.valueOf(Status.BAD_REQUEST.getStatusCode()));
			//apiResponse.setMensaje(apiRequest.getUrlApi() + " " + e.getMessage());

			return token;
		}
				
	
	}
	
	
	private ApiResponse getApiResponse(String codError, String msjError) {
		ApiResponse apiResponse = new ApiResponse();
		Principal principal = new Principal();
		principal.setCodigo(codError);
		principal.setMensaje(msjError);
		apiResponse.setPrincipal(principal);
		return apiResponse;
	}
	
	
	
	
}