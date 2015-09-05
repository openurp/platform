package org.beangle.commons.text.i18n;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.nustaq.serialization.FSTConfiguration;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

class Test {
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();

	public static void main(String[] args) throws Exception {

		List<String> obj = Arrays.asList("OSChina.NET", "Team@OSC", "Git@OSC", "Sonar@OSC");
		byte[] bits = serialize(obj);
		for (byte b : bits) {
			System.out.print(Byte.toString(b) + " ");
		}
		System.out.println();
		System.out.println(bits.length);
		System.out.println(deserialize(bits));

	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FSTObjectOutput out = conf.getObjectOutput(baos);
		out.writeObject(obj);
		out.flush();
		baos.close();
		return baos.toByteArray();
	}

	public static Object deserialize(byte[] bits) throws Exception {
		if (bits == null || bits.length == 0)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(bits);
		FSTObjectInput in = conf.getObjectInput(bais);
		Object result = in.readObject();
		bais.close();
		return result;
	}
}