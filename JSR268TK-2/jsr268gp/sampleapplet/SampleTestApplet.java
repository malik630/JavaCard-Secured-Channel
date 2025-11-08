package jsr268gp.sampleapplet;

import javacard.framework.*;
import javacard.security.AESKey;
import javacard.security.KeyBuilder;
import javacard.security.KeyPair;
import javacard.security.MessageDigest;
import javacard.security.RSAPrivateKey;
import javacardx.crypto.Cipher;




public class SampleTestApplet extends Applet {

	// constants declaration // 
    // code of CLA byte in the command APDU header
    final static byte CLA = (byte) 0x80;
    public final static byte INS_INIT = (byte) 0x10;
    public final static byte INS_GET = (byte) 0x11;
    public final static byte INS_SET = (byte) 0x12;
    public final static byte INS_MASK = (byte) 0x13;
    public final static byte INS_DECRYPT = (byte) 0x14;
    public final static byte INS_CONCAT = (byte) 0x15;
    public final static byte VERIF_SIGN = (byte) 0x16;
    public final static byte INS_ENCRYPT_SIGN = (byte) 0x18;
    public final static byte INS_SIGN = (byte) 0x17;
    public final static byte SET_ID = (byte) 0x1B;
    public final static byte GET_ID = (byte) 0x1C;
    public final static byte INS_DEBITER = (byte) 0x1D;
    public final static byte INS_RECHARGER = (byte) 0x1E;
    public final static byte INS_GET_COMPTEUR = (byte) 0x1F;
    public final static byte P1_Y = (byte) 0x01;
    public final static byte P1_P = (byte) 0x02;
    public final static byte P1_G = (byte) 0x03;
    public final static byte P1_S = (byte) 0x04;
    public final static byte P1_K = (byte) 0x05;
    public final static byte P1_Encrypted_Sign = (byte) 0x06;
    public final static byte P1_INIT_WITH_PRIVKEY = (byte) 0x1F;
    public final static byte BLANK = (byte) 0x00;
    
    //Variables
    public DH dh;
    public Transaction transact;
    private final AESKey encKey = (AESKey) KeyBuilder.buildKey(KeyBuilder.TYPE_AES_TRANSIENT_RESET, KeyBuilder.LENGTH_AES_128, false);
    public final byte[] buffer2 = JCSystem.makeTransientByteArray(DH.maxLength, JCSystem.CLEAR_ON_RESET);
    private byte[] clientModulusTab = new byte[128];
    private byte[] clientPrivateExponentTab = new byte[128];
    private byte[] serverPrivateExponentTab = new byte[128];
    private byte[] serverModulusTab = new byte[128];
    private byte[] serverPublicExponentTab = new byte[128];
    private byte[] xBytes = new byte[128];
    private byte[] Sy = new byte[144];
    private byte[] signatureDec = new byte[128];
    private byte[] concat = new byte[256];
    private byte[] signatureClient = new byte[128];
    private byte verif; 
    private byte[] id = new byte[20];
    private final Cipher aesCipher = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_ECB_NOPAD, false);
    private byte[] montantCrypt = new byte[16];
    private byte[] montant = new byte[1];
    private byte[] suiteMessage = {
    		(byte)0x00, (byte)0x01, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
    		(byte)0xFF, (byte)0x00, (byte)0x30, (byte)0x31, (byte)0x30,
    		(byte)0x0D, (byte)0x06, (byte)0x09, (byte)0x60, (byte)0x86,
    		(byte)0x48, (byte)0x01, (byte)0x65, (byte)0x03, (byte)0x04,
    		(byte)0x02, (byte)0x01, (byte)0x05, (byte)0x00, (byte)0x04,
    		(byte)0x20
    };
    protected SampleTestApplet() {
    	register();
    	 // Creates an instance of the DH class and it's variables.
         dh = new DH();
         transact = new Transaction();
    }

    public static void install(byte bArray[], short bOffset, byte bLength)
            throws ISOException {
    	new SampleTestApplet().register();
    }
    
    public void process(APDU apdu) throws ISOException {
    	byte[] buffer = apdu.getBuffer();
        if (selectingApplet()) {
            return;
        }
        
        short offset = 0;
        if (buffer[ISO7816.OFFSET_CLA]!= CLA)
    	   ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
        switch (buffer[ISO7816.OFFSET_INS]) {
        
    
        case (0x01):   
        	short clientModulusLength = apdu.setIncomingAndReceive();
        	Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,clientModulusTab,offset,clientModulusLength);
        return;
        case (0x02):
            Util.arrayCopyNonAtomic(clientModulusTab,(short) 0,buffer,offset,(short) clientModulusTab.length);
            apdu.setOutgoingAndSend((short)0, (short) clientModulusTab.length);
        return;
        case (0x03):
        	short clientPrivateExponentLength = apdu.setIncomingAndReceive();
    	    Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,clientPrivateExponentTab, offset,clientPrivateExponentLength); 	    
        return;
        case (0x04):
        	Util.arrayCopyNonAtomic(clientPrivateExponentTab,(short) 0,buffer,offset,(short) clientPrivateExponentTab.length);
            apdu.setOutgoingAndSend((short)0, (short) clientPrivateExponentTab.length);
        return;
        case (0x05):
        	short serverPrivateExponentLength = apdu.setIncomingAndReceive();
    	    Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,serverPrivateExponentTab,offset,serverPrivateExponentLength);
        return;
        case (0x06):
            Util.arrayCopyNonAtomic(serverPrivateExponentTab,(short) 0,buffer,offset,(short) serverPrivateExponentTab.length);
            apdu.setOutgoingAndSend((short)0, (short) serverPrivateExponentTab.length);
        return;
        case (0x07):
        	short serverModulusLength = apdu.setIncomingAndReceive();
	        Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,serverModulusTab,offset,serverModulusLength);
        return;
        case (0x08):
            Util.arrayCopyNonAtomic(serverModulusTab,(short) 0,buffer,offset,(short) serverModulusTab.length);
            apdu.setOutgoingAndSend((short)0, (short) serverModulusTab.length);
        return;
        case (0x19):
        	short serverPublicExponentLength = apdu.setIncomingAndReceive();
    	    Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA, serverPublicExponentTab,(short) (serverPublicExponentTab.length - serverPublicExponentLength),serverPublicExponentLength); 	    
        return;
        case (0x1A):
        	Util.arrayCopyNonAtomic(serverPublicExponentTab,(short) 0,buffer,offset,(short) serverPublicExponentTab.length);
            apdu.setOutgoingAndSend((short)0, (short) serverPublicExponentTab.length);
        return;
        case INS_INIT:
            if (buffer[ISO7816.OFFSET_P1] == P1_INIT_WITH_PRIVKEY) {
            	short xLength = apdu.setIncomingAndReceive();
            	Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,xBytes,offset,xLength);
                dh.init(xBytes, offset);
            } else {
                dh.init();
            }
            return;
        case INS_GET:
            if (buffer[ISO7816.OFFSET_P1] == P1_Y) {
                apdu.setOutgoing();
                apdu.setOutgoingLength(DH.maxLength);
                dh.getY(buffer, (short) 0);
                apdu.sendBytesLong(buffer, (short) 0, DH.maxLength);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_P) {
                apdu.setOutgoing();
                apdu.setOutgoingLength(DH.maxLength);
                dh.getP(buffer, (short) 0);
                apdu.sendBytesLong(buffer, (short) 0, DH.maxLength);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_G) {
                apdu.setOutgoing();
                apdu.setOutgoingLength(DH.maxLength);
                dh.getG(buffer, (short) 0);
                apdu.sendBytesLong(buffer, (short) 0, DH.maxLength);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_S) {
                apdu.setOutgoing();
                apdu.setOutgoingLength(DH.maxLength);
                dh.getS(buffer, (short) 0);
                apdu.sendBytesLong(buffer, (short) 0, DH.maxLength);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_K) {
                apdu.setOutgoing();
                apdu.setOutgoingLength((short) 16);
                dh.getK(buffer, (short) 0);
                apdu.sendBytesLong(buffer, (short) 0, (short) 16);
            } else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
            return;

        case INS_SET:
            if (buffer[ISO7816.OFFSET_P1] == P1_Y) {
            	apdu.setIncomingAndReceive();
                dh.setY(buffer, ISO7816.OFFSET_CDATA, DH.maxLength, (short) 0);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_P) {
            	apdu.setIncomingAndReceive();
                dh.setP(buffer, ISO7816.OFFSET_CDATA, DH.maxLength, (short) 0);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_G) {
            	apdu.setIncomingAndReceive();
                dh.setG(buffer, ISO7816.OFFSET_CDATA, DH.maxLength, (short) 0);
            } else if (buffer[ISO7816.OFFSET_P1] == P1_Encrypted_Sign) {
            	short SyLength = apdu.setIncomingAndReceive();
            	Util.arrayCopyNonAtomic(buffer, ISO7816.OFFSET_CDATA, Sy, (short) 0, SyLength);
            }
            else {
                ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);
            }
            return;
            
        case INS_MASK:
        	dh.genMaskedKey();
        	return;
            
        case INS_DECRYPT:
        	byte[] bufferGetK = new byte[16];
        	dh.getK(bufferGetK, (short) 0);
        	encKey.setKey(bufferGetK, (short) 0);
        	aesCipher.init(encKey, Cipher.MODE_DECRYPT);
        	aesCipher.doFinal(Sy, (short) 0, (short) signatureDec.length, signatureDec, (short) 0);
        	apdu.setOutgoing();
            apdu.setOutgoingLength((short) signatureDec.length);
            apdu.sendBytesLong(signatureDec, (short) 0, (short) signatureDec.length);
            return;
            
        case INS_CONCAT:
        	byte[] bufferGetY = new byte[128];
        	dh.getY(bufferGetY, offset);
        	Util.arrayCopyNonAtomic(bufferGetY, (short) 0, concat, (short) 0, (short) bufferGetY.length);
        	Util.arrayCopyNonAtomic(xBytes, (short) 0, concat, (short) bufferGetY.length, (short) xBytes.length);
        	apdu.setOutgoing();
            apdu.setOutgoingLength((short) concat.length);
            apdu.sendBytesLong(concat, (short) 0, (short) concat.length);
            return;
            
        case VERIF_SIGN:
        	MessageDigest digest = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        	short hashLen = digest.getLength();
            byte[] hash = new byte[hashLen]; 
            digest.doFinal(concat, (short) 0, (short) concat.length, hash, (short) 0);
        	verif = dh.verifSign(serverPublicExponentTab, hash, signatureDec, serverModulusTab);
        	buffer[0] = verif;
        	apdu.setOutgoingAndSend((short)0, (short)1);
            return;
        case INS_SIGN:
        	MessageDigest digest2 = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);
        	short hashLen2 = digest2.getLength();
            byte[] hash2 = new byte[hashLen2];
            digest2.doFinal(concat, (short) 0, (short) concat.length, hash2, (short) 0);
            byte[] message = new byte[128];
            Util.arrayCopyNonAtomic(suiteMessage, (short) 0, message, (short) 0, (short) suiteMessage.length);
            Util.arrayCopyNonAtomic(hash2, (short)0, message, (short) suiteMessage.length,(short) hash2.length);
            signatureClient = dh.signer(clientPrivateExponentTab, clientModulusTab, message);
            apdu.setOutgoing();
            apdu.setOutgoingLength((short) signatureClient.length);
            apdu.sendBytesLong(signatureClient, (short) 0, (short) signatureClient.length);
            return;
        
        case INS_ENCRYPT_SIGN:
        	byte[] bufferEncGetK = new byte[16];
        	dh.getK(bufferEncGetK, (short) 0);
        	encKey.setKey(bufferEncGetK, (short) 0);
        	aesCipher.init(encKey, Cipher.MODE_ENCRYPT);
        	short ciphertextLen = aesCipher.doFinal(signatureClient, (short) 0, (short) signatureClient.length, buffer, (short) 0);
            apdu.setOutgoing();
            apdu.setOutgoingLength((short) ciphertextLen );
            apdu.sendBytesLong(buffer, (short) 0, (short) ciphertextLen);
            return;
            
        case SET_ID:
        	short idLength = apdu.setIncomingAndReceive();
        	Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,id,(byte)(id.length - idLength),idLength);
        	return;
        
        case GET_ID:
        	Util.arrayCopyNonAtomic(id,(short) 0,buffer,offset,(short) id.length);
            apdu.setOutgoingAndSend((short)0, (short) id.length);
            return;
             
        case INS_DEBITER:
        	short encMontantLength = apdu.setIncomingAndReceive();
        	Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,montantCrypt,offset,encMontantLength);
        	byte[] bufferK = new byte[16];
        	dh.getK(bufferK, (short) 0);
        	encKey.setKey(bufferK, (short) 0);
        	aesCipher.init(encKey, Cipher.MODE_DECRYPT);
        	aesCipher.doFinal(montantCrypt, (short) 0, (short) montant.length, montant, (short) 0);
        	if(montant[0] > transact.getCompteur()){
        		transact.debiter(montant);
        	}
        	return;
        	
        case INS_RECHARGER:
        	short montantLength = apdu.setIncomingAndReceive();
        	Util.arrayCopyNonAtomic(buffer,(short)ISO7816.OFFSET_CDATA,montantCrypt,offset,montantLength);
        	byte[] bufferK2 = new byte[16];
        	dh.getK(bufferK2, (short) 0);
        	encKey.setKey(bufferK2, (short) 0);
        	aesCipher.init(encKey, Cipher.MODE_DECRYPT);
        	aesCipher.doFinal(montantCrypt, (short) 0, (short) montant.length, montant, (short) 0);
            transact.debiter(montant);
        	return;
        	
        case INS_GET_COMPTEUR:
        	byte[] bufferGetK2 = new byte[16];
        	dh.getK(bufferGetK2, (short) 0);
        	encKey.setKey(bufferGetK2, (short) 0);
        	aesCipher.init(encKey, Cipher.MODE_ENCRYPT);
        	montant[0] = transact.getCompteur();
        	short encmontantLen = aesCipher.doFinal(montant, (short) 0, (short) montantCrypt.length, buffer, (short) 0);
            apdu.setOutgoing();
            apdu.setOutgoingLength((short) encmontantLen );
            apdu.sendBytesLong(buffer, (short) 0, (short) encmontantLen);
        	
        
        	
        default:
        	ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
            return;
        }
       
    }
    
}

class DH {
private RSAPrivateKey dhPriv;
   private Cipher dhCipher;
   private RSAPrivateKey signPriv;
   private Cipher signCipher;

   private byte[] P = {
		   (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		   (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xC9, (byte)0x0F,
		   (byte)0xDA, (byte)0xA2, (byte)0x21, (byte)0x68, (byte)0xC2,
		   (byte)0x34, (byte)0xC4, (byte)0xC6, (byte)0x62, (byte)0x8B,
		   (byte)0x80, (byte)0xDC, (byte)0x1C, (byte)0xD1, (byte)0x29,
		   (byte)0x02, (byte)0x4E, (byte)0x08, (byte)0x8A, (byte)0x67,
		   (byte)0xCC, (byte)0x74, (byte)0x02, (byte)0x0B, (byte)0xBE,
		   (byte)0xA6, (byte)0x3B, (byte)0x13, (byte)0x9B, (byte)0x22,
		   (byte)0x51, (byte)0x4A, (byte)0x08, (byte)0x79, (byte)0x8E,
		   (byte)0x34, (byte)0x04, (byte)0xDD, (byte)0xEF, (byte)0x95,
		   (byte)0x19, (byte)0xB3, (byte)0xCD, (byte)0x3A, (byte)0x43,
		   (byte)0x1B, (byte)0x30, (byte)0x2B, (byte)0x0A, (byte)0x6D,
		   (byte)0xF2, (byte)0x5F, (byte)0x14, (byte)0x37, (byte)0x4F,
		   (byte)0xE1, (byte)0x35, (byte)0x6D, (byte)0x6D, (byte)0x51,
		   (byte)0xC2, (byte)0x45, (byte)0xE4, (byte)0x85, (byte)0xB5,
		   (byte)0x76, (byte)0x62, (byte)0x5E, (byte)0x7E, (byte)0xC6,
		   (byte)0xF4, (byte)0x4C, (byte)0x42, (byte)0xE9, (byte)0xA6,
		   (byte)0x37, (byte)0xED, (byte)0x6B, (byte)0x0B, (byte)0xFF,
		   (byte)0x5C, (byte)0xB6, (byte)0xF4, (byte)0x06, (byte)0xB7,
		   (byte)0xED, (byte)0xEE, (byte)0x38, (byte)0x6B, (byte)0xFB,
		   (byte)0x5A, (byte)0x89, (byte)0x9F, (byte)0xA5, (byte)0xAE,
		   (byte)0x9F, (byte)0x24, (byte)0x11, (byte)0x7C, (byte)0x4B,
		   (byte)0x1F, (byte)0xE6, (byte)0x49, (byte)0x28, (byte)0x66,
		   (byte)0x51, (byte)0xEC, (byte)0xE6, (byte)0x53, (byte)0x81,
		   (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
		   (byte)0xFF, (byte)0xFF, (byte)0xFF
  
   };

   public static final short maxLength = 128;
   private byte[] G = new byte[maxLength];
   private byte[] Y = JCSystem.makeTransientByteArray(maxLength, JCSystem.CLEAR_ON_RESET);
   private byte[] S = JCSystem.makeTransientByteArray(maxLength, JCSystem.CLEAR_ON_RESET);
   private byte[] m = JCSystem.makeTransientByteArray(maxLength, JCSystem.CLEAR_ON_RESET);
   private byte[] K = JCSystem.makeTransientByteArray((short)16, JCSystem.CLEAR_ON_RESET);
   public DH() {
       // Creates a RSA private key instance as template for the DH private key
       dhPriv = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, KeyBuilder.LENGTH_RSA_1024, false);
       
       // Creates a RSA private key instance for the signature
       signPriv = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, KeyBuilder.LENGTH_RSA_1024, false);

       // Creates an RSA cipher instance
       dhCipher = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);
       
       // Creates an RSA cipher instance for the signature
       signCipher = Cipher.getInstance(Cipher.ALG_RSA_NOPAD, false);

       // Set default G to 2
       G[(short) (maxLength - 1)] = (byte) 0x02;

   }

   public void init() {
	      
	    // Create a keypair instance using an RSA keypair as template

	       KeyPair dhKeyPair = new KeyPair(KeyPair.ALG_RSA, (short) dhPriv.getSize());

	       // Gen DH private key
	       dhKeyPair.genKeyPair();
	       dhPriv = (RSAPrivateKey) dhKeyPair.getPrivate();
	       
	       // Get Private Exponent
	       dhPriv.getExponent(m, (short)0) ;

       // Load DH's P as RSA's M
       dhPriv.setModulus(P, (short) 0, maxLength);
       
       // Set private key into cipher
       dhCipher.init(dhPriv, Cipher.MODE_DECRYPT);

       // Execute Y = G^bobPrivKey mod P via RSA's decrypt
       dhCipher.doFinal(G, (short) 0, maxLength, Y, (short) 0);
       
   }

   public void init(byte[] privateKey, short offset) {
       // Load DH's P as RSA's M
       dhPriv.setModulus(P, (short) 0, maxLength);
       
       // Load DH's m as RSA's d
       dhPriv.setExponent(m, (short) 0, maxLength);

       // Set private key into cipher
       dhCipher.init(dhPriv, Cipher.MODE_DECRYPT);

       // Execute Y = G^bobPrivKey mod P via RSA's decrypt
       dhCipher.doFinal(privateKey, offset, maxLength, S, (short) 0);
   }

   public void getG(byte[] output, short offset) {
	   Util.arrayCopyNonAtomic(G, (short) 0, output, offset, maxLength);
   }

    // Get P value
   
   public void getP(byte[] output, short offset) {
	   Util.arrayCopyNonAtomic(P, (short) 0, output, offset, maxLength);
   }

    // Get Y value
   public void getY(byte[] output, short offset) {
	   Util.arrayCopyNonAtomic(Y, (short) 0, output, offset, maxLength);
   }

    // Get S value
   public void getS(byte[] output, short offset) {
	   Util.arrayCopyNonAtomic(S, (short) 0, output, offset, maxLength);
   }
   
   // Get K value
   public void getK(byte[] output, short offset) {
	   Util.arrayCopyNonAtomic(K, (short) 0, output, offset, (short) 16);
   }
   
    // Set Y value
   public void setY(byte[] data, short offset, short length, short yOffset) {
       Util.arrayCopyNonAtomic(data, offset, Y, yOffset, length);
   }

    // Set P value
   
   public void setP(byte[] data, short offset, short length, short pOffset) {
	   Util.arrayCopyNonAtomic(data, offset, P, pOffset, length);
   }

    // Set G value
       public void setG(byte[] data, short offset, short length, short gOffset) {
    	   Util.arrayCopyNonAtomic(data, offset, G, gOffset, length);
   }

    // Destroys DH private key
       
   public void clearKey() {
       dhPriv.clearKey();
   }
   
// Fonction MGF1
   public static void maskFunction(byte[] mgfSeed, short maskLen, byte[] maskedKey, short offset) {
       MessageDigest digest = MessageDigest.getInstance(MessageDigest.ALG_SHA_256, false);

       byte[] mask = new byte[maskLen];
       byte[] counterBytes = new byte[4]; // Supposant que le compteur soit codé sur 4 octets
       Util.arrayCopy(mgfSeed, (short) 0, counterBytes, (short)0, (short) counterBytes.length);
       byte[] concat = new byte[(short) (mgfSeed.length+counterBytes.length)];
       short counter = 0;
       short hashLen = digest.getLength();
       byte[] hash = new byte[hashLen];
       while (maskLen > 0) {
    	   counterBytes[0] = (byte) (counter >>> 24);
           counterBytes[1] = (byte) (counter >>> 16);
           counterBytes[2] = (byte) (counter >>> 8);
           counterBytes[3] = (byte) counter;
           digest.reset();
           Util.arrayCopy(mgfSeed, (short) 0, concat, (short)0, (short) mgfSeed.length);
           Util.arrayCopy(counterBytes, (short) 0, concat, (short)mgfSeed.length, (short) counterBytes.length);
           digest.update(concat, (short)0, (short) concat.length); 
           digest.doFinal(concat, (short) 0, (short) concat.length, hash, (short) 0);

           if (maskLen > hashLen) {
               Util.arrayCopy(hash, (short) 0, mask, (short) (mask.length - maskLen), hashLen);
           } else {
               Util.arrayCopy(hash, (short) 0, mask, (short) (mask.length - maskLen), maskLen);
           }

           maskLen -= hashLen;
           counter++;
       }
       Util.arrayCopyNonAtomic(mask, (short) 0, maskedKey, offset, (short) mask.length);
   }
   
   byte verifSign(byte[] publicExponent, byte[] message, byte[] signature, byte[] modulus){
	   byte verif = (byte) 0x01;
	   byte[] messageToCompare = new byte[128];
		  // Create a keypair instance using an RSA keypair as template

	       KeyPair signKeyPair = new KeyPair(KeyPair.ALG_RSA, (short) signPriv.getSize());

	       // Gen signature private key
	       signKeyPair.genKeyPair();
	       signPriv = (RSAPrivateKey) signKeyPair.getPrivate();
		   signPriv.setExponent(publicExponent, (short)0, maxLength);
		   signPriv.setModulus(modulus, (short)0, maxLength);
		   
		  // Set private key into cipher
	       signCipher.init(signPriv, Cipher.MODE_DECRYPT);

	       // Execute messageToCompare = signature^publicExponent mod modulus via RSA's decrypt
	       signCipher.doFinal(signature, (short) 0, maxLength, messageToCompare, (short) 0);
	       short i = 0 ;
	       
	       // Compare if the message and the messageToCompare are equal
       while(i < message.length && verif == (byte) 0x01) {
           if (messageToCompare[(short)(messageToCompare.length - message.length + i)] != message[i]) {
               verif = (byte) 0x00;
           }
           i++;
       }
       
       return verif;
   }
   
   byte[] signer(byte[] privateExponent, byte[] clientModulus, byte[] message){
	   byte[] signature = new byte[128];
	   KeyPair signKeyPair = new KeyPair(KeyPair.ALG_RSA, (short) signPriv.getSize());

       // Gen signature private key
       signKeyPair.genKeyPair();
       signPriv = (RSAPrivateKey) signKeyPair.getPrivate();
	   signPriv.setExponent(privateExponent, (short)0, maxLength);
	   signPriv.setModulus(clientModulus, (short)0, maxLength);
	   
	  // Set private key into cipher
       signCipher.init(signPriv, Cipher.MODE_DECRYPT);

       // Execute signature = message^privateExponent mod clientModulus via RSA's decrypt
       signCipher.doFinal(message, (short) 0, (short) message.length, signature, (short) 0);
       return signature;
   }
   
   public void genMaskedKey(){
	   maskFunction(S, (short) 16, K, (short) 0);
   }

}

 class Utils {
 public static void zeroize(byte[] data) {
       data[0] = (byte) 0x00;
       Util.arrayCopyNonAtomic(data, (short) 0, data, (short) 0, (short) data.length);
   }

}
 
 class Transaction{
	 private byte compteur;
	 
	 public byte debiter(byte montant[]){
		 this.compteur = (byte)(compteur - montant[0]);
		 return compteur;
	 }
	 public byte recharger(byte[] montant){
		 this.compteur = (byte)(compteur + montant[0]);
		 return compteur;
	 }
	 public byte getCompteur(){
		 return this.compteur;
	 }
 }

