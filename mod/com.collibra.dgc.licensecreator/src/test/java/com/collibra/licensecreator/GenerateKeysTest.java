package com.collibra.licensecreator;


import com.smardec.license4j.LicenseUtil;

public class GenerateKeysTest {

	static public void main(String[] args) {
		try {
			LicenseUtil.createKeyPair("keys.txt");
			System.out.println("Keys are written to file \"" + "keys.txt" + "\"");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
