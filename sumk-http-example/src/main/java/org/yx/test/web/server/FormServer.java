package org.yx.test.web.server;

import org.yx.annotation.Bean;
import org.yx.annotation.http.Web;
import org.yx.sumk.form.annotation.FormParam;
import org.yx.test.web.demo.QueryObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : wjiajun
 * @description:
 */
@Bean
public class FormServer {

    @Web(value = "echo_object")
    @FormParam
    public List<String> echo_object(QueryObject object, List<String> names) {
        List<String> list = new ArrayList<>();
        for (String name : names) {
            list.add("echo" + " " + name);
        }

        System.out.println("object name:" +  object.getName());
        System.out.println("object age:" +  object.getAge());
        System.out.println("object date:" +  object.getDate());
        System.out.println("object map:" +  object.getMap());
        return list;
    }

    @Web(value = "echo_raw")
    @FormParam
    public List<String> echo_raw(QueryObject object, List<String> names, String name, int age) {
        List<String> list = new ArrayList<>();
        for (String name1 : names) {
            list.add("echo" + " " + name1);
        }

        System.out.println("object name:" +  object.getName());
        System.out.println("object age:" +  object.getAge());
        System.out.println("object date:" +  object.getDate());
        System.out.println("object map:" +  object.getMap());
        System.out.println("string name:" +  name);
        System.out.println("int name:" +  age);
        return list;
    }
}
