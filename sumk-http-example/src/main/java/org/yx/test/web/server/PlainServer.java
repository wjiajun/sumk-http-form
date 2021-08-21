package org.yx.test.web.server;

import org.yx.annotation.Bean;
import org.yx.annotation.Param;
import org.yx.annotation.http.Web;
import org.yx.http.MessageType;
import org.yx.sumk.form.annotation.FormParam;
import org.yx.sumk.form.annotation.JsonParam;
import org.yx.test.web.demo.QueryObject;

import java.util.ArrayList;
import java.util.List;

@Bean
public class PlainServer {

	@Web(value = "echo")
	public List<String> echo(String echo, List<String> names) {
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(value = "echoJson")
	@JsonParam
	public List<String> echoJson(String echo, List<String> names) {
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(value = "echoBodyJson")
	@FormParam
	public List<String> echoBodyJson(String echo, List<String> names) {
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}

	@Web(value = "base64", requestType = MessageType.BASE64, responseType = MessageType.BASE64)
	public List<String> base64(@Param(max = 20) String echo, List<String> names) {
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}
}
