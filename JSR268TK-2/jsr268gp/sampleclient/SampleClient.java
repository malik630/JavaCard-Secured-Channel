package jsr268gp.sampleclient;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;







import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.smartcardio.ATR;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import jsr268gp.util.Util;

import javax.swing.table.DefaultTableModel;




class Bienvenue extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private String action = "none";
    
    

    public Bienvenue(final String id, final Socket soc, final BufferedReader in, final PrintWriter out, final CardChannel canal, final CommandAPDU commande, final ResponseAPDU reponse, final byte[] serverPublicExponentTab, final byte[] sessionKey) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1078, 531);
        getContentPane().setBackground(new Color(137, 162, 233));
        getContentPane().setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Que voulez-vous faire ?");
        lblNewLabel_1.setBounds(72, 77, 155, 14);
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.PLAIN, 15));
        getContentPane().add(lblNewLabel_1);

        

        out.println(action);
        
        JButton afficherbutton = new JButton("Afficher mon crédit");
        afficherbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ajouter ici le code pour afficher le crédit (sans la base de données)
            	action = "Afficher mon crédit";
            	out.println(action);            	
            	afficherCredit(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab, sessionKey);
            	
            }
        });
        afficherbutton.setBounds(38, 113, 193, 23);
        getContentPane().add(afficherbutton);

        JButton btnNewButton_1 = new JButton("Afficher mon historique");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	action = "Afficher mon historique";
            	out.println(action);
            	try {
					afficherHistorique(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnNewButton_1.setBounds(36, 147, 192, 23);
        getContentPane().add(btnNewButton_1);

        JButton btnNewButton_2 = new JButton("Recharger mon crédit");
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	action = "Recharger mon crédit";
            	out.println(action);
            	try {
					rechargerCredit(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab, sessionKey);
				} catch (InvalidKeyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CardException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnNewButton_2.setBounds(38, 181, 190, 23);
        getContentPane().add(btnNewButton_2);

        JButton btnNewButton = new JButton("Débiter");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	action = "Débiter";
            	out.println(action);
            	try {
					debiterCredit(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab,sessionKey);
				} catch (InvalidKeyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchAlgorithmException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalBlockSizeException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (BadPaddingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CardException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        btnNewButton.setBounds(38, 215, 193, 23);
        getContentPane().add(btnNewButton);

        JPanel panel = new JPanel();
        panel.setBounds(38, 269, 193, 115);
        getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel_2 = new JLabel("Combien?");
        lblNewLabel_2.setBounds(10, 11, 66, 14);
        panel.add(lblNewLabel_2);

        textField = new JTextField();
        textField.setBounds(67, 41, 96, 20);
        panel.add(textField);
        textField.setColumns(10);

        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(0, 64, 128));
        panel_1.setBounds(871, 71, 170, 95);
        getContentPane().add(panel_1);
        panel_1.setLayout(null);

        JPanel panel_2 = new JPanel();
        panel_2.setBounds(143, 69, 21, 14);
        panel_1.add(panel_2);
        panel_2.setBackground(new Color(231, 214, 3));

        JLabel nomcarte = new JLabel("");
        nomcarte.setForeground(Color.WHITE);
        nomcarte.setBounds(15, 69, 69, 20);
        panel_1.add(nomcarte);

        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(0, 0, 160));
        panel_3.setBounds(0, 0, 1056, 55);
        getContentPane().add(panel_3);
        panel_3.setLayout(null);

        JLabel lblNewLabel_3 = new JLabel("ESI");
        lblNewLabel_3.setForeground(Color.WHITE);
        lblNewLabel_3.setBounds(927, 6, 60, 39);
        panel_3.add(lblNewLabel_3);
        lblNewLabel_3.setFont(new Font("Palatino Linotype", Font.BOLD, 28));

        JLabel lblNewLabel = new JLabel("Bienvenue");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(392, 6, 184, 44);
        panel_3.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Perpetua Titling MT", Font.BOLD, 26));

        JLabel lblNewLabel_5 = new JLabel("Banque");
        lblNewLabel_5.setBounds(963, 25, 78, 23);
        panel_3.add(lblNewLabel_5);
        lblNewLabel_5.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
        lblNewLabel_5.setForeground(new Color(0, 128, 192));
    }

	protected void debiterCredit(String id, Socket soc, BufferedReader in, PrintWriter out, CardChannel canal, CommandAPDU commande, ResponseAPDU reponse, byte[] serverPublicExponentTab, byte[] sessionKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, CardException {
		// TODO Auto-generated method stub
		String montant = textField.getText();
		// Création d'une clé AES avec la clé masquée K obtenue :
		SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

		// Créer une instance de Cipher pour le chiffrement AES
		Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

		// Initialiser le Cipher en mode de chiffrement avec la clé
		
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		// Chiffrer la signature obtenue :
		byte[] rechargeCredit = cipher.doFinal(montant.getBytes());
		commande = new CommandAPDU((byte) 0x80,(byte)0x1D,(byte)0x00,(byte)0x00, rechargeCredit);
		reponse = canal.transmit(commande);
		System.out.println("Reponse SELECT2 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
		out.println(bytesToBigInteger(rechargeCredit));
		
		
	}

	protected void rechargerCredit(String id, Socket soc, BufferedReader in, PrintWriter out, CardChannel canal, CommandAPDU commande, ResponseAPDU reponse, byte[] serverPublicExponentTab, byte[] sessionKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, CardException {
		// TODO Auto-generated method stub
		String montant = textField.getText();
		
		// Création d'une clé AES avec la clé masquée K obtenue :
		SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

        // Créer une instance de Cipher pour le chiffrement AES
        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

        // Initialiser le Cipher en mode de chiffrement avec la clé
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Chiffrer la signature obtenue :
        byte[] rechargeCredit = cipher.doFinal(montant.getBytes());
        commande = new CommandAPDU((byte) 0x80,(byte)0x1E,(byte)0x00,(byte)0x00, rechargeCredit);
        reponse = canal.transmit(commande);
        System.out.println("Reponse SELECT2 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
        out.println(bytesToBigInteger(rechargeCredit));
		
	}

	protected void afficherHistorique(String id, Socket soc, BufferedReader in, PrintWriter out, CardChannel canal, CommandAPDU commande, ResponseAPDU reponse, byte[] serverPublicExponentTab) throws IOException {
		// TODO Auto-generated method stub
		// Créer un modèle de tableau pour stocker les transactions
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID Transaction");
        model.addColumn("Type");
        model.addColumn("Montant");
        model.addColumn("Date et Heure");
        model.addColumn("Nouveau Crédit");
		while (true) {
	        // Read a line of data (assuming each field is separated by newline)
	        String line = in.readLine();

	        // Check if we reached the end of the stream (null indicates closed connection)
	        if (line == null) {
	          break;
	        }

	        // Process the received line
	        int idTransaction;
	        String type;
	        int montant;
	        String dateHeure;
	        int nouveauCredit;

	        try {
	          // Parse the line based on field order sent by the server
	          idTransaction = Integer.parseInt(line);
	          line = in.readLine(); // Read next line for type
	          type = line;
	          line = in.readLine(); // Read next line for montant
	          montant = Integer.parseInt(line);
	          line = in.readLine(); // Read next line for dateHeure (might need custom parsing)
	          dateHeure = in.readLine(); // Assuming server sends timestamps in a compatible format
	          line = in.readLine(); // Read next line for nouveauCredit
	          nouveauCredit = Integer.parseInt(line);
	        } catch (NumberFormatException e) {
	          // Handle potential parsing errors (e.g., invalid integer format)
	          System.err.println("Error parsing data: " + e.getMessage());
	          continue;
	        }
	        model.addRow(new Object[]{idTransaction, type, montant, dateHeure, nouveauCredit});
		}
		 // Créer une JTable avec le modèle de tableau
        JTable table = new JTable(model);

        // Ajouter la JTable à un JScrollPane pour permettre le défilement si nécessaire
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(250, 150, 600, 200);
        contentPane.add(scrollPane);
		
	}

	protected void afficherCredit(String id, Socket soc, BufferedReader in, PrintWriter out, CardChannel canal, CommandAPDU commande, ResponseAPDU reponse, byte[] serverPublicExponentTab, byte[] sessionKey) {
		// TODO Auto-generated method stub
		try {
		 Component[] components = contentPane.getComponents();
         for (Component component : components) {
             if (component instanceof JLabel) {
                 String text = ((JLabel) component).getText();
                 if (text.startsWith("Votre crédit est")) {
                     contentPane.remove(component);
                     break; // Sortir de la boucle dès que le label est supprimé
                 }
             }
         }
                   SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

	               // Créer une instance de Cipher pour le chiffrement AES
	               Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
	               // Initialiser le Cipher en mode déchiffrement avec la clé
	               cipher.init(Cipher.DECRYPT_MODE, secretKey);
	               
	               commande = new CommandAPDU((byte) 0x80,(byte)0x1F,(byte)0x00,(byte)0x00, (byte)0x7F);
	               reponse = canal.transmit(commande);
	               System.out.println("Reponse SELECT2 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
	               byte[] creditBytes = reponse.getData();
				   String encCredit = in.readLine();
				   byte[] decCredit = cipher.doFinal(encCredit.getBytes());
  			       String creditStr = byteArrayToString(decCredit);
				   int credit = Integer.parseInt(creditStr);
					
					
					if(credit == creditBytes[0]){
					       JLabel lblCredit = new JLabel("Votre crédit est : " + credit);
                           lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 18));
                           lblCredit.setBounds(244, 79, 189, 45);
                           contentPane.add(lblCredit);
                           contentPane.revalidate();
                           contentPane.repaint();
					}
					//else une erreur est survenue
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public static String byteArrayToString(byte[] bytes) {

        // Initialise une chaîne de caractères vide

        StringBuilder sb = new StringBuilder();

        int indicateur = 0;

        // Boucle à travers chaque byte du tableau

        for (byte b : bytes) {
        	if(b != 0){
        		indicateur = 1;
        	}

            // Ajoute chaque byte converti en caractère à la chaîne
            if(indicateur == 1){
                sb.append((char) b);
            }

        }

        

        // Retourne la chaîne de caractères résultante

        return sb.toString();

    }
    
	 public static BigInteger bytesToBigInteger(byte[] byteArray) {
	        // Si le tableau de bytes est vide, retourner BigInteger.ZERO
	        if (byteArray.length == 0) {
	            return BigInteger.ZERO;
	        }
	        // Convertir le tableau de bytes en un BigInteger
	        return new BigInteger(1, byteArray); // Le "1" signifie que le tableau de bytes est positif ou nul
	    }
}

class Forms {

	public static byte[] bigIntegerToBytes(BigInteger bigInteger) {
    byte[] byteArray = bigInteger.toByteArray();
    // Si le tableau a une longueur égale à 1 et que la première valeur est égale à 0, c'est-à-dire que le BigInteger est zéro ou négatif, nous devons supprimer le signe.
    if (byteArray.length > 1 && byteArray[0] == 0) {
        byte[] trimmedArray = new byte[byteArray.length - 1];
        System.arraycopy(byteArray, 1, trimmedArray, 0, trimmedArray.length);
        return trimmedArray;
    }
    return byteArray;
}

public static BigInteger bytesToBigInteger(byte[] byteArray) {
    // Si le tableau de bytes est vide, retourner BigInteger.ZERO
    if (byteArray.length == 0) {
        return BigInteger.ZERO;
    }
    // Convertir le tableau de bytes en un BigInteger
    return new BigInteger(1, byteArray); // Le "1" signifie que le tableau de bytes est positif ou nul
}

public Forms(String userId, Socket soc, BufferedReader in, PrintWriter out, CardChannel canal, CommandAPDU commande, ResponseAPDU reponse, byte[] serverPublicExponentTab) {
    try {
    	
        String action = in.readLine();
        while (!(action.equals(null))) {
        	action = in.readLine();
        	if (action.equals("saved")) { // personalize
                //personalisation
                String id = in.readLine();
                byte[] idBytes = id.getBytes();
                // Génération des clés RSA du client sur 1024 :
                int keySize = 1024;

                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(keySize);

                KeyPair keyPair = keyPairGenerator.generateKeyPair();

                // Initialisation de la clé privée et publique :
                RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
                RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();

                // Envoi au serveur de la clé privée et publique du client :
                BigInteger privateMod = rsaPrivateKey.getModulus();
                BigInteger publicMod = rsaPublicKey.getModulus();
                out.println(rsaPublicKey.getModulus());
                System.out.println("Client public Modulus: " + in.readLine());
                BigInteger privateExpo = rsaPrivateKey.getPrivateExponent();
                BigInteger publicExpo = rsaPublicKey.getPublicExponent();
                out.println(rsaPublicKey.getPublicExponent());
                System.out.println("Client public Exponent: " + in.readLine());

                 byte[] clientModulusTab = bigIntegerToBytes(privateMod);
    	         byte[] clientPubExponentTab = bigIntegerToBytes(publicExpo);
    	         byte[] clientPrvExponentTab = bigIntegerToBytes(privateExpo);
    	         commande = new CommandAPDU((byte) 0x80,(byte)0x01,(byte)0x00,(byte)0x00,clientModulusTab);
    	         System.out.println("Commande2 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
    	         reponse = canal.transmit(commande);
    	         System.out.println("Reponse2 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
    	         commande = new CommandAPDU((byte) 0x80,(byte)0x02,(byte)0x00,(byte)0x00,0x7F);
    	         reponse = canal.transmit(commande);
    	         System.out.println("Reponse SELECT2 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    	         commande = new CommandAPDU((byte)0x80,(byte)0x03,(byte)0x00,(byte)0x00,clientPrvExponentTab);
    	         System.out.println("Commande3 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
    	         reponse = canal.transmit(commande);
    	         System.out.println("Reponse3 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
    	         commande = new CommandAPDU((byte)0x80,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x7F);
    	         reponse = canal.transmit(commande);
    	         System.out.println("Reponse SELECT3 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    	         commande = new CommandAPDU((byte) 0x80,(byte)0x1B,(byte)0x00,(byte)0x00,idBytes);
                 System.out.println("Commande2 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
                 reponse = canal.transmit(commande);
                 System.out.println("Reponse2 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
      
            }
        }
        
    } catch (Exception e) {
        e.printStackTrace();
    }
}


   
}

public class SampleClient extends JFrame {

	private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField Nom;
    private JRadioButton adminButton;
    private JRadioButton userButton;
    private JLabel Info;
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JPasswordField PIN;
    int attempts = 0;


    public SampleClient() {
    	setTitle("Interface de connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 706, 449);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(137, 162, 233));
        panel.setBounds(0, 0, 693, 412);
        contentPane.add(panel);
        panel.setLayout(null);

        adminButton = new JRadioButton("Admin");
        adminButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        adminButton.setBackground(new Color(137, 162, 233));
        adminButton.setBounds(347, 216, 157, 54);
        panel.add(adminButton);
        buttonGroup.add(adminButton);

        userButton = new JRadioButton("User");
        userButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        userButton.setBackground(new Color(137, 162, 233));
        userButton.setBounds(530, 211, 157, 64);
        panel.add(userButton);
        buttonGroup.add(userButton);
        
        PIN = new JPasswordField();
        PIN.setColumns(10);
        PIN.setBounds(446, 173, 129, 31);
        panel.add(PIN);
        
        Nom = new JTextField();
        Nom.setBounds(446, 112, 129, 31);
        panel.add(Nom);
        Nom.setColumns(10);
        
        JLabel lblNewLabel = new JLabel("I D :");
        lblNewLabel.setForeground(new Color(255, 255, 255));
        lblNewLabel.setBounds(325, 115, 66, 25);
        panel.add(lblNewLabel);
        lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        
        // Ajout d'un label
        JLabel lblNewLabel_5 = new JLabel("banque");
        lblNewLabel_5.setForeground(new Color(0, 64, 128));
        lblNewLabel_5.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
        lblNewLabel_5.setBounds(884, 22, 78, 23);
        
        // Ajout d'un label
        JLabel lblNewLabel_3_1 = new JLabel("ESI");
        lblNewLabel_3_1.setForeground(Color.WHITE);
        lblNewLabel_3_1.setFont(new Font("Palatino Linotype", Font.BOLD, 28));
        lblNewLabel_3_1.setBounds(902, 0, 60, 39);
       

        JButton ok = new JButton("OK");
        ok.setFont(new Font("Times New Roman", Font.BOLD, 15));
        ok.setBackground(SystemColor.activeCaption);
        ok.setForeground(new Color(0, 0, 0));
        ok.setBounds(416, 271, 119, 38);
        panel.add(ok);
        
        JPanel panel_1 = new JPanel();
        panel_1.setBackground(new Color(0, 128, 128));
        panel_1.setBounds(0, 0, 280, 412);
        panel.add(panel_1);
        panel_1.setLayout(null);
        
        JLabel lblNewLabel_2 = new JLabel("");
        //ImageIcon img = new ImageIcon(this.getClass().getResource("/im.jpg"));
        //lblNewLabel_2.setIcon(img);
        lblNewLabel_2.setBounds(0, 0, 290, 447);
        panel_1.add(lblNewLabel_2);
        
        JLabel lblNewLabel_1 = new JLabel("mot de passe:");
        lblNewLabel_1.setForeground(new Color(255, 255, 255));
        lblNewLabel_1.setBounds(325, 179, 111, 17);
        panel.add(lblNewLabel_1);
        lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 18));
        
        JLabel Info = new JLabel("");
        Info.setFont(new Font("Tahoma", Font.PLAIN, 17));
        Info.setBounds(295, 352, 376, 14);
        panel.add(Info);
        Info.setForeground(new Color(255, 0, 0));
        
        JLabel lblNewLabel_3 = new JLabel("Log In\r\n");
        lblNewLabel_3.setForeground(Color.BLUE);
        lblNewLabel_3.setFont(new Font("Times New Roman", Font.BOLD, 24));
        lblNewLabel_3.setBounds(444, 32, 72, 64);
        panel.add(lblNewLabel_3);
        
        JLabel lblNewLabel_6 = new JLabel("Merci de votre confiance");
        lblNewLabel_6.setBounds(410, 325, 175, 20);
        panel.add(lblNewLabel_6);

        // Gestion de l'événement du bouton OK
        ok.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                handleOkButton();
            }
        });

    }

    private void handleOkButton() {
        // Initialisation de la communication avec le client
        System.out.println("Server waiting for client");
        TerminalFactory tf = TerminalFactory.getDefault();
    	CardTerminals list = tf.terminals();
    	//CardTerminal cad = list.getTerminal("USB CCID Smart Card Reader 0");
    	//CardTerminal cad = list.getTerminal("ACS ACR1281U 0");
    	CardTerminal cad = list.getTerminal("ACS ACR1281 1S Dual Reader ICC 0");
        try {
        	
        	
        	//Etablir la connexion avec la carte   puce
        	Card c = cad.connect("T=0");
        	System.out.println("Card: "+c);

        	//Afficher l'ATR et sa taille (reset the card)
        	ATR atr = c.getATR();
        	System.out.println("ATR: "+Util.byteArrayToHexString(atr.getBytes(), " ")+"\n");

        	//Ouverture d'un canal de communication
        	CardChannel canal = c.getBasicChannel();

        	//Select de l'applet
        	                                                                                                                   
        	   
        	CommandAPDU commande = new CommandAPDU(new byte[]{(byte) 0x00, (byte) 0xA4, (byte) 0x04,(byte) 0x00,(byte) 0x08,(byte) 0xA0,(byte) 0x00,(byte) 0x00,(byte) 0x00,(byte) 0x04,(byte) 0x00,(byte) 0x00, (byte) 0x02, (byte)0x7F});

        	                                                                                                                     
        	ResponseAPDU reponse = canal.transmit(commande);
        	System.out.println("Reponse SELECT1 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
        	   System.out.println("Reponse SELECT1 : "+reponse+"\n");

        	   
        	   
        	   
        	   
        	   
        	   ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
            System.out.println("Client started");
            Socket soc = new Socket("localhost", 9803);
            PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            String id = Nom.getText();
            String password = new String(PIN.getPassword());
            String userType= null;
            if(adminButton.isSelected()) {
            	userType = "admin";
            }else {
            	 if(userButton.isSelected()) {
                 	userType = "user";
                 }
            }

            // Envoi des données au serveur
            out.println(id);
            System.out.println("ID: " + in.readLine());
            out.println(userType);
            String type = in.readLine();
            System.out.println("User Type: " + type);
            out.println(password);
            String userStatut = in.readLine();
            String userPin = in.readLine();
            
            if(userPin.equals( hashPassword(password))) {
            	if (type.equals("admin")) {
                    // Génération des paires de clés RSA du serveur (1024 bits)
                    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
                    keyPairGen.initialize(1024);
                    KeyPair serverKeyPair = keyPairGen.generateKeyPair();
                    RSAPublicKey serverPublicKey = (RSAPublicKey) serverKeyPair.getPublic();
                    RSAPrivateKey serverPrivateKey = (RSAPrivateKey) serverKeyPair.getPrivate();
                    
                    
                    
                
                    
                    // Envoi de la clé publique au client
                    String str = serverPublicKey.getModulus().toString();
                    out.println(str);
                    System.out.println(in.readLine());
                    str = serverPublicKey.getPublicExponent().toString();
                    out.println(str);
                    System.out.println(in.readLine());
                    
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    
                    // Envoi de la clé privee au serveur               			        
    		        
    		        // Récupération du modulo publique du serveur ( inutile de récupérer l'éxposant publique ) :
    			     byte[] serverPublicModulusTab = bigIntegerToBytes(serverPrivateKey.getModulus());
    			     byte[] serverPrivateExponentTab = bigIntegerToBytes(serverPrivateKey.getPrivateExponent());
    			     byte[] serverPublicExponentTab = bigIntegerToBytes(serverPublicKey.getPublicExponent());
                     String cmpt = in.readLine();
                    System.out.println(cmpt);
                    commande = new CommandAPDU((byte) 0x80,(byte)0x01,(byte)0x00,(byte)0x00,serverPublicModulusTab);
       	         System.out.println("Commande2 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
       	         reponse = canal.transmit(commande);
       	         System.out.println("Reponse2 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
       	         commande = new CommandAPDU((byte) 0x80,(byte)0x02,(byte)0x00,(byte)0x00,0x7F);
       	         reponse = canal.transmit(commande);
       	         System.out.println("Reponse SELECT2 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
       	         commande = new CommandAPDU((byte)0x80,(byte)0x03,(byte)0x00,(byte)0x00,serverPrivateExponentTab);
       	         System.out.println("Commande3 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
       	         reponse = canal.transmit(commande);
       	         System.out.println("Reponse3 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
       	         commande = new CommandAPDU((byte)0x80,(byte)0x04,(byte)0x00,(byte)0x00,(byte)0x7F);
       	         reponse = canal.transmit(commande);
       	         System.out.println("Reponse SELECT3 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
       	         byte[] idBytes = id.getBytes();
       	         commande = new CommandAPDU((byte) 0x80,(byte)0x1B,(byte)0x00,(byte)0x00,idBytes);
                    System.out.println("Commande2 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
                    reponse = canal.transmit(commande);
                    System.out.println("Reponse2 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
                    if(cmpt.equals("0")){
    		        
    			     
    			     
    			     // Envoi de la clé privée RSA du serveur :
    			     commande = new CommandAPDU((byte)0x80,(byte)0x05,(byte)0x00,(byte)0x00,serverPrivateExponentTab);
    			         System.out.println("Commande4 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
    			  reponse = canal.transmit(commande);
    			  System.out.println("Reponse4 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
    			  commande = new CommandAPDU((byte)0x80,(byte)0x06,(byte)0x00,(byte)0x00,0x7F);
    			  reponse = canal.transmit(commande);
    			  System.out.println("Reponse SELECT4 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    			     commande = new CommandAPDU((byte) 0x80,(byte)0x07,(byte)0x00,(byte)0x00,serverPublicModulusTab);
    			         System.out.println("Commande5 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
    			  reponse = canal.transmit(commande);
    			  System.out.println("Reponse5 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
    			  commande = new CommandAPDU((byte) 0x80,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x7F);
    			  reponse = canal.transmit(commande);
    			  System.out.println("Reponse6 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

                    }
    		     
                    commande = new CommandAPDU((byte)0x80,(byte)0x06,(byte)0x00,(byte)0x00,0x7F);
    		         reponse = canal.transmit(commande);
    		         System.out.println("Reponse 5 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    		         byte[] serverPrivateExponentBytes = reponse.getData();
    				  BigInteger serverPrivateExponent = bytesToBigInteger(serverPrivateExponentBytes);
    				  out.println(serverPrivateExponent);
    				  System.out.println(in.readLine());
    		         commande = new CommandAPDU((byte) 0x80,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x7F);
    		         reponse = canal.transmit(commande);
    		         System.out.println("Reponse6 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    		          byte[] serverPrivateModulusBytes = reponse.getData();
    				  BigInteger serverPrivateModulus = bytesToBigInteger(serverPrivateModulusBytes);
    				  out.println(serverPrivateModulus);  
    				  System.out.println(in.readLine());
    				  byte[] sessionKey = openchanel(soc,  in,  out,  commande,  reponse, canal, serverPublicExponentTab);
    				  System.out.println(bytesToBigInteger(sessionKey).toString(16));
    				  String passwordHashed = hashPassword(password);
    				// Création d'une clé AES avec la clé masquée K obtenue :
    					SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

    			        // Créer une instance de Cipher pour le chiffrement AES
    			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

    			        // Initialiser le Cipher en mode de chiffrement avec la clé
    			        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    			        // Chiffrer la signature obtenue :
    			        byte[] encryptedBytes = cipher.doFinal(passwordHashed.getBytes());
    			        out.println(bytesToBigInteger(encryptedBytes));
    			        String strg = in.readLine();
    			        if(str.equals("mot de passe correct !")){
    			        	 // Initialisation de la fenêtre pour la communication avec la carte à puce
    		                Forms frame = new Forms(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab);
    			        }        
                }else {
                    Info.setText("Ce compte n'est pas un administrateur");
                }
            
                 if(type.equals("user") && (userStatut.equals("actif")) ) {    //conecter comme user
                		commande = new CommandAPDU((byte) 0x80,(byte)0x1C,(byte)0x00,(byte)0x00,(byte)0x7F);
                	    reponse = canal.transmit(commande);
                	    System.out.println("Reponse2 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
                	    String idCard = byteArrayToString(reponse.getData());
                	    System.out.println(idCard);
                	    int test = 1;
                	    if(!(idCard.equals(id))){
                	    	test = 0;
                	    }
                	    out.println(test);
                	    if(test == 1){
                		     //recupurer les cle pub du client
                		     BigInteger clientpubmod = new BigInteger(in.readLine());
    			             BigInteger clientpubexp = new BigInteger(in.readLine());
    			        
    			        
    			        
    			             //recupurer les cle pub du serveur
                		     BigInteger serverpubmod = new BigInteger(in.readLine());
    			             BigInteger serverpubexp = new BigInteger(in.readLine());
    			             commande = new CommandAPDU((byte)0x80,(byte)0x06,(byte)0x00,(byte)0x00,0x7F);
    			             reponse = canal.transmit(commande);
    			         System.out.println("Reponse 5 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    			         byte[] serverPrivateExponentBytes = reponse.getData();
    					  BigInteger serverPrivateExponent = bytesToBigInteger(serverPrivateExponentBytes);
    					  out.println(serverPrivateExponent);
    					  System.out.println(in.readLine());
    			         commande = new CommandAPDU((byte) 0x80,(byte)0x08,(byte)0x00,(byte)0x00,(byte)0x7F);
    			         reponse = canal.transmit(commande);
    			         System.out.println("Reponse6 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    			         byte[] serverPrivateModulusBytes = reponse.getData();
    					  BigInteger serverPrivateModulus = bytesToBigInteger(serverPrivateModulusBytes);
    					  out.println(serverPrivateModulus);  
    					  System.out.println(in.readLine());
    					// Envoi de la clé publique RSA du serveur :
    					  byte[] serverPublicExponent = bigIntegerToBytes(serverpubexp);
    					  commande = new CommandAPDU((byte) 0x80,(byte)0x19,(byte)0x00,(byte)0x00,serverPublicExponent);
    					         System.out.println("Commande15 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
    					  reponse = canal.transmit(commande);
    					  commande = new CommandAPDU((byte) 0x80,(byte)0x1A,(byte)0x00,(byte)0x00,(byte)0x7F);
    					  reponse = canal.transmit(commande);
    					  System.out.println("Reponse19 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
    					  byte[] serverPublicExponentTab = reponse.getData();
    					  byte[] sessionKey = openchanel(soc,  in,  out,  commande,  reponse, canal, serverPublicExponentTab);
    					  System.out.println(bytesToBigInteger(sessionKey).toString(16));
    					  String passwordHashed = hashPassword(password);
    					// Création d'une clé AES avec la clé masquée K obtenue :
    						SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

    				        // Créer une instance de Cipher pour le chiffrement AES
    				        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

    				        // Initialiser le Cipher en mode de chiffrement avec la clé
    				        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

    				        // Chiffrer la signature obtenue :
    				        byte[] encryptedBytes = cipher.doFinal(passwordHashed.getBytes());
    				        out.println(bytesToBigInteger(encryptedBytes));
    				        String str = in.readLine();
    				        if(str.equals("mot de passe correct !")){
    				            Bienvenue bienvenue = new Bienvenue(id, soc, in, out, canal, commande, reponse, serverPublicExponentTab, sessionKey);
    				            bienvenue.setVisible(true);
    				        }

    				     
                	    }
                }else {
                	Info.setText("Compte bloqué. Veuillez contacter l'administrateur.");
                    Info.setForeground(Color.RED);
                }
            }else {
            	Info.setText("Code PIN incorrect.");
                if (attempts < 3) {
                	attempts++;
                    Info.setText("Code PIN incorrect. Tentative " + attempts);
                    System.out.println(attempts);
                    Info.setForeground(Color.RED);

                    if (attempts >= 3) {
                        Info.setText("Compte bloqué. Un e-mail a été envoyé à votre adresse enregistrée.");
                    }
                }
            }
            
            

        } catch (Exception e) {	
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
    	
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SampleClient().setVisible(true);
            }
        });
    }
    
    
    
    
    public static byte[] bigIntegerToBytes(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        // Si le tableau a une longueur égale à 1 et que la première valeur est égale à 0, c'est-à-dire que le BigInteger est zéro ou négatif, nous devons supprimer le signe.
        if (byteArray.length > 1 && byteArray[0] == 0) {
            byte[] trimmedArray = new byte[byteArray.length - 1];
            System.arraycopy(byteArray, 1, trimmedArray, 0, trimmedArray.length);
            return trimmedArray;
        }
        return byteArray;
    }

    public static BigInteger bytesToBigInteger(byte[] byteArray) {
        // Si le tableau de bytes est vide, retourner BigInteger.ZERO
        if (byteArray.length == 0) {
            return BigInteger.ZERO;
        }
        // Convertir le tableau de bytes en un BigInteger
        return new BigInteger(1, byteArray); // Le "1" signifie que le tableau de bytes est positif ou nul
    }
    public byte[] openchanel(Socket soc, BufferedReader in, PrintWriter out, CommandAPDU commande, ResponseAPDU reponse, CardChannel canal, byte[] serverPublicExponentTab) {
    	byte[] maskedK = new byte[128];
    	// Récupération des paramètres DH auprès du client ( Y, P, G) :
        
		try {
			 // Diffie-Hellman key exchange (DHKE)

            // 1. Generate a large prime p and a generator g on the client side (use a secure prime generation library)
            // You can find libraries for secure prime generation. This example is omitted for brevity.
   
   // Initialisation des paramétres DH :
   commande = new CommandAPDU((byte)0x80,(byte)0x10,(byte)0x00,(byte)0x00);
   System.out.println("Commande7 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse7 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   
   // Affichage Y :
   commande = new CommandAPDU((byte)0x80,(byte)0x11,(byte)0x01,(byte)0x00,(byte)0x7F);
   System.out.println("Commande8 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse8 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   BigInteger y = bytesToBigInteger(reponse.getData());
   String str = y.toString();
            out.println(str);
            System.out.println(in.readLine());
   
   //Affichage P :
   commande = new CommandAPDU((byte)0x80,(byte)0x11,(byte)0x02,(byte)0x00,(byte)0x7F);
   System.out.println("Commande9 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse9 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   BigInteger p = bytesToBigInteger(reponse.getData());
   str = p.toString();
            out.println(str);
            System.out.println(in.readLine());

   //Affichage G :
   commande = new CommandAPDU((byte)0x80,(byte)0x11,(byte)0x03,(byte)0x00,(byte)0x7F);
   System.out.println("Commande10 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse10 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   BigInteger g = bytesToBigInteger(reponse.getData());
   str = g.toString();
            out.println(str);
            System.out.println(in.readLine());
   
   // X = G^n mod P :
   BigInteger x = new BigInteger(in.readLine());
   byte[] xBytes = bigIntegerToBytes(x);
   
   // Affichage x (1024 bits) :
   System.out.println(in.readLine());
   
   // K = Y^n mod P (1024 bits) :
   BigInteger K = new BigInteger(in.readLine());
   
   // Affichage de K :
   System.out.println(in.readLine());
   
   // Application du masque à K (résultat sur 128 bits)
   BigInteger bigmaskedK = new BigInteger(in.readLine());
   maskedK = bigIntegerToBytes(bigmaskedK);        
            //Affichage de K :
            System.out.println("K masqué : "+bigmaskedK.toString(16));

       // Affichage de la concaténation ainsi que sa taille :
       BigInteger bigconcat = new BigInteger(in.readLine());
       System.out.println("X Concat Y : "+ bigconcat.toString(16));
    
// Affichage de la signature :
       BigInteger bigsigned = new BigInteger(in.readLine());
System.out.println("Signature du serveur : "+ bigsigned.toString(16));

// Affichage de la donnée chiffrée et sa taille :
       BigInteger bigencryptedBytes = new BigInteger(in.readLine());
       byte[] encryptedBytes = bigIntegerToBytes(bigencryptedBytes);
       System.out.println("Signature du serveur chiffrée : "+bigencryptedBytes.toString(16));
       
       // Envoi du X pour le calcul de K dans le client :
       commande = new CommandAPDU((byte)0x80,(byte)0x10,(byte)0x1F,(byte)0x00,xBytes);
   System.out.println("Commande11 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse11 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   
   // Affichage de K côté client :
   commande = new CommandAPDU((byte)0x80,(byte)0x11,(byte)0x04,(byte)0x00,(byte)0x7F);
   System.out.println("Commande12 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse12 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
   
   // Envoi de la signature chiffrée :
   commande = new CommandAPDU((byte)0x80,(byte)0x12,(byte)0x06,(byte)0x00,encryptedBytes);
   System.out.println("Commande13 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
   reponse = canal.transmit(commande);
   System.out.println("Reponse13 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// Application du masque au K obtenue dans le client :
commande = new CommandAPDU((byte) 0x80,(byte)0x13,(byte)0x00,(byte)0x00);
       System.out.println("Commande14 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
reponse = canal.transmit(commande);
System.out.println("Reponse14 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
commande = new CommandAPDU((byte) 0x80,(byte)0x11,(byte)0x05,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse15 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// Récupération de la signature déchiffrée auprès du client :
commande = new CommandAPDU((byte) 0x80,(byte)0x14,(byte)0x00,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse16 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// Récupération du Y || X auprès du client :
commande = new CommandAPDU((byte) 0x80,(byte)0x15,(byte)0x00,(byte)0x00);
reponse = canal.transmit(commande);
System.out.println("Reponse17 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// Envoi de la clé publique RSA du serveur :
commande = new CommandAPDU((byte) 0x80,(byte)0x19,(byte)0x00,(byte)0x00,serverPublicExponentTab);
       System.out.println("Commande15 : "+Util.byteArrayToHexString(commande.getBytes(), " "));
reponse = canal.transmit(commande);
System.out.println("Reponse18 : "+Util.byteArrayToHexString(reponse.getBytes(), " "));
commande = new CommandAPDU((byte) 0x80,(byte)0x1A,(byte)0x00,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse19 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");
//Execution de la vérification de la signature côté client :
commande = new CommandAPDU((byte) 0x80,(byte)0x16,(byte)0x00,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse20 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// on récupère la réponse de la vérification de la signature du serveur auprès du client :

byte[] response = new byte[1];
response = reponse.getData();

// Si la réponse est 1 le client a alors authentifié le serveur :
if(response[0] == 1){
System.out.println("Authentification du serveur avec succés");
}

// Execution de l'instruction de signature du message concaténé auprès du client :
commande = new CommandAPDU((byte) 0x80,(byte)0x17,(byte)0x00,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse21 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

// Execution de l'instruction de chiffrement de la signature auprès du client :
commande = new CommandAPDU((byte) 0x80,(byte)0x18,(byte)0x00,(byte)0x00,(byte)0x7F);
reponse = canal.transmit(commande);
System.out.println("Reponse22 : "+Util.byteArrayToHexString(reponse.getBytes(), " ")+"\n");

       // récupération et envoi de la signature du client déchifrée :
       byte[] encsignedclient = reponse.getData();
       BigInteger bigencsignedclient = bytesToBigInteger(encsignedclient);
       out.println(bigencsignedclient);
       
       // Réponse du serveur :
       str = in.readLine();
       if(str.equals("true")){
        System.out.println("Authentification du client avec succés");
       }
       
      
// D connexion
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        return maskedK;
    }
    //Fonction de masque MGF1, retourne le masque de la clé secrète :
    public static byte[] maskFunction(byte[] mgfSeed, int maskLen) throws NoSuchAlgorithmException {
       MessageDigest digest = MessageDigest.getInstance("SHA-256");
       byte[] mask = new byte[maskLen];
       byte[] counterBytes = new byte[4];// Supposant que le compteur soit codé sur 4 octets
       System.arraycopy(mgfSeed, 0, counterBytes, 0, counterBytes.length);
       byte[] concat = new byte[mgfSeed.length+counterBytes.length];
       int counter = 0;
       int hashLen = digest.getDigestLength();
          byte[] hash = new byte[hashLen];
       while (maskLen > 0) {
           counterBytes[0] = (byte) (counter >>> 24);
           counterBytes[1] = (byte) (counter >>> 16);
           counterBytes[2] = (byte) (counter >>> 8);
           counterBytes[3] = (byte) counter;

           digest.reset();
           System.arraycopy(mgfSeed,  0, concat, 0, mgfSeed.length);
           System.arraycopy(counterBytes, 0, concat, mgfSeed.length, counterBytes.length);
           digest.update(concat);
           hash = digest.digest(concat);

           if (maskLen > hashLen) {
               System.arraycopy(hash, 0, mask, mask.length - maskLen, hashLen);
           } else {
               System.arraycopy(hash, 0, mask, mask.length - maskLen, maskLen);
           }

           maskLen -= hashLen;
           counter++;
       }

       return mask;
    }
    public static String byteArrayToString(byte[] bytes) {

        // Initialise une chaîne de caractères vide

        StringBuilder sb = new StringBuilder();

        int indicateur = 0;

        // Boucle à travers chaque byte du tableau

        for (byte b : bytes) {
        	if(b != 0){
        		indicateur = 1;
        	}

            // Ajoute chaque byte converti en caractère à la chaîne
            if(indicateur == 1){
                sb.append((char) b);
            }

        }

        

        // Retourne la chaîne de caractères résultante

        return sb.toString();

    }
    public static String hashPassword(String password) {
	    MessageDigest digest = null;
	    try {
	        // Change "SHA-256" to "SHA-1" for 128-bit hash
	        digest = MessageDigest.getInstance("SHA-1");
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }

	    digest.update(password.getBytes());
	    byte[] hash = digest.digest();

	    // Convert the byte array to a hex string for easier viewing
	    StringBuilder sb = new StringBuilder();
	    for (byte b : hash) {
	        sb.append(String.format("%02x", b));
	    }

	    // Truncate to get only the first 16 bytes (128 bits) for the hash
	    return sb.toString().substring(0, 32);
	}

}
    
