package org.yx.test.web.server;

import org.yx.log.Log;
import org.yx.log.LogType;
import org.yx.main.SumkServer;

public class Main {
	public static void main(String[] args) {
		try {
			Log.setLogType(LogType.console);//因为没有引入日志包，才写的临时代码
			Log.get(Main.class).info("为了测试方便，测试环境内置了zookeeper服务器。");
			long begin=System.currentTimeMillis();
			SumkServer.start();
			System.out.println("启动完成,除zookeeper服务器外耗时："+(System.currentTimeMillis()-begin)+"毫秒");
			Thread.currentThread().join();
		} catch (Exception e) {
			Log.printStack("main",e);
		}
	}

}
