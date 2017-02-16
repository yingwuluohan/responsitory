package com.common.utils.redis;


import com.common.utils.Exception.SerializationException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;

public class JdkSerializationRedisSerializer implements RedisSerializer<Object> {


	private final Converter<Object, byte[]> serializer = new SerializingConverter();
	private final Converter<byte[], Object> deserializer = new DeserializingConverter();

	public Object deserialize(byte[] bytes) {
		if (SerializationUtils.isEmpty(bytes)) {
			return null;
		}

		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}

	
	public byte[] serialize(Object object) {
		if (object == null) {
			return SerializationUtils.EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			throw new SerializationException("Cannot serialize", ex);
		}
	}

}
