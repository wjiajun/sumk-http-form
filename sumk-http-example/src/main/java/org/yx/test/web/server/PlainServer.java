package org.yx.test.web.server;

import org.yx.annotation.Bean;
import org.yx.annotation.Param;
import org.yx.annotation.http.Web;
import org.yx.http.MessageType;

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

	@Web(value = "base64", requestType = MessageType.BASE64, responseType = MessageType.BASE64)
	public List<String> base64(@Param(max = 20) String echo, List<String> names) {
		List<String> list = new ArrayList<>();
		for (String name : names) {
			list.add(echo + " " + name);
		}
		return list;
	}
}
