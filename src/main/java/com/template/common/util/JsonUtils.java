package com.template.common.util;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.NullNode;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevin on 2017-12-26
 */
public class JsonUtils  {
	private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	private static final String DEFAULT_DATA_PATH = "data";
	private static ObjectMapper mapper = new ObjectMapper();

	public static String toJson(Object object) {
		String json = "";

		try {
			json = mapper.writeValueAsString(object);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return json;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String json, Object valueType) {
		return (T)toObject(json, valueType.getClass());
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(String json, Class<T> valueType) {
		Object object = null;

		try {
			object = mapper.readValue(json, valueType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return (T) object;

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> T toObject(String json, TypeReference valueTypeRef) {
		Object object = null;

		try {
			object = mapper.readValue(json, valueTypeRef);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return (T)object;
	}

	private static JsonNode getRootNode(String json) {
		if (StringUtils.isEmpty(json)) {
			return null;
		}

		try {
			return mapper.readTree(json);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	/**
	 * Map에 dbid, ArrayList<MODEL>이 포함된 경우 사용한다.
	 *
	 * @param json
	 * @param valueTypeRef
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> toMap(String json, TypeReference valueTypeRef) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		JsonNode jsonNode = getRootNode(json);

		if (jsonNode == null) {
			return resultMap;
		}

		JsonNode dbidNode = jsonNode.path("rscId");
		resultMap.put("dbid", toObject(dbidNode.toString(), String.class));

		JsonNode dataNode = jsonNode.path("data");
		resultMap.put("data", toObject(dataNode.toString(), valueTypeRef));

		return resultMap;
	}

	/**
	 * Map에 dbid, ArrayList<MODEL>이 포함된 경우 사용한다.
	 *
	 * @param json
	 * @return
	 */
	public static Map<String, Object> toMap(String json) {
		try {
			return mapper.readValue(json, new TypeReference<Map<String, Object>>() {
			});
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T toDataObject(String json, Class<T> valueType) {
		return toDataObject(json, DEFAULT_DATA_PATH, valueType);
	}

	public static <T> T rootDataToObject(String json, Class<T> valueType) {
		return toRootDataObject(json, valueType);
	}


	/**
	 * 존재하지 않을 가능성이 있는 프로퍼티 조회시 사용.
	 * 의도된 바이므로 별도의 에러로그를 찍지 않는다.
	 * 프로퍼티가 존재하지 않을 시에는 defaultValue 를 반환함.
	 * @param json
	 * @param dataName
	 * @param valueType
	 * @param defaultValue
	 * @return
	 */
	public static <T> T toDataObject(String json, String dataName, Class<T> valueType, T defaultValue) {
		JsonNode rootNode = getRootNode(json);

		if (rootNode == null) {
			return null;
		}

		JsonNode dataNode = rootNode.path(dataName);

		try {
			return mapper.readValue(dataNode, valueType);
		} catch (IOException e) {
		}

		return defaultValue;
	}

	public static <T> T toRootDataObject(String json, Class<T> valueType) {
		JsonNode rootNode = getRootNode(json);
		if (rootNode == null) {
			return null;
		}

		try {
			return mapper.readValue(rootNode, valueType);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T toDataObject(String json, String dataName, Class<T> valueType) {
		JsonNode rootNode = getRootNode(json);

		if (rootNode == null) {
			return null;
		}

		JsonNode dataNode = rootNode.path(dataName);

		try {
			return mapper.readValue(dataNode, valueType);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T toDataObject(String json, int index, Class<T> valueType) {
		JsonNode rootNode = getRootNode(json);

		if (rootNode == null) {
			return null;
		}

		JsonNode dataNode = rootNode.path(index);

		try {
			return mapper.readValue(dataNode, valueType);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T> T toDataObject(String json, TypeReference<?> valueTypeRef) {
		return (T)toDataObject(json, DEFAULT_DATA_PATH, valueTypeRef);
	}

	@SuppressWarnings("unchecked")
	public static <T> T toDataObject(String json, String dataName, TypeReference<?> valueTypeRef) {
		JsonNode rootNode = getRootNode(json);

		if (rootNode == null) {
			return null;
		}

		JsonNode dataNode = rootNode.path(dataName);

		try {
			return (T)mapper.readValue(dataNode, valueTypeRef);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T toDataObjectEx(String json, String dataName, TypeReference<?> valueTypeRef) throws IOException {
		JsonNode rootNode = getRootNode(json);

		if (rootNode == null) {
			return null;
		}

		JsonNode dataNode = rootNode.path(dataName);
		return (T)mapper.readValue(dataNode, valueTypeRef);

	}

	public static <T> List<T> toResultListDataObject(String json, String filedName, Class<T> valueType) {
		JsonNode rootNode = getRootNode(json);
		if (rootNode == null) {
			return null;
		}

		try {
			JsonNode resultNode = rootNode.findValue("resultData");
			if(resultNode.get(filedName).isNull()) {
				return null;
			}
			ArrayNode arrayNode = (ArrayNode)resultNode.get(filedName);
			List<T> convertList = new ArrayList<>();
			for (JsonNode jsonNode : arrayNode) {
				convertList.add(mapper.readValue(jsonNode, valueType));
			}

			return convertList;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public static <T> T toResultDataObject(String json, String filedName, Class<T> valueType) {
		JsonNode rootNode = getRootNode(json);
		if (rootNode == null) {
			return null;
		}

		try {
			JsonNode resultNode = rootNode.findValue("resultData");
			JsonNode fieldNode = resultNode.findValue(filedName);
			T convertNode = mapper.readValue(fieldNode, valueType);

			return convertNode;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}
}
