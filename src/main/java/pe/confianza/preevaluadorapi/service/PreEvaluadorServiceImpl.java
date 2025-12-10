package pe.confianza.preevaluadorapi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class PreEvaluadorServiceImpl implements PreEvaluadorService{
	
	private static final Logger logger = LoggerFactory.getLogger(PreEvaluadorServiceImpl.class);
	
	public String htmltopdf(String input, String output)
	{
		return "ok";
	}

}
