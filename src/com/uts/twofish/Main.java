package com.uts.twofish;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        System.out.println("===Selamat datang di program Algoritma Twofish===");
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("Pilih menu");
            System.out.println("1. Encrypt");
            System.out.println("2. Decrypt");
            System.out.println("3. Exit");
            System.out.print("Masukkan pilihan: ");
            String choice = input.nextLine();
            switch (choice) {
                case "1":
                    System.out.print("Masukkan plaintext: ");
                    String strPlainText = input.nextLine();
                    System.out.print("Masukkan key (16, 24, 32 bytes): ");
                    String strKeyEncrypt = input.nextLine();

                    byte[] bytePlainText = strPlainText.getBytes();
                    byte[] plainText = new byte[128];
                    for (int i = 0; i < 128; i++) {
                        if (i < bytePlainText.length) {
                            plainText[i] = bytePlainText[i];
                        } else {
                            plainText[i] = 0;
                        }
                    }

                    byte[] byteKeyEncrypt = strKeyEncrypt.getBytes();
                    byte[] keyEncrypt = new byte[strKeyEncrypt.length()];
                    System.arraycopy(byteKeyEncrypt, 0, keyEncrypt, 0, strKeyEncrypt.length());
                    Object twofishKeyEncrypt = Twofish.makeKey(keyEncrypt); // generate key

                    byte[] encrypt = Twofish.blockEncrypt(plainText, 0, twofishKeyEncrypt); // encrypt
                    StringBuilder hexEncrypt = new StringBuilder();
                    for (byte b : encrypt) {
                        hexEncrypt.append(String.format("%02x", b));
                    }
                    System.out.println("Hasil Enkripsi : " + hexEncrypt);
                    System.out.println();
                    break;
                case "2":
                    System.out.print("Masukkan ciphertext: ");
                    String strCipherText = input.nextLine();
                    System.out.print("Masukkan key (16, 24, 32 bytes): ");
                    String strKeyDecrypt = input.nextLine();

                    byte[] byteChiperText = new byte[strCipherText.length() / 2];
                    for (int i = 0; i < strCipherText.length(); i += 2) {
                        byteChiperText[i / 2] = (byte) ((Character.digit(strCipherText.charAt(i), 16) << 4) + Character.digit(strCipherText.charAt(i + 1), 16));
                    }
                    byte[] cipherText = new byte[128];
                    for (int i = 0; i < 128; i++) {
                        if (i < byteChiperText.length) {
                            cipherText[i] = byteChiperText[i];
                        } else {
                            cipherText[i] = 0;
                        }
                    }

                    byte[] byteKeyDecrypt = strKeyDecrypt.getBytes();
                    byte[] keyDecrypt = new byte[strKeyDecrypt.length()];
                    System.arraycopy(byteKeyDecrypt, 0, keyDecrypt, 0, strKeyDecrypt.length());
                    Object twofishKeyDecrypt = Twofish.makeKey(keyDecrypt); // generate key

                    byte[] decrypt = Twofish.blockDecrypt(cipherText, 0, twofishKeyDecrypt); // decrypt
                    String strDecrypt = new String(decrypt);
                    System.out.println("Hasil Dekripsi : " + strDecrypt);
                    System.out.println();
                    break;
                case "3":
                    isRunning = false;
                    input.close();
                    break;
                default:
                    System.out.println("Invalid choice");
                    System.out.println();
                    break;
            }
        }
        System.out.println("===Terima kasih telah menggunakan program Algoritma Twofish===");
    }
}