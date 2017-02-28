package com.fang.common.project.redis;

import java.util.*;

public abstract class SerializationUtils {


	public static final byte[] EMPTY_ARRAY = new byte[0];

	public static boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}


	@SuppressWarnings("unchecked")
	public static <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type, RedisSerializer<?> redisSerializer) {
		// connection in pipeline/multi mode
		if (rawValues == null) {
			return null;
		}

		Collection<Object> values = (List.class.isAssignableFrom(type) ? new ArrayList<Object>(rawValues.size())
				: new LinkedHashSet<Object>(rawValues.size()));
		for (byte[] bs : rawValues) {
			values.add(redisSerializer.deserialize(bs));
		}

		return (T) values;
	}

	@SuppressWarnings("unchecked")
	public static <T> Set<T> deserialize(Set<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
		return deserializeValues(rawValues, Set.class, redisSerializer);
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> deserialize(List<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
		return deserializeValues(rawValues, List.class, redisSerializer);
	}

	@SuppressWarnings("unchecked")
	public static <T> Collection<T> deserialize(Collection<byte[]> rawValues, RedisSerializer<T> redisSerializer) {
		return deserializeValues(rawValues, List.class, redisSerializer);
	}

}
