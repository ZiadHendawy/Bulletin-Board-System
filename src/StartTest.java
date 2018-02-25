import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Test;


public class StartTest {

	@Test
	public void StartSmokeTest() {
	
		
		
		Start.start(initMap());
		
	}
	public HashMap <String, String> initMap(){
		HashMap <String, String> map = new HashMap <String, String>();
		map.put("RW.server", "127.0.0.1");
		map.put("RW.server.port", "9898");
		map.put("RW.numberOfReaders", "1");
		map.put("RW.numberOfWriters", "1");
		
		map.put("RW.reader0", "127.0.0.1");
		map.put("RW.reader0.pw", "123456");
		
		map.put("RW.writer0", "127.0.0.1");
		map.put("RW.writer0.pw", "123456");
		return map;
	}
	

	@Test
	public void SplitMethodTest(){
		String [] splited = Start.split("ziad@localhost");
		assertEquals(splited[1], "ziad");
		assertEquals(splited[0], "localhost");
		assertEquals(splited.length, 2);
		
		splited = Start.split("ziad@127.0.0.1");
		assertEquals(splited[1], "ziad");
		assertEquals(splited[0], "127.0.0.1");
		assertEquals(splited.length, 2);
		
		
		splited = Start.split("localhost");
		assertEquals(splited[0], "localhost");
		assertEquals(splited[1], null);
		assertEquals(splited.length, 2);
		
		splited = Start.split("127.0.0.1");
		assertEquals(splited[0], "127.0.0.1");
		assertEquals(splited[1], null);
		assertEquals(splited.length, 2);
		
	}

}
