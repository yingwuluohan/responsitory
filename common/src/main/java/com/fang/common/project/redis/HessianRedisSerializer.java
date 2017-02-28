package com.fang.common.project.redis;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.common.utils.Exception.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by fn on 2017/2/28.
 */
public class HessianRedisSerializer<T> implements RedisSerializer<T> {
    @SuppressWarnings("unchecked")
    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (SerializationUtils.isEmpty(bytes)) {
            return null;
        }
        T result = null;
        ByteArrayInputStream byteInputStream = null;
        Hessian2Input objectInputStream = null;
        try {
            byteInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new Hessian2Input(byteInputStream);
            //objectInputStream.startMessage();
            result = (T) objectInputStream.readObject();
            //objectInputStream.completeMessage();
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize with hessian",
                    ex);
        } finally {
            if (null != objectInputStream) {
                try {
                    objectInputStream.close();
                    byteInputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return SerializationUtils.EMPTY_ARRAY;
        }
        byte[] result = null;
        Hessian2Output objectOutputStream = null;
        ByteArrayOutputStream byteOutputStream = null;
        try {
            byteOutputStream = new ByteArrayOutputStream();
            objectOutputStream = new Hessian2Output(byteOutputStream);
            //objectOutputStream.startMessage();
            objectOutputStream.writeObject(t);
            //objectOutputStream.completeMessage();
            objectOutputStream.flush();
            result = byteOutputStream.toByteArray();
        } catch (Exception e) {
            throw new SerializationException("Cannot serialize with hessian", e);
        } finally {
            if (null != objectOutputStream) {
                try {
                    objectOutputStream.close();
                    byteOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
