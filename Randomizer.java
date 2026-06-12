package rngbuilder;

import java.security.SecureRandom;

public class Randomizer {
	SecureRandom random = new SecureRandom();

	// Constants
	String alphabetLower = "abcdefghijklmnopqrstuvwxyz";
	String alphabetUpper = alphabetLower.toUpperCase();
	String alphabetDigits = "0123456789";
	String alphabetSymbols = "'%#&@]<~:_^,.<";
	String alphabet = alphabetLower + alphabetUpper + alphabetDigits + alphabetSymbols;


	public Randomizer() {	}

	public String generatePassword(int byteSize) {
		StringBuilder sb = new StringBuilder();
		byte[] arr = new byte[byteSize];
		random.nextBytes(arr);
		for (byte element : arr) {
			int temp = Byte.toUnsignedInt(element);
			temp = temp%alphabet.length();
			char res = alphabet.charAt(temp);
			sb.append(res);
		}
		return sb.toString();

	}
}

