package com.mavericksoft.qepi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TripleDES{
    
    public static final String CLAVE ="12345678AbcdEfgH98765432";

	/**
	 * Main para la prueba
	 * @
	 * */
	public static void main(String args[]) throws Exception {
		if(args.length != 3 ){
			System.out.println("ERROR: Numero de argumentos incorrectos. TripleDES opcion cadena  clave_24_caracteres Ejm: TripleDES encriptar luis.segovia@tcs.com  12345678AbcdEfgH98765432");
			//return "ERROR: Numero de argumentos incorrectos. TripleDES opcion cadena  clave_24_caracteres Ejm: TripleDES encriptar luis.segovia@tcs.com  12345678AbcdEfgH98765432";
			return;
		}
		boolean encriptar;
		String clave;
		String datoEnClaro;
		String datoEncriptado;
		//true= encriptar, false= desencriptar
		encriptar=(args[0].trim().equalsIgnoreCase("encriptar"))?true:false;
		//La clave es de 24 caracteres (3 octetos). Ejm 12345678AbcdEfgH98765432
		clave = args[2];
		if(encriptar){
			datoEnClaro = args[1];
			System.out.println(encripta(clave, datoEnClaro));
			//return encriptaTripleDes(clave, datoEnClaro);
		}else{
			datoEncriptado = args[1];
			System.out.println(desencripta(clave, datoEncriptado));
			//return desencriptaTripleDes(clave, datoEncriptado);
		}
	}

	/**
	 * Metodo que encripta una cadena con el algoritmo TripleDes
	 * utiliza ESede/ECB/PKCS5Padding
	 * @param String clave de 24 caracteres (3 octetos para TripleDes)
	 * @param datoEnClaro, dato a encriptar
	 * @return String cadena encriptada en hexadecimal
	 * @throws Exception
	 * */
	public static String encripta(String clave, String datoEnClaro)throws Exception{
		if(clave.length()!=24){
			throw new Exception("La clave debe ser de 24 caracteres");
		}
		String claveCadenaHex = TripleDES.cadenaASCIIACadenaHexadecimal(clave);
		byte[] llave = TripleDES.cadenaHexadecimalAArregloBytes(claveCadenaHex); 
		SecretKeySpec key = new SecretKeySpec(llave, "DESede");
		Cipher criptografia = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		byte datoAEncriptar[] = datoEnClaro.getBytes("UTF-8");
		criptografia.init(Cipher.ENCRYPT_MODE, key);
		byte datoEncriptado[] = criptografia.doFinal(datoAEncriptar);
		String datoEncriptadoEnHexadecimal = TripleDES.arregloBytesACadenaHexadecimal(datoEncriptado);
		return datoEncriptadoEnHexadecimal;  

	}



	/**
	 * Metodo que desencripta una cadena con el algoritmo TripleDes
	 * utiliza ESede/ECB/PKCS5Padding
	 * @param String clave de 24 caracteres (3 octetos para TripleDes)
	 * @param datoEncriptado, dato encriptado en hexadecimal
	 * @return String cadena desencriptada
	 * @throws Exception
	 * */
	public static String desencripta(String clave, String datoEncriptadoEnHexadecimal)throws Exception{
		if(clave.length()!=24){
			throw new Exception("La clave debe ser de 24 caracteres");
		}
		String claveCadenaHex = TripleDES.cadenaASCIIACadenaHexadecimal(clave);
		byte[] llave = TripleDES.cadenaHexadecimalAArregloBytes(claveCadenaHex); 
		SecretKeySpec key = new SecretKeySpec(llave, "DESede");
		Cipher criptografia = Cipher.getInstance("DESede/ECB/PKCS5Padding");
		byte datoADesencriptar[] = TripleDES.cadenaHexadecimalAArregloBytes(datoEncriptadoEnHexadecimal);
		criptografia.init(Cipher.DECRYPT_MODE, key);
		byte datoDesencriptado[] = criptografia.doFinal(datoADesencriptar);
		String desencriptado = new String(datoDesencriptado, 0, datoDesencriptado.length, "UTF8");
		return desencriptado;
	}

	/**
	 * Transforma un arreglo de bytes a cadena hexadecimal
	 * @param byte[] arreglo, arreglo de bytes a transformar
	 * @return String cadena hexadecimal resultante
	 * */
	private static String arregloBytesACadenaHexadecimal(byte[] arreglo) {
		char[] kDigitos = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		int longitudRequerida = arreglo.length;
		StringBuilder cadenaTemporal = new StringBuilder(longitudRequerida * 2);
		for (int i = 0; i < longitudRequerida; i++) {
			int valor = (arreglo[i] + 256) % 256;
			int indiceAlto = valor >> 4;
		int indiceBajo = valor & 0x0f;
		cadenaTemporal.append(kDigitos[indiceAlto]);
		cadenaTemporal.append(kDigitos[indiceBajo]);
		}
		return cadenaTemporal.toString();
	}

	/**
	 * Transforma una cadena hexadecimal a un arreglo de bytes
	 * @param String cadena, cadena hexadecimal a transformar
	 * @return byte[] arreglo de bytes resultante 
	 * */
	private static byte[] cadenaHexadecimalAArregloBytes(String cadena) {
		char[] hex = cadena.toCharArray();
		int longitud = hex.length / 2;
		byte[] arregloTemporal = new byte[longitud];
		for (int i = 0; i < longitud; i++) {
			int alto = Character.digit(hex[i * 2], 16);
			int bajo = Character.digit(hex[i * 2 + 1], 16);
			int valor = (alto << 4) | bajo;
			if (valor > 127){
				valor -= 256;
			}
			arregloTemporal[i] = (byte) valor;
		}
		return arregloTemporal;
	}

	/**
	 * Transforma una cadena ascii a cadena en hexadecimal
	 * @param String cadena, en ascii a transformar
	 * @return String cadena hexadecimal resultante.
	 * */
	private static String cadenaASCIIACadenaHexadecimal(String cadena){
		char caracter;
		StringBuffer cadenaHexadecimal = new StringBuffer();
		for(int i=0; i<cadena.length(); i++){
			caracter = cadena.charAt(i);
			cadenaHexadecimal.append(Integer.toHexString((int)caracter));
		}
		return cadenaHexadecimal.toString().toUpperCase();
	}

}  
