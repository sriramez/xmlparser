package xmlparser;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisConnector {

	private static Jedis jedisInstance = null;

	private JedisConnector() {

	}

	public static Jedis getInstance() {
		if (jedisInstance == null) {
			JedisPool pool = new JedisPool("localhost", 6379);
			jedisInstance = pool.getResource();
		}
		return jedisInstance;
	}

}
