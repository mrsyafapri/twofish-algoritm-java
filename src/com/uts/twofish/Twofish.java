package com.uts.twofish;

import java.security.InvalidKeyException;

public class Twofish {
    private static final int BLOCK_SIZE = 16; // how many bytes in a block
    private static final int ROUNDS = 16; // how many rounds in encryption (default 16)
    private static final int INPUT_WHITEN = 0; // index for start of round input whitening
    private static final int OUTPUT_WHITEN = INPUT_WHITEN + BLOCK_SIZE / 4; // index for start of round output whitening
    private static final int ROUND_SUBKEYS = OUTPUT_WHITEN + BLOCK_SIZE / 4; // number of subkeys for each round
    private static final int SK_STEP = 0x02020202; // a fixed constant used to generate even subkeys
    private static final int SK_BUMP = 0x01010101; // a fixed constant used to generate odd subkeys
    private static final int SK_ROTL = 9; // fixed number determining bit shift in keys generator
    private static final byte[][] P = new byte[][]{{ // p0
            (byte) 0xA9, (byte) 0x67, (byte) 0xB3, (byte) 0xE8, (byte) 0x04, (byte) 0xFD, (byte) 0xA3, (byte) 0x76, (byte) 0x9A, (byte) 0x92, (byte) 0x80, (byte) 0x78, (byte) 0xE4, (byte) 0xDD, (byte) 0xD1, (byte) 0x38, (byte) 0x0D, (byte) 0xC6, (byte) 0x35, (byte) 0x98, (byte) 0x18, (byte) 0xF7, (byte) 0xEC, (byte) 0x6C, (byte) 0x43, (byte) 0x75, (byte) 0x37, (byte) 0x26, (byte) 0xFA, (byte) 0x13, (byte) 0x94, (byte) 0x48, (byte) 0xF2, (byte) 0xD0, (byte) 0x8B, (byte) 0x30, (byte) 0x84, (byte) 0x54, (byte) 0xDF, (byte) 0x23, (byte) 0x19, (byte) 0x5B, (byte) 0x3D, (byte) 0x59, (byte) 0xF3, (byte) 0xAE, (byte) 0xA2, (byte) 0x82, (byte) 0x63, (byte) 0x01, (byte) 0x83, (byte) 0x2E, (byte) 0xD9, (byte) 0x51, (byte) 0x9B, (byte) 0x7C, (byte) 0xA6, (byte) 0xEB, (byte) 0xA5, (byte) 0xBE, (byte) 0x16, (byte) 0x0C, (byte) 0xE3, (byte) 0x61, (byte) 0xC0, (byte) 0x8C, (byte) 0x3A, (byte) 0xF5, (byte) 0x73, (byte) 0x2C, (byte) 0x25, (byte) 0x0B, (byte) 0xBB, (byte) 0x4E, (byte) 0x89, (byte) 0x6B, (byte) 0x53, (byte) 0x6A, (byte) 0xB4, (byte) 0xF1, (byte) 0xE1, (byte) 0xE6, (byte) 0xBD, (byte) 0x45, (byte) 0xE2, (byte) 0xF4, (byte) 0xB6, (byte) 0x66, (byte) 0xCC, (byte) 0x95, (byte) 0x03, (byte) 0x56, (byte) 0xD4, (byte) 0x1C, (byte) 0x1E, (byte) 0xD7, (byte) 0xFB, (byte) 0xC3, (byte) 0x8E, (byte) 0xB5, (byte) 0xE9, (byte) 0xCF, (byte) 0xBF, (byte) 0xBA, (byte) 0xEA, (byte) 0x77, (byte) 0x39, (byte) 0xAF, (byte) 0x33, (byte) 0xC9, (byte) 0x62, (byte) 0x71, (byte) 0x81, (byte) 0x79, (byte) 0x09, (byte) 0xAD, (byte) 0x24, (byte) 0xCD, (byte) 0xF9, (byte) 0xD8, (byte) 0xE5, (byte) 0xC5, (byte) 0xB9, (byte) 0x4D, (byte) 0x44, (byte) 0x08, (byte) 0x86, (byte) 0xE7, (byte) 0xA1, (byte) 0x1D, (byte) 0xAA, (byte) 0xED, (byte) 0x06, (byte) 0x70, (byte) 0xB2, (byte) 0xD2, (byte) 0x41, (byte) 0x7B, (byte) 0xA0, (byte) 0x11, (byte) 0x31, (byte) 0xC2, (byte) 0x27, (byte) 0x90, (byte) 0x20, (byte) 0xF6, (byte) 0x60, (byte) 0xFF, (byte) 0x96, (byte) 0x5C, (byte) 0xB1, (byte) 0xAB, (byte) 0x9E, (byte) 0x9C, (byte) 0x52, (byte) 0x1B, (byte) 0x5F, (byte) 0x93, (byte) 0x0A, (byte) 0xEF, (byte) 0x91, (byte) 0x85, (byte) 0x49, (byte) 0xEE, (byte) 0x2D, (byte) 0x4F, (byte) 0x8F, (byte) 0x3B, (byte) 0x47, (byte) 0x87, (byte) 0x6D, (byte) 0x46, (byte) 0xD6, (byte) 0x3E, (byte) 0x69, (byte) 0x64, (byte) 0x2A, (byte) 0xCE, (byte) 0xCB, (byte) 0x2F, (byte) 0xFC, (byte) 0x97, (byte) 0x05, (byte) 0x7A, (byte) 0xAC, (byte) 0x7F, (byte) 0xD5, (byte) 0x1A, (byte) 0x4B, (byte) 0x0E, (byte) 0xA7, (byte) 0x5A, (byte) 0x28, (byte) 0x14, (byte) 0x3F, (byte) 0x29, (byte) 0x88, (byte) 0x3C, (byte) 0x4C, (byte) 0x02, (byte) 0xB8, (byte) 0xDA, (byte) 0xB0, (byte) 0x17, (byte) 0x55, (byte) 0x1F, (byte) 0x8A, (byte) 0x7D, (byte) 0x57, (byte) 0xC7, (byte) 0x8D, (byte) 0x74, (byte) 0xB7, (byte) 0xC4, (byte) 0x9F, (byte) 0x72, (byte) 0x7E, (byte) 0x15, (byte) 0x22, (byte) 0x12, (byte) 0x58, (byte) 0x07, (byte) 0x99, (byte) 0x34, (byte) 0x6E, (byte) 0x50, (byte) 0xDE, (byte) 0x68, (byte) 0x65, (byte) 0xBC, (byte) 0xDB, (byte) 0xF8, (byte) 0xC8, (byte) 0xA8, (byte) 0x2B, (byte) 0x40, (byte) 0xDC, (byte) 0xFE, (byte) 0x32, (byte) 0xA4, (byte) 0xCA, (byte) 0x10, (byte) 0x21, (byte) 0xF0, (byte) 0xD3, (byte) 0x5D, (byte) 0x0F, (byte) 0x00, (byte) 0x6F, (byte) 0x9D, (byte) 0x36, (byte) 0x42, (byte) 0x4A, (byte) 0x5E, (byte) 0xC1, (byte) 0xE0}, { // p1
            (byte) 0x75, (byte) 0xF3, (byte) 0xC6, (byte) 0xF4, (byte) 0xDB, (byte) 0x7B, (byte) 0xFB, (byte) 0xC8, (byte) 0x4A, (byte) 0xD3, (byte) 0xE6, (byte) 0x6B, (byte) 0x45, (byte) 0x7D, (byte) 0xE8, (byte) 0x4B, (byte) 0xD6, (byte) 0x32, (byte) 0xD8, (byte) 0xFD, (byte) 0x37, (byte) 0x71, (byte) 0xF1, (byte) 0xE1, (byte) 0x30, (byte) 0x0F, (byte) 0xF8, (byte) 0x1B, (byte) 0x87, (byte) 0xFA, (byte) 0x06, (byte) 0x3F, (byte) 0x5E, (byte) 0xBA, (byte) 0xAE, (byte) 0x5B, (byte) 0x8A, (byte) 0x00, (byte) 0xBC, (byte) 0x9D, (byte) 0x6D, (byte) 0xC1, (byte) 0xB1, (byte) 0x0E, (byte) 0x80, (byte) 0x5D, (byte) 0xD2, (byte) 0xD5, (byte) 0xA0, (byte) 0x84, (byte) 0x07, (byte) 0x14, (byte) 0xB5, (byte) 0x90, (byte) 0x2C, (byte) 0xA3, (byte) 0xB2, (byte) 0x73, (byte) 0x4C, (byte) 0x54, (byte) 0x92, (byte) 0x74, (byte) 0x36, (byte) 0x51, (byte) 0x38, (byte) 0xB0, (byte) 0xBD, (byte) 0x5A, (byte) 0xFC, (byte) 0x60, (byte) 0x62, (byte) 0x96, (byte) 0x6C, (byte) 0x42, (byte) 0xF7, (byte) 0x10, (byte) 0x7C, (byte) 0x28, (byte) 0x27, (byte) 0x8C, (byte) 0x13, (byte) 0x95, (byte) 0x9C, (byte) 0xC7, (byte) 0x24, (byte) 0x46, (byte) 0x3B, (byte) 0x70, (byte) 0xCA, (byte) 0xE3, (byte) 0x85, (byte) 0xCB, (byte) 0x11, (byte) 0xD0, (byte) 0x93, (byte) 0xB8, (byte) 0xA6, (byte) 0x83, (byte) 0x20, (byte) 0xFF, (byte) 0x9F, (byte) 0x77, (byte) 0xC3, (byte) 0xCC, (byte) 0x03, (byte) 0x6F, (byte) 0x08, (byte) 0xBF, (byte) 0x40, (byte) 0xE7, (byte) 0x2B, (byte) 0xE2, (byte) 0x79, (byte) 0x0C, (byte) 0xAA, (byte) 0x82, (byte) 0x41, (byte) 0x3A, (byte) 0xEA, (byte) 0xB9, (byte) 0xE4, (byte) 0x9A, (byte) 0xA4, (byte) 0x97, (byte) 0x7E, (byte) 0xDA, (byte) 0x7A, (byte) 0x17, (byte) 0x66, (byte) 0x94, (byte) 0xA1, (byte) 0x1D, (byte) 0x3D, (byte) 0xF0, (byte) 0xDE, (byte) 0xB3, (byte) 0x0B, (byte) 0x72, (byte) 0xA7, (byte) 0x1C, (byte) 0xEF, (byte) 0xD1, (byte) 0x53, (byte) 0x3E, (byte) 0x8F, (byte) 0x33, (byte) 0x26, (byte) 0x5F, (byte) 0xEC, (byte) 0x76, (byte) 0x2A, (byte) 0x49, (byte) 0x81, (byte) 0x88, (byte) 0xEE, (byte) 0x21, (byte) 0xC4, (byte) 0x1A, (byte) 0xEB, (byte) 0xD9, (byte) 0xC5, (byte) 0x39, (byte) 0x99, (byte) 0xCD, (byte) 0xAD, (byte) 0x31, (byte) 0x8B, (byte) 0x01, (byte) 0x18, (byte) 0x23, (byte) 0xDD, (byte) 0x1F, (byte) 0x4E, (byte) 0x2D, (byte) 0xF9, (byte) 0x48, (byte) 0x4F, (byte) 0xF2, (byte) 0x65, (byte) 0x8E, (byte) 0x78, (byte) 0x5C, (byte) 0x58, (byte) 0x19, (byte) 0x8D, (byte) 0xE5, (byte) 0x98, (byte) 0x57, (byte) 0x67, (byte) 0x7F, (byte) 0x05, (byte) 0x64, (byte) 0xAF, (byte) 0x63, (byte) 0xB6, (byte) 0xFE, (byte) 0xF5, (byte) 0xB7, (byte) 0x3C, (byte) 0xA5, (byte) 0xCE, (byte) 0xE9, (byte) 0x68, (byte) 0x44, (byte) 0xE0, (byte) 0x4D, (byte) 0x43, (byte) 0x69, (byte) 0x29, (byte) 0x2E, (byte) 0xAC, (byte) 0x15, (byte) 0x59, (byte) 0xA8, (byte) 0x0A, (byte) 0x9E, (byte) 0x6E, (byte) 0x47, (byte) 0xDF, (byte) 0x34, (byte) 0x35, (byte) 0x6A, (byte) 0xCF, (byte) 0xDC, (byte) 0x22, (byte) 0xC9, (byte) 0xC0, (byte) 0x9B, (byte) 0x89, (byte) 0xD4, (byte) 0xED, (byte) 0xAB, (byte) 0x12, (byte) 0xA2, (byte) 0x0D, (byte) 0x52, (byte) 0xBB, (byte) 0x02, (byte) 0x2F, (byte) 0xA9, (byte) 0xD7, (byte) 0x61, (byte) 0x1E, (byte) 0xB4, (byte) 0x50, (byte) 0x04, (byte) 0xF6, (byte) 0xC2, (byte) 0x16, (byte) 0x25, (byte) 0x86, (byte) 0x56, (byte) 0x55, (byte) 0x09, (byte) 0xBE, (byte) 0x91}};

    /**
     * Define the fixed p0/p1 permutations used in keyed S-box lookup.
     * By changing the following constant definitions, the S-boxes will
     * automatically get changed in the Twofish engine.
     */
    private static final int P_00 = 1;
    private static final int P_01 = 0;
    private static final int P_02 = 0;
    private static final int P_03 = P_01 ^ 1;
    private static final int P_04 = 1;
    private static final int P_10 = 0;
    private static final int P_11 = 0;
    private static final int P_12 = 1;
    private static final int P_13 = P_11 ^ 1;
    private static final int P_14 = 0;
    private static final int P_20 = 1;
    private static final int P_21 = 1;
    private static final int P_22 = 0;
    private static final int P_23 = P_21 ^ 1;
    private static final int P_24 = 0;
    private static final int P_30 = 0;
    private static final int P_31 = 1;
    private static final int P_32 = 1;
    private static final int P_33 = P_31 ^ 1;
    private static final int P_34 = 1;

    /**
     * Primitive polynomial for GF(256)
     */
    private static final int GF256_FDBK_2 = 0x169 / 2;
    private static final int GF256_FDBK_4 = 0x169 / 4;

    /**
     * Maximum Distance Separable (MDS) matrix
     */
    protected static final int[][] MDS = new int[4][256]; // blank final
    protected static final int RS_GF_FDBK = 0x14D; // field generator

    /**
     * Precompute the MDS matrix
     */
    static {
        int[] m1 = new int[2];
        int[] mX = new int[2];
        int[] mY = new int[2];
        int i, j;

        // compute the MDS matrix
        for (i = 0; i < 256; i++) {
            j = P[0][i] & 0xFF; // compute all the matrix elements
            m1[0] = j;
            mX[0] = Mx_X(j) & 0xFF;
            mY[0] = Mx_Y(j) & 0xFF;

            j = P[1][i] & 0xFF; // compute all the matrix elements
            m1[1] = j;
            mX[1] = Mx_X(j) & 0xFF;
            mY[1] = Mx_Y(j) & 0xFF;

            MDS[0][i] = m1[P_00] | mX[P_00] << 8 | mY[P_00] << 16 | mY[P_00] << 24;
            MDS[1][i] = mY[P_10] | mY[P_10] << 8 | mX[P_10] << 16 | m1[P_10] << 24;
            MDS[2][i] = mX[P_20] | mY[P_20] << 8 | m1[P_20] << 16 | mY[P_20] << 24;
            MDS[3][i] = mX[P_30] | m1[P_30] << 8 | mY[P_30] << 16 | mX[P_30] << 24;
        }
    }

    /**
     * Linear feedback shift register
     *
     * @param x input bit which is a linear function of its previous state
     * @return output stream from shift register used in pseudo-random generators
     */
    private static int LFSR1(int x) {
        return (x >> 1) ^ ((x & 0x01) != 0 ? GF256_FDBK_2 : 0);
    }

    /**
     * Linear feedback shift register
     *
     * @param x input bit which is a linear function of its previous state
     * @return output stream from shift register used in pseudo-random generators
     */
    private static int LFSR2(int x) {
        return (x >> 2) ^ ((x & 0x02) != 0 ? GF256_FDBK_2 : 0) ^ ((x & 0x01) != 0 ? GF256_FDBK_4 : 0);
    }

    /**
     * x XOR with linear feedback shift register
     *
     * @param x input for XOR operation
     * @return x XORed with linear feedback shift register
     */
    private static int Mx_X(int x) {
        return x ^ LFSR2(x);
    }

    /**
     * x XOR with linear feedback shift register
     *
     * @param x input for double XOR operation
     * @return x XORed with two linear feedback shift registers
     */
    private static int Mx_Y(int x) {
        return x ^ LFSR1(x) ^ LFSR2(x);
    }

    /**
     * Expand a user-supplied key material into a session key.
     *
     * @param k The 64/128/192/256-bit user-key to use.
     * @return This cipher's round keys.
     * @throws InvalidKeyException If the key is invalid.
     */
    public static synchronized Object makeKey(byte[] k) throws InvalidKeyException {
        if (k == null) {
            throw new InvalidKeyException("Key is null");
        }
        int length = k.length;
        if (length != 8 && length != 16 && length != 24 && length != 32) {
            throw new InvalidKeyException("Incorrect key length. Allowed lengths are 8, 16, 24 and 32 bytes");
        }

        int k64Cnt = length / 8;
        int subkeyCnt = ROUND_SUBKEYS + 2 * ROUNDS;
        int[] k32e = new int[4]; // even 32-bit entities
        int[] k32o = new int[4]; // odd 32-bit entities
        int[] sBoxKey = new int[4];

        // split user key material into even/odd 32-bit entities and compute S-box keys
        // using (12,8) Reed-Solomon code over GF(256)
        int i, j, offset = 0;
        for (i = 0, j = k64Cnt - 1; i < 4 && offset < length; i++, j--) {
            k32e[i] = (k[offset++] & 0xFF) | (k[offset++] & 0xFF) << 8 | (k[offset++] & 0xFF) << 16 | (k[offset++] & 0xFF) << 24;
            k32o[i] = (k[offset++] & 0xFF) | (k[offset++] & 0xFF) << 8 | (k[offset++] & 0xFF) << 16 | (k[offset++] & 0xFF) << 24;
            sBoxKey[j] = RS_MDS_Encode(k32e[i], k32o[i]); // reverse order
        }

        // compute round subkeys for PHT. these same subkeys will be used in encryption
        // but will be applied in reverse order in decryption
        int q, A, B;
        int[] subKeys = new int[subkeyCnt];
        for (i = q = 0; i < subkeyCnt / 2; i++, q += SK_STEP) {
            A = F32(k64Cnt, q, k32e); // A uses even key entities
            B = F32(k64Cnt, q + SK_BUMP, k32o); // B uses odd key entities
            B = B << 8 | B >>> 24;
            A += B;
            subKeys[2 * i] = A; // combine with a PHT
            A += B;
            subKeys[2 * i + 1] = A << SK_ROTL | A >>> (32 - SK_ROTL); // rotate and combine
        }

        // fully expand the table for speed
        int k0 = sBoxKey[0];
        int k1 = sBoxKey[1];
        int k2 = sBoxKey[2];
        int k3 = sBoxKey[3];
        int b0, b1, b2, b3;
        int[] sBox = new int[4 * 256];
        for (i = 0; i < 256; i++) {
            b0 = b1 = b2 = b3 = i;
            switch (k64Cnt & 3) {
                case 1:
                    sBox[2 * i] = MDS[0][(P[P_01][b0] & 0xFF) ^ b0(k0)];
                    sBox[2 * i + 1] = MDS[1][(P[P_11][b1] & 0xFF) ^ b1(k0)];
                    sBox[0x200 + 2 * i] = MDS[2][(P[P_21][b2] & 0xFF) ^ b2(k0)];
                    sBox[0x200 + 2 * i + 1] = MDS[3][(P[P_31][b3] & 0xFF) ^ b3(k0)];
                    break;
                case 0:
                    b0 = (P[P_04][b0] & 0xFF) ^ b0(k3);
                    b1 = (P[P_14][b1] & 0xFF) ^ b1(k3);
                    b2 = (P[P_24][b2] & 0xFF) ^ b2(k3);
                    b3 = (P[P_34][b3] & 0xFF) ^ b3(k3);
                case 3:
                    b0 = (P[P_03][b0] & 0xFF) ^ b0(k2);
                    b1 = (P[P_13][b1] & 0xFF) ^ b1(k2);
                    b2 = (P[P_23][b2] & 0xFF) ^ b2(k2);
                    b3 = (P[P_33][b3] & 0xFF) ^ b3(k2);
                case 2: // 128-bit keys (optimize for this case)
                    sBox[2 * i] = MDS[0][(P[P_01][(P[P_02][b0] & 0xFF) ^ b0(k1)] & 0xFF) ^ b0(k0)];
                    sBox[2 * i + 1] = MDS[1][(P[P_11][(P[P_12][b1] & 0xFF) ^ b1(k1)] & 0xFF) ^ b1(k0)];
                    sBox[0x200 + 2 * i] = MDS[2][(P[P_21][(P[P_22][b2] & 0xFF) ^ b2(k1)] & 0xFF) ^ b2(k0)];
                    sBox[0x200 + 2 * i + 1] = MDS[3][(P[P_31][(P[P_32][b3] & 0xFF) ^ b3(k1)] & 0xFF) ^ b3(k0)];
            }
        }

        return new Object[]{sBox, subKeys};
    }

    /**
     * Encrypt exactly one block of plaintext.
     *
     * @param in         The plaintext.
     * @param inOffset   Index of in from which to start considering data. Use when
     *                   decrypting data larger than 1 block (16 bytes)
     *                   to specify, which block is to be decrypted.
     * @param sessionKey The session key to use for encryption.
     * @return The ciphertext generated from a plaintext using the session key.
     */
    public static byte[] blockEncrypt(byte[] in, int inOffset, Object sessionKey) {
        Object[] sk = (Object[]) sessionKey; // extract S-Boxes and subkeys
        int[] sBox = (int[]) sk[0];
        int[] sKey = (int[]) sk[1];

        int x0 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x1 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x2 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x3 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;

        x0 ^= sKey[0];
        x1 ^= sKey[1];
        x2 ^= sKey[2];
        x3 ^= sKey[3];

        int t0, t1;
        int k = ROUND_SUBKEYS;
        for (int R = 0; R < ROUNDS; R += 2) {
            t0 = Fe32(sBox, x0, 0);
            t1 = Fe32(sBox, x1, 3);
            x2 ^= t0 + t1 + sKey[k++];
            x2 = x2 >>> 1 | x2 << 31;
            x3 = x3 << 1 | x3 >>> 31;
            x3 ^= t0 + 2 * t1 + sKey[k++];

            t0 = Fe32(sBox, x2, 0);
            t1 = Fe32(sBox, x3, 3);
            x0 ^= t0 + t1 + sKey[k++];
            x0 = x0 >>> 1 | x0 << 31;
            x1 = x1 << 1 | x1 >>> 31;
            x1 ^= t0 + 2 * t1 + sKey[k++];
        }
        x2 ^= sKey[4];
        x3 ^= sKey[5];
        x0 ^= sKey[6];
        x1 ^= sKey[7];

        byte[] result = new byte[]{(byte) x2, (byte) (x2 >>> 8), (byte) (x2 >>> 16), (byte) (x2 >>> 24), (byte) x3, (byte) (x3 >>> 8), (byte) (x3 >>> 16), (byte) (x3 >>> 24), (byte) x0, (byte) (x0 >>> 8), (byte) (x0 >>> 16), (byte) (x0 >>> 24), (byte) x1, (byte) (x1 >>> 8), (byte) (x1 >>> 16), (byte) (x1 >>> 24),};

        return result;
    }

    /**
     * Decrypt one block of ciphertext.
     *
     * @param in         Ciphertext.
     * @param inOffset   Index of in from which to start considering data. Use when
     *                   decrypting data larger than 1 block (16 bytes)
     *                   to specify, which block is to be decrypted.
     * @param sessionKey The session key to use for decryption.
     * @return The plaintext generated from a ciphertext using the session key.
     */
    public static byte[] blockDecrypt(byte[] in, int inOffset, Object sessionKey) {
        Object[] sk = (Object[]) sessionKey; // extract S-Boxes and subkeys
        int[] sBox = (int[]) sk[0];
        int[] sKey = (int[]) sk[1];

        int x2 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x3 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x0 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;
        int x1 = (in[inOffset++] & 0xFF) | (in[inOffset++] & 0xFF) << 8 | (in[inOffset++] & 0xFF) << 16 | (in[inOffset++] & 0xFF) << 24;

        x2 ^= sKey[4];
        x3 ^= sKey[5];
        x0 ^= sKey[6];
        x1 ^= sKey[7];

        int k = ROUND_SUBKEYS + 2 * ROUNDS - 1;
        int t0, t1;
        for (int R = 0; R < ROUNDS; R += 2) {
            t0 = Fe32(sBox, x2, 0);
            t1 = Fe32(sBox, x3, 3);
            x1 ^= t0 + 2 * t1 + sKey[k--];
            x1 = x1 >>> 1 | x1 << 31;
            x0 = x0 << 1 | x0 >>> 31;
            x0 ^= t0 + t1 + sKey[k--];

            t0 = Fe32(sBox, x0, 0);
            t1 = Fe32(sBox, x1, 3);
            x3 ^= t0 + 2 * t1 + sKey[k--];
            x3 = x3 >>> 1 | x3 << 31;
            x2 = x2 << 1 | x2 >>> 31;
            x2 ^= t0 + t1 + sKey[k--];
        }
        x0 ^= sKey[0];
        x1 ^= sKey[1];
        x2 ^= sKey[2];
        x3 ^= sKey[3];

        byte[] result = new byte[]{(byte) x0, (byte) (x0 >>> 8), (byte) (x0 >>> 16), (byte) (x0 >>> 24), (byte) x1, (byte) (x1 >>> 8), (byte) (x1 >>> 16), (byte) (x1 >>> 24), (byte) x2, (byte) (x2 >>> 8), (byte) (x2 >>> 16), (byte) (x2 >>> 24), (byte) x3, (byte) (x3 >>> 8), (byte) (x3 >>> 16), (byte) (x3 >>> 24),};

        return result;
    }

    /**
     * Takes out of 32 bit number 16 least significant bits
     *
     * @param x 32 input for AND operation
     * @return 16 least significant bits of x
     */
    private static int b0(int x) {
        return x & 0xFF;
    }

    /**
     * Takes out of 32 bit number middle 16 bits
     *
     * @param x 32 bit input for the right shift and AND operation
     * @return middle 16 bits of x
     */
    private static int b1(int x) {
        return (x >>> 8) & 0xFF;
    }

    /**
     * Takes out of 32 bit number 16 most significant bits
     *
     * @param x 32 bit input for the right shift and AND operation
     * @return 16 most significant bits of x
     */
    private static int b2(int x) {
        return (x >>> 16) & 0xFF;
    }

    /**
     * Takes out of 32 bit number 8 most significant bits
     *
     * @param x 32 bit input for the right shift and AND operation
     * @return 8 most significant bits of x
     */
    private static int b3(int x) {
        return (x >>> 24) & 0xFF;
    }

    /**
     * Use (12, 8) Reed-Solomon code over GF(256) to produce a key S-box 32-bit
     * entity from two key material 32-bit entities.
     *
     * @param k0 1st 32-bit entity.
     * @param k1 2nd 32-bit entity.
     * @return Remainder polynomial generated using RS code
     */
    private static int RS_MDS_Encode(int k0, int k1) {
        int r = k1;
        for (int i = 0; i < 4; i++) {
            r = RS_rem(r);
        }
        r ^= k0;
        for (int i = 0; i < 4; i++) {
            r = RS_rem(r);
        }
        return r;
    }

    /**
     * Reed-Solomon code parameters: (12, 8) reversible code:
     *
     * <pre>
     *   g(x) = x**4 + (a + 1/a) x**3 + a x**2 + (a + 1/a) x + 1
     * </pre>
     * <p>
     * where a = primitive root of field generator 0x14D
     */
    private static int RS_rem(int x) {
        int b = (x >>> 24) & 0xFF;
        int g2 = ((b << 1) ^ ((b & 0x80) != 0 ? RS_GF_FDBK : 0)) & 0xFF;
        int g3 = (b >>> 1) ^ ((b & 0x01) != 0 ? (RS_GF_FDBK >>> 1) : 0) ^ g2;
        int result = (x << 8) ^ (g3 << 24) ^ (g2 << 16) ^ (g3 << 8) ^ b;
        return result;
    }

    /**
     * the h-function
     *
     * @param k64Cnt key bit length
     * @param x      32 bit input number
     * @param k32    array of 32-bit entities
     * @return one word output - the new key
     */
    private static int F32(int k64Cnt, int x, int[] k32) {
        int b0 = b0(x);
        int b1 = b1(x);
        int b2 = b2(x);
        int b3 = b3(x);
        int k0 = k32[0];
        int k1 = k32[1];
        int k2 = k32[2];
        int k3 = k32[3];

        int result = 0;
        switch (k64Cnt & 3) {
            case 1:
                result = MDS[0][(P[P_01][b0] & 0xFF) ^ b0(k0)] ^ MDS[1][(P[P_11][b1] & 0xFF) ^ b1(k0)] ^ MDS[2][(P[P_21][b2] & 0xFF) ^ b2(k0)] ^ MDS[3][(P[P_31][b3] & 0xFF) ^ b3(k0)];
                break;
            case 0:
                b0 = (P[P_04][b0] & 0xFF) ^ b0(k3);
                b1 = (P[P_14][b1] & 0xFF) ^ b1(k3);
                b2 = (P[P_24][b2] & 0xFF) ^ b2(k3);
                b3 = (P[P_34][b3] & 0xFF) ^ b3(k3);
            case 3:
                b0 = (P[P_03][b0] & 0xFF) ^ b0(k2);
                b1 = (P[P_13][b1] & 0xFF) ^ b1(k2);
                b2 = (P[P_23][b2] & 0xFF) ^ b2(k2);
                b3 = (P[P_33][b3] & 0xFF) ^ b3(k2);
            case 2:
                result = MDS[0][(P[P_01][(P[P_02][b0] & 0xFF) ^ b0(k1)] & 0xFF) ^ b0(k0)] ^ MDS[1][(P[P_11][(P[P_12][b1] & 0xFF) ^ b1(k1)] & 0xFF) ^ b1(k0)] ^ MDS[2][(P[P_21][(P[P_22][b2] & 0xFF) ^ b2(k1)] & 0xFF) ^ b2(k0)] ^ MDS[3][(P[P_31][(P[P_32][b3] & 0xFF) ^ b3(k1)] & 0xFF) ^ b3(k0)];
                break;
        }
        return result;
    }

    /**
     * s-Boxes XOR
     *
     * @param sBox array of s-Boxes
     * @param x    input 32 bit number
     * @param R    parameter determining which bits should be taken out of x
     * @return XORed specific s-Boxes
     */
    private static int Fe32(int[] sBox, int x, int R) {
        return sBox[2 * whichBits(x, R)] ^ sBox[2 * whichBits(x, R + 1) + 1] ^ sBox[0x200 + 2 * whichBits(x, R + 2)] ^ sBox[0x200 + 2 * whichBits(x, R + 3) + 1];
    }

    /**
     * Method which depending on N takes specific bits out of 32 bit number
     *
     * @param x 32 bit input number
     * @param N parameter determining which bits should be taken out of x
     * @return specific bits out of x
     */
    private static int whichBits(int x, int N) {
        int result = 0;
        switch (N % 4) {
            case 0:
                result = b0(x);
                break;
            case 1:
                result = b1(x);
                break;
            case 2:
                result = b2(x);
                break;
            case 3:
                result = b3(x);
                break;
        }
        return result;
    }
}
