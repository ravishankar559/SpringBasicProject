package com.sample.SpringRestServices.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.SpringRestServices.DTO.RequestDTO;

@Component
public class RequestParserAndGenerator {
   
	@Value("#{${taskNameMap}}")
	private Map<String,String> taskNameMap;
	
	@Value("#{${taskVoMap}}")
	private Map<String,String> taskVoMap;
	
	@Value("#{${validFlowsForAPI}}")
	private Map<String,List<String>> validFlowsForAPI;
	
	public String getTaskNameFromRequest(String pathInfo) {
		String taskName = null;
		//Map<String,String> taskNameMap = this.taskNameMap;
		if(null != taskNameMap && !taskNameMap.isEmpty() && !StringUtils.isEmpty(pathInfo)) {
			for(Map.Entry<String, String> taskMap : taskNameMap.entrySet()) {
				if(pathInfo.contains(taskMap.getKey())){
					taskName = taskMap.getValue();
				}
			}
		}
		return taskName;
	}

	public RequestDTO getRequestFromJson(HttpServletRequest request, String taskName) {
		RequestDTO requestDTO = null;
		if(null != taskVoMap && !taskVoMap.isEmpty()) {
			String voName = taskVoMap.get(taskName);
			BufferedReader bufferedReader = null;
			ObjectMapper objectMapper = new ObjectMapper();
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream()));
				StringBuilder jsonRequestBuilder = new StringBuilder();
				String line = null;
				String jsonRequestData = null;
				while((line = bufferedReader.readLine()) != null ) {
					jsonRequestBuilder.append(line);
				}
				if(jsonRequestBuilder != null) {
					jsonRequestData = jsonRequestBuilder.toString();
				}
				requestDTO = (RequestDTO) objectMapper.readValue(jsonRequestData, Class.forName(voName));
			}catch (IOException e) {
				
			}catch (ClassNotFoundException e) {
				
			}
		}
		return requestDTO;
	}

	public boolean isValidFlow(String requestPath, String flow) {
		
		List<String> validFlows = null;
		if(null != validFlowsForAPI && !validFlowsForAPI.isEmpty() && !StringUtils.isEmpty(requestPath)) {
			for(Map.Entry<String, List<String>> apiAsKey : validFlowsForAPI.entrySet()) {
				if(requestPath.contains(apiAsKey.getKey())){
					validFlows = apiAsKey.getValue();
					for(String Flowtype : validFlows) {
						if(Flowtype.equals(flow)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
