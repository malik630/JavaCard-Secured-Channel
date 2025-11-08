package sampleserver;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
//Packages définis pour l'usage des fonctions cryptographiques :
import java.math.BigInteger;
import java.net.InetAddress;
//Packages relatifs à la communication avec le client à travers les sockets :
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;




class bienvenueserver{
	private PreparedStatement pst;
	private Connection con;
    private ResultSet rs;



	//user space
    public void Connect() throws ClassNotFoundException, SQLException {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/projet", "root", "mellitiesi2023");  
    }
    private void sendEmailNotification(String recipient, int debitedAmount, int newCredit) {
        // Sender's email credentials
        final String username = "esibanque@gmail.com";
        final String password = "bqfc iacn zfpj fcyt"; 
        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Notification de débit");
            message.setText("Cher client " + recipient + ",\n\nVotre compte a été débité de " + debitedAmount +
                    ". Votre nouveau crédit est " + newCredit + ".\n\nCordialement,\nEsibanque");

            // Send the email
            Transport.send(message);
            System.out.println("Email notification sent successfully to " + recipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void sendEmailNotificationRecharge(String recipient, int rechargedAmount, int newCredit) {
        // Sender's email credentials
        final String username = "esibanque@gmail.com";
        final String password = "bqfc iacn zfpj fcyt"; 
        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject("Notification de recharge");
            message.setText("Cher client,\n\nVotre compte a été rechargé de " + rechargedAmount +
                    ". Votre nouveau crédit est " + newCredit + ".\n\nCordialement,\nEsibanque");

            // Send the email
            Transport.send(message);
            System.out.println("Email notification sent successfully to " + recipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	
	
	public bienvenueserver(String userId, ServerSocket socket, Socket soc, BufferedReader in, PrintWriter out, BigInteger serverPrivateModulus, BigInteger serverPrivateExponent, byte[] sessionKey) throws ClassNotFoundException, SQLException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
		try {
			String action = in.readLine();
			while(action != null) {
				action = in.readLine();
				if (action.equals("Afficher mon crédit")) {
					Connect();
					try {
					// Requête SQL pour récupérer le crédit de l'utilisateur à partir de son ID dans le tableau "comptes"
	                String query = "SELECT credit FROM comptes WHERE id = ?";
	                pst = con.prepareStatement(query);
	                pst.setString(1, userId);
	                ResultSet creditResultSet = pst.executeQuery();

	                if (creditResultSet.next()) {
	                    int credit = creditResultSet.getInt("credit");
	                    SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");
                        BigInteger bigCredit = new BigInteger(String.valueOf(credit));
       			        // Créer une instance de Cipher pour le chiffrement AES
       			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
       			        // Initialiser le Cipher en mode déchiffrement avec la clé
       			        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
       			        byte[] creditBytes = cipher.doFinal(bigIntegerToBytes(bigCredit));
    			        BigInteger creditEnc = bytesToBigInteger(creditBytes);
	                    out.println(creditEnc);
	                } else {
	                    System.out.println("Aucun crédit associé à cet utilisateur.");
	                }
	            
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        } 
	    
					
					
				}else {
					if (action.equals("Afficher mon historique")) {
						Connect();
				        try {
				           
				                // Requête SQL pour récupérer l'historique des transactions de l'utilisateur à partir de son ID dans le tableau "transactions"
				                String query = "SELECT * FROM transactions WHERE id = ?";
				                pst = con.prepareStatement(query);
				                pst.setString(1, userId);
				                ResultSet transactionsResultSet = pst.executeQuery();
				                
				             // Parcourir les résultats de la requête et ajouter chaque transaction au modèle de tableau
				                while (transactionsResultSet.next()) {
				                    int idTransaction = transactionsResultSet.getInt("idtransaction");
				                    out.println(idTransaction);
				                    String type = transactionsResultSet.getString("type");
				                    out.println(type);
				                    int montant = transactionsResultSet.getInt("montant");
				                    out.println(montant);
				                    Timestamp dateHeure = transactionsResultSet.getTimestamp("dateetheure");
				                    out.println(dateHeure);
				                    int nouveauCredit = transactionsResultSet.getInt("nouveaucredit");
				                    out.println(nouveauCredit);
				                }
				        }catch (Exception e) {
				        	e.printStackTrace();
				        }
						
						
						
					}else {
						if (action.equals("Recharger mon crédit")) {
                            BigInteger montantBigInt = new BigInteger(in.readLine());
                            
                            SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

           			        // Créer une instance de Cipher pour le chiffrement AES
           			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
           			        // Initialiser le Cipher en mode déchiffrement avec la clé
           			        cipher.init(Cipher.DECRYPT_MODE, secretKey);
           			        
           			        byte[] montantBytes = cipher.doFinal(bigIntegerToBytes(montantBigInt));
           			        String montantstr = byteArrayToString(montantBytes);
							int montant = Integer.parseInt(montantstr);
							Connect();
							try {
								// Récupérer l'e-mail de l'utilisateur à partir du tableau "clients"
					                String userEmailQuery = "SELECT email FROM clients WHERE id = ?";
					                pst = con.prepareStatement(userEmailQuery);
					                pst.setString(1, userId);
					                ResultSet userEmailResultSet = pst.executeQuery();
					                String userEmail = "";
					                if (userEmailResultSet.next()) {
					                    userEmail = userEmailResultSet.getString("email");
					                    
					                 // Récupérer l'email associé à l'ID de l'utilisateur dans le tableau "clients"
					                    String creditQuery = "SELECT credit FROM comptes WHERE id = ?";
					                    pst = con.prepareStatement(creditQuery);
					                    pst.setString(1, userId);
					                    ResultSet ResultSet = pst.executeQuery();
					                    int credit;
										if (ResultSet.next()) {
					                        credit = Integer.parseInt(ResultSet.getString("credit"));
					                   
							        	
							        	
					                    // Mettre à jour le crédit dans le tableau "comptes"
					                    String updateCreditQuery = "UPDATE comptes SET credit = credit + ? WHERE id = ?";
					                    pst = con.prepareStatement(updateCreditQuery);
					                    pst.setInt(1, montant);
					                    pst.setString(2, userId);
					                    pst.executeUpdate();

					                    // Enregistrer la transaction dans la table "transactions"
					                    String insertTransactionQuery = "INSERT INTO transactions (idtransaction, type, id, montant, dateetheure, nouveaucredit) VALUES (?, ?, ?, ?, ?, ?)";
					                    pst = con.prepareStatement(insertTransactionQuery);
					                    int idt = 1000;
					                    pst.setInt(1, idt);
					                    idt++;
					                    pst.setString(2, "recharge");
					                    pst.setString(3, userId);
					                    pst.setInt(4, montant);
					                    pst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
					                    credit=+montant;
					                    pst.setInt(6, credit);
					                    pst.executeUpdate();

					                    // Envoyer une notification par e-mail
					                    sendEmailNotificationRecharge(userEmail, montant, credit);

					                    System.out.println("Recharge de " + montant + " crédits effectuée avec succès. Nouveau solde : " + credit);
					                } else {
					                    System.out.println("Aucun e-mail trouvé pour cet utilisateur.");
					                }
					                }
					            
					        } catch (SQLException ex) {
					            ex.printStackTrace();
					            System.out.println("Erreur lors de la recharge du crédit.");
					        } catch (NumberFormatException ex) {
					            System.out.println("Montant de recharge invalide.");
					        } 
						}else {
							if (action.equals("Débiter")) {
								BigInteger montantBigInt = new BigInteger(in.readLine());
	                            
	                            SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

	           			        // Créer une instance de Cipher pour le chiffrement AES
	           			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
	           			        // Initialiser le Cipher en mode déchiffrement avec la clé
	           			        cipher.init(Cipher.DECRYPT_MODE, secretKey);
	           			        
	           			        byte[] montantBytes = cipher.doFinal(bigIntegerToBytes(montantBigInt));
	           			        String montantstr = byteArrayToString(montantBytes);
								int montant = Integer.parseInt(montantstr);
								Connect();
								try {
						        	// Récupérer l'email associé à l'ID de l'utilisateur dans le tableau "clients"
				                    String creditQuery = "SELECT credit FROM clients WHERE id = ?";
				                    pst = con.prepareStatement(creditQuery);
				                    pst.setString(1, userId);
				                    ResultSet ResultSet = pst.executeQuery();
				                    int credit;
									if (ResultSet.next()) {
				                        credit = Integer.parseInt(ResultSet.getString("credit"));
				                   
						        	
						        	
						        	
						            if (montant <= credit) {
						                // le montant 
						                credit -= montant;


						                    // Récupérer l'email associé à l'ID de l'utilisateur dans le tableau "clients"
						                    String userEmailQuery = "SELECT email FROM clients WHERE id = ?";
						                    pst = con.prepareStatement(userEmailQuery);
						                    pst.setString(1, userId);
						                    ResultSet userEmailResultSet = pst.executeQuery();
						                    String userEmail = "";
						                    if (userEmailResultSet.next()) {
						                        userEmail = userEmailResultSet.getString("email");

						                        // Mettre à jour le crédit dans le tableau "comptes"
						                        String updateCreditQuery = "UPDATE comptes SET credit = ? WHERE id = ?";
						                        pst = con.prepareStatement(updateCreditQuery);
						                        pst.setInt(1, credit);
						                        pst.setString(2, userId);
						                        pst.executeUpdate();

						                        // Enregistrer la transaction dans la table "transactions"
						                        String insertTransactionQuery = "INSERT INTO transactions (idtransaction, type, id,montant, dateetheure, nouveaucredit) VALUES (?, ?,?, ?, ?, ?)";
						                        pst = con.prepareStatement(insertTransactionQuery);
						                        int idt=1000;
						                        pst.setInt(1, idt);
						                        idt++;
						                        pst.setString(2, "retrait");
						                        pst.setString(3, userId);
						                        pst.setInt(4, montant);
						                        pst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
						                        pst.setInt(6, credit);
						                        pst.executeUpdate();

						                        // Envoyer une notification par e-mail
						                        sendEmailNotification(userEmail, montant, credit);

						                        System.out.println("Débit de " + montant + " crédits effectué avec succès. Nouveau solde : " + credit);
						                    } else {
						                        System.out.println("Aucun email trouvé pour cet utilisateur.");
						                    }
						                
						            } else {
						                System.out.println("Crédit insuffisant pour le débit.");
						               
						            }
									}
						        } catch (SQLException ex) {
						            ex.printStackTrace();
						            System.out.println("Erreur lors de la déduction du crédit.");
						        } catch (NumberFormatException ex) {
						            System.out.println("Montant de débit invalide.");
						        }
							}
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//Méthode pour convertir un BigInteger en tableau de byte :
    public static byte[] bigIntegerToBytes(BigInteger bigInteger) {
    byte[] byteArray = bigInteger.toByteArray();
    //Si le tableau a une longueur égale à 1 et que la première valeur est ègale à 0, c'est-à-dire que le BigInteger est zéro ou négatif, nous devons supprimer le signe.
    if (byteArray.length > 1 && byteArray[0] == 0) {
    byte[] trimmedArray = new byte[byteArray.length - 1];
    System.arraycopy(byteArray, 1, trimmedArray, 0, trimmedArray.length);
    return trimmedArray;
    }
    return byteArray;
    }
    //Méthode pour convertir un tableau de byte en BigInteger :
    public static BigInteger bytesToBigInteger(byte[] byteArray) {
    //Si le tableau de bytes est vide, retourner BigInteger.ZERO
    if (byteArray.length == 0) {
    return BigInteger.ZERO;
    }
    return new BigInteger(1, byteArray); // Le "1" signifie que le tableau de bytes est positif ou nul
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
}


class formServer extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField nomfield;
    private JTextField pinfield;
    private JTextField numerofield;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private JTextField idfield;
    private JTextField adressefield;
    private JTextField emailfield;
    private JTextField datefield;
    private JTextField numsécuritéfield;
    private JTextField nouveaunomfield;
    private JTextField modifierstatutfield;
    private JTextField nouveautypefield;

//connection m3a lbdd
    public void Connect() throws ClassNotFoundException, SQLException {
        
        	Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/projet", "root", "mellitiesi2023");
      
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
    public formServer(String userId, final ServerSocket socket, final Socket soc, final BufferedReader in, final PrintWriter out, final BigInteger serverPublicModulus, final BigInteger serverPrivateExponent) {
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//ttghl9 
        setBounds(100, 100, 1012, 493);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(172, 189, 240));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Enregistrement", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panel.setBounds(10, 56, 259, 285);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel username = new JLabel("Nom d'utilisateur");
        username.setEnabled(false);
        username.setBounds(10, 33, 100, 14);
        panel.add(username);

        JLabel pin = new JLabel("PIN");
        pin.setEnabled(false);
        pin.setBounds(10, 74, 49, 14);
        panel.add(pin);

        nomfield = new JTextField();
        nomfield.setBounds(110, 30, 96, 20);
        panel.add(nomfield);
        nomfield.setColumns(10);

        pinfield = new JTextField();
        pinfield.setBounds(110, 71, 96, 20);
        panel.add(pinfield);
        pinfield.setColumns(10);
        
        adressefield = new JTextField();
        adressefield.setBounds(110, 106, 96, 20);
        panel.add(adressefield);
        adressefield.setColumns(10);
        
        emailfield = new JTextField();
        emailfield.setBounds(110, 178, 96, 20);
        panel.add(emailfield);
        emailfield.setColumns(10);
        
        datefield = new JTextField();
        datefield.setBounds(110, 209, 96, 20);
        panel.add(datefield);
        datefield.setColumns(10);
        
        numsécuritéfield = new JTextField();
        numsécuritéfield.setBounds(110, 240, 96, 20);
        panel.add(numsécuritéfield);
        numsécuritéfield.setColumns(10);
        
        JLabel adresse = new JLabel("adresse");
        adresse.setBounds(10, 109, 49, 14);
        panel.add(adresse);
        
        JLabel numero = new JLabel("numero telephone");
        numero.setBounds(10, 140, 90, 14);
        panel.add(numero);
        
        JLabel date = new JLabel("date de naissance");
        date.setBounds(10, 212, 100, 14);
        panel.add(date);
        
        JLabel numerosécurité = new JLabel("num sécurité sociale");
        numerosécurité.setBounds(10, 243, 115, 14);
        panel.add(numerosécurité);
        
        JLabel email = new JLabel("email");
        email.setBounds(10, 181, 49, 14);
        panel.add(email);
        
                numerofield = new JTextField();
                numerofield.setBounds(110, 137, 96, 20);
                panel.add(numerofield);
                numerofield.setColumns(10);

                
                final String action = "not saved";
                		
                JButton savebutton = new JButton("Enregistrer");
                savebutton.setFont(new Font("Tahoma", Font.PLAIN, 12));
                savebutton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	enregisteruser(action, socket, soc, in, out, serverPublicModulus, serverPrivateExponent);
                    }
                });
                savebutton.setBounds(20, 352, 89, 23);
                contentPane.add(savebutton);
        JButton clearbutton = new JButton("Effacer");
        clearbutton.setFont(new Font("Tahoma", Font.PLAIN, 12));
        clearbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	try {
					effaceruser();
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        clearbutton.setBounds(143, 352, 89, 23);
        contentPane.add(clearbutton);
        
        final JPanel panel_2 = new JPanel();
        panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        panel_2.setBounds(737, 84, 212, 95);
        contentPane.add(panel_2);
        panel_2.setLayout(null);
        
        JPanel panel_3 = new JPanel();
        panel_3.setBackground(new Color(0, 0, 160));
        panel_3.setBounds(0, 0, 979, 45);
        contentPane.add(panel_3);
        panel_3.setLayout(null);
        
        JLabel lblNewLabel_5 = new JLabel("banque");
        lblNewLabel_5.setForeground(new Color(0, 128, 192));
        lblNewLabel_5.setFont(new Font("Tw Cen MT Condensed", Font.BOLD, 18));
        lblNewLabel_5.setBounds(884, 22, 78, 23);
        panel_3.add(lblNewLabel_5);
        
        JLabel lblNewLabel_3_1 = new JLabel("ESI");
        lblNewLabel_3_1.setForeground(Color.WHITE);
        lblNewLabel_3_1.setFont(new Font("Palatino Linotype", Font.BOLD, 28));
        lblNewLabel_3_1.setBounds(902, 0, 60, 39);
        panel_3.add(lblNewLabel_3_1);
        
                JLabel lblNewLabel = new JLabel("Bienvenue Admin");
                lblNewLabel.setBounds(382, -4, 314, 35);
                panel_3.add(lblNewLabel);
                lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 24));
                lblNewLabel.setForeground(new Color(255, 255, 255));
        
        JPanel panel_4 = new JPanel();
        panel_4.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "modification", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_4.setBounds(310, 56, 378, 151);
        contentPane.add(panel_4);
                panel_4.setLayout(null);
        
                JButton modifierButton = new JButton("modifier nom");
                modifierButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
                modifierButton.setBounds(231, 47, 137, 23);
                panel_4.add(modifierButton);
                
                nouveaunomfield = new JTextField();
                nouveaunomfield.setBounds(125, 48, 96, 20);
                panel_4.add(nouveaunomfield);
                nouveaunomfield.setColumns(10);
                
                modifierstatutfield = new JTextField();
                modifierstatutfield.setBounds(125, 79, 96, 20);
                panel_4.add(modifierstatutfield);
                modifierstatutfield.setColumns(10);
                
                JButton modifierstatutbutton = new JButton("modifier statut");
                modifierstatutbutton.setFont(new Font("Tahoma", Font.PLAIN, 12));
                modifierstatutbutton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
							modifierstatut();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                });
                modifierstatutbutton.setBounds(231, 78, 137, 23);
                panel_4.add(modifierstatutbutton);
                
                JButton modifiertypebutton = new JButton("modifier type");
                modifiertypebutton.setFont(new Font("Tahoma", Font.PLAIN, 12));
                modifiertypebutton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        try {
							modifiertype();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
                    }
                });
                modifiertypebutton.setBounds(231, 117, 137, 23);
                panel_4.add(modifiertypebutton);
                
                JLabel nouveaunom = new JLabel("nouveau nom");
                nouveaunom.setFont(new Font("Tahoma", Font.PLAIN, 12));
                nouveaunom.setBounds(10, 49, 105, 14);
                panel_4.add(nouveaunom);
                
                        JLabel lblNewLabel_1 = new JLabel("ID utilisateur");
                        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
                        lblNewLabel_1.setBounds(36, 23, 100, 14);
                        panel_4.add(lblNewLabel_1);
                        
                                idfield = new JTextField();
                                idfield.setBounds(151, 20, 70, 20);
                                panel_4.add(idfield);
                                idfield.setColumns(10);
                                
                                
                                JButton btnNewButton = new JButton("checher");
                                btnNewButton.setBounds(279, 11, 89, 23);
                                panel_4.add(btnNewButton);
                                btnNewButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                    	try {
											checher(panel_2);
										} catch (ClassNotFoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
                                    }
                                });
                                
                                JLabel nouveaustatut = new JLabel("nouveau statut");
                                nouveaustatut.setFont(new Font("Tahoma", Font.PLAIN, 12));
                                nouveaustatut.setBounds(10, 82, 124, 14);
                                panel_4.add(nouveaustatut);
                                final JPanel panel_1 = new JPanel();
                                panel_1.setBounds(299, 220, 482, 213);
                                contentPane.add(panel_1);
                                
                                
                                nouveautypefield = new JTextField();
                                nouveautypefield.setBounds(125, 118, 96, 20);
                                panel_4.add(nouveautypefield);
                                nouveautypefield.setColumns(10);
                                
                                JLabel nouveautype = new JLabel("nouveau type");
                                nouveautype.setFont(new Font("Tahoma", Font.PLAIN, 12));
                                nouveautype.setBounds(10, 121, 124, 14);
                                panel_4.add(nouveautype);
                                
                                JButton afficherhistoriquecnx = new JButton("Afficher l'historique de connexion");
                                afficherhistoriquecnx.setFont(new Font("Tahoma", Font.PLAIN, 12));
                                afficherhistoriquecnx.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                    	try {
											Afficherhistorique(panel_1);
										} catch (ClassNotFoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
                                    }
                                });
                                afficherhistoriquecnx.setBounds(747, 184, 195, 23);
                                contentPane.add(afficherhistoriquecnx);
                                
                                
                               
                                
                                modifierButton.addActionListener(new ActionListener() {
                                    public void actionPerformed(ActionEvent e) {
                                    	try {
											modifiernom();
										} catch (ClassNotFoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
                                    }
                                });
    }

    public void enregisteruser(String action, ServerSocket socket, Socket soc, BufferedReader in, PrintWriter out, BigInteger serverPublicModulus, BigInteger serverPrivateExponent) {
    	// Récupération des données depuis les champs de texte
        String userName = nomfield.getText();
        String pin = pinfield.getText();
        String adresse = adressefield.getText();
        String numTelephone = numerofield.getText();
        String email = emailfield.getText();
        String dateNaissance = datefield.getText();
        String numSecuSociale = numsécuritéfield.getText();

        
        
        
        try {
            Connect();
            Statement stmt = con.createStatement();

            // Obtention du dernier ID client dans la base de données
            ResultSet rs = stmt.executeQuery("SELECT MAX(id) FROM clients");
            int lastClientID = 0;
            if (rs.next()) {
                lastClientID = rs.getInt(1);
            }
            int newClientID = lastClientID + 1;
            
            // Hashage du PIN
            String hashedPin = hashPassword(pin);
            int randomAccountID = new Random().nextInt(1000000);

            String query2 = "INSERT INTO comptes (idcompte, id, dateouverture, type, credit, statut, pin,clépublique_mod, clépublique_exp) VALUES (?, ?, ?, ?, ?, ?, ?,?, ?)";
            pst = con.prepareStatement(query2);
            pst.setInt(1, randomAccountID);
            pst.setInt(2, newClientID);
            pst.setTimestamp(3, new Timestamp(System.currentTimeMillis())); // Date
            pst.setString(4, "user");
            pst.setInt(5, 0); // C
            pst.setString(6, "bloqué");
            pst.setString(7, hashedPin); // Utilisation du PIN hashé
            action = "saved";
            out.println(action);     //user is saved, we should personalize
            
            out.println(newClientID);
            out.flush();
            
         // Récupération des clés RSA du client :
            BigInteger publicMod = new BigInteger(in.readLine());   //*************************************CLIENT*************************************************************
            out.println(publicMod);
            BigInteger publicExpo = new BigInteger(in.readLine());   //*************************************CLIENT*************************************************************8
            out.println(publicExpo);
            
            String pub_mod = publicMod.toString();
            String pub_exp = publicExpo.toString();
            
            System.out.println("___________________________________________: "+pub_mod);
            System.out.println("___________________________________________: "+pub_exp);
            pst.setString(8, pub_mod);
            pst.setString(9, pub_exp);
            pst.executeUpdate();

            // Puis insérer les données dans la table `clients`
            String query1 = "INSERT INTO clients (nom, id, adresse, numtelephone, email, datedenaissance, numerosécuritésociale) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(query1);
            pst.setString(1, userName);
            pst.setInt(2, newClientID);
            pst.setString(3, adresse);
            pst.setString(4, numTelephone);
            pst.setString(5, email);
            pst.setString(6, dateNaissance);
            pst.setString(7, numSecuSociale);
            pst.executeUpdate();


            
            // Nettoyage des champs de texte après l'enregistrement
            nomfield.setText("");
            pinfield.setText("");
            adressefield.setText("");
            numerofield.setText("");
            emailfield.setText("");
            datefield.setText("");
            numsécuritéfield.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // Fermeture des ressources
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    
    public void effaceruser() throws ClassNotFoundException {
    	String userName = nomfield.getText();
        String pin = pinfield.getText();

        try {
            Connect();
            String query = "SELECT * FROM clients WHERE nom = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userName);
            rs = pst.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id");

               
                String pinQuery = "SELECT pin FROM comptes WHERE id = ?";
                pst = con.prepareStatement(pinQuery);
                pst.setInt(1, userId);
                ResultSet pinRs = pst.executeQuery();

                if (pinRs.next()) {
                    String pinFromDatabase = pinRs.getString("pin");
                    System.out.println("PIN from database: " + pinFromDatabase); 

                    if (pinFromDatabase.equals(pin)) {
                        
                        String deleteQuery = "DELETE FROM clients WHERE id = ?";
                        pst = con.prepareStatement(deleteQuery);
                        pst.setInt(1, userId);
                        pst.executeUpdate();

                       
                        String deleteAccountQuery = "DELETE FROM comptes WHERE id = ?";
                        pst = con.prepareStatement(deleteAccountQuery);
                        pst.setInt(1, userId);
                        pst.executeUpdate();

                        System.out.println("Utilisateur effacé avec succès !");
                    } else {
                        System.out.println("Le PIN fourni est incorrect.");
                    }
                } else {
                    System.out.println("Aucun compte trouvé pour cet utilisateur.");
                }
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void modifiernom() throws ClassNotFoundException{
    	String userId = idfield.getText();
        String newUserName = nouveaunomfield.getText();

        try {
            Connect();
            
          
            String query = "SELECT * FROM clients WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
               
                String updateQuery = "UPDATE clients SET nom = ? WHERE id = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, newUserName);
                pst.setString(2, userId);
                pst.executeUpdate();

                System.out.println("Nom modifié avec succès !");
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
           
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void modifierstatut() throws ClassNotFoundException{
    	String userId = idfield.getText();
        String newStatut = modifierstatutfield.getText();

        try {
            Connect();
           
            String query = "SELECT * FROM comptes WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
               
                String updateQuery = "UPDATE comptes SET statut = ? WHERE id = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, newStatut);
                pst.setString(2, userId);
                pst.executeUpdate();

                System.out.println("Statut modifié avec succès !");
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
           
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void modifiertype() throws ClassNotFoundException{
    	String userId = idfield.getText();
        String newType = nouveautypefield.getText();

        try {
            Connect();
            
      
            String query = "SELECT * FROM comptes WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
               
                String updateQuery = "UPDATE comptes SET type = ? WHERE id = ?";
                pst = con.prepareStatement(updateQuery);
                pst.setString(1, newType);
                pst.setString(2, userId);
                pst.executeUpdate();

                System.out.println("Type de compte modifié avec succès !");
            } else {
                System.out.println("Utilisateur non trouvé.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
           
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void checher(JPanel panel_2) throws ClassNotFoundException{
    	panel_2.removeAll();

        String userId = idfield.getText();

        try {
            Connect();
            
          
            String query1 = "SELECT nom FROM clients WHERE id = ?";
            pst = con.prepareStatement(query1);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                JLabel nameLabel = new JLabel("Nom: " + rs.getString("nom"));
                nameLabel.setBounds(10, 10, 200, 20);
                panel_2.add(nameLabel);
            }

         
            String query2 = "SELECT pin FROM comptes WHERE id = ?";
            pst = con.prepareStatement(query2);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            if (rs.next()) {
                JLabel pinLabel = new JLabel("PIN: " + rs.getString("pin"));
                pinLabel.setBounds(10, 35, 200, 20);
                panel_2.add(pinLabel);
            } else {
                JLabel notFoundLabel = new JLabel("Aucun compte trouvé pour l'ID: " + userId);
                notFoundLabel.setBounds(10, 35, 300, 20);
                panel_2.add(notFoundLabel);
            }

            panel_2.revalidate();
            panel_2.repaint();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void Afficherhistorique(JPanel panel_1) throws ClassNotFoundException{
    	String userId = idfield.getText();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("ID Connexion");
        model.addColumn("Date et Heure");
        model.addColumn("Adresse IP");
        model.addColumn("Résultat");

        try {
            Connect();
            String query = "SELECT * FROM historique WHERE id = ?";
            pst = con.prepareStatement(query);
            pst.setString(1, userId);
            rs = pst.executeQuery();

            while (rs.next()) {
                String id = rs.getString("id");
                String idConnexion = rs.getString("idconnexion");
                String dateHeure = rs.getString("dateetheure");
                String adresseIP = rs.getString("adresseip");
                String resultat = rs.getString("résultat");

                model.addRow(new Object[]{id, idConnexion, dateHeure, adresseIP, resultat});
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setBounds(10, 10, 229, 286);
            panel_1.removeAll();
            panel_1.add(scrollPane);
            panel_1.revalidate();
            panel_1.repaint();

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
}  
}

public class SampleServer {
	
	private static Connection con;
	
	
	public static void Connect() throws ClassNotFoundException, SQLException {
	  
	        	Class.forName("com.mysql.cj.jdbc.Driver");
	            con = DriverManager.getConnection("jdbc:mysql://localhost/projet", "root", "mellitiesi2023");
	       
	    }
    public static String hashPassword(String password) {
	    MessageDigest digest = null;
	    try {
	        // Utilise SHA-1 pour le hachage (peut être modifié selon vos besoins)
	        digest = MessageDigest.getInstance("SHA-1");
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	        return null;
	    }

	    digest.update(password.getBytes());
	    byte[] hash = digest.digest();

	    // Convertit le tableau de bytes en une chaîne hexadécimale pour une visualisation plus facile
	    StringBuilder sb = new StringBuilder();
	    for (byte b : hash) {
	        sb.append(String.format("%02x", b));
	    }

	    // Tronque pour obtenir uniquement les 16 premiers bytes (128 bits) du hachage
	    return sb.toString().substring(0, 32);
	}

	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
	//Initialisation de la communication avec le client :
		Connect();
		System.out.println("Server waiting for client");
		ServerSocket socket;
		socket = new ServerSocket(9803);
		Socket soc = socket.accept();
		System.out.println("Connection established");
		BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
		PrintWriter out = new PrintWriter(soc.getOutputStream(), true);
		
		
		
		
		String id = in.readLine();
		out.println(id);
		String type = in.readLine();
		out.println(type);
		PreparedStatement checkUserStmt = con.prepareStatement("SELECT id, email FROM clients WHERE id = ?");
        checkUserStmt.setString(1, id);
        ResultSet userResultSet = checkUserStmt.executeQuery();
		
		
		
		if (userResultSet.next()) {   //recuperation de l'id
            String userId = userResultSet.getString("id");
            String userEmail = userResultSet.getString("email");

            PreparedStatement checkAccountStmt = con.prepareStatement("SELECT type, pin, statut FROM comptes WHERE id = ?");
            checkAccountStmt.setString(1, userId);
            ResultSet accountResultSet = checkAccountStmt.executeQuery();

            if (accountResultSet.next()) {    //recuperation de pin
                String userType = accountResultSet.getString("type");
                String userPIN = accountResultSet.getString("pin");
                String userStatus = accountResultSet.getString("statut");
                out.println(userStatus);
                
                if (type.equals(userType) && (id.equals(userId)) && userType.equals("admin")) {
                	
                		//go to form
                		// Récupération de la clé publique du serveur pour la stocker dans la base de donnée
                        String str = in.readLine();
                        BigInteger PublicMod = new BigInteger(str);   //****************************************SERVER************************************************************8
                        out.println("Server public Modulus: "+str);
                        str = in.readLine();
                        BigInteger PublicExpo = new BigInteger(str);   //*******************************************SERVER************************************************************
                        out.println("Server public Exponent: "+str);

                        int cmpt = 0;
                        checkUserStmt = con.prepareStatement("SELECT cmpt FROM cmpt WHERE idadmin = ?");
                        checkUserStmt.setString(1, userId);
                        userResultSet = checkUserStmt.executeQuery();
                        if (userResultSet.next()) { 
                			cmpt = userResultSet.getInt("cmpt");
                			out.println(cmpt);
                        
                			if (cmpt == 0) {
                				String query3 = "UPDATE comptes SET clépublique_mod = ?, clépublique_exp = ? WHERE id = ?";
                				checkUserStmt = con.prepareStatement(query3);
                				checkUserStmt.setString(1, PublicMod.toString());
                				checkUserStmt.setString(2, PublicExpo.toString());
                				checkUserStmt.setString(3, userId);
                				checkUserStmt.executeUpdate();


                				cmpt++;
                				String updateQuery = "UPDATE cmpt SET cmpt = ? WHERE idadmin = ?";
                				checkUserStmt = con.prepareStatement(updateQuery);
                				checkUserStmt.setInt(1, cmpt);
                				checkUserStmt.setString(2, userId);
                				checkUserStmt.executeUpdate();
                				
                				
                			}
                			
                			    checkUserStmt = con.prepareStatement("SELECT privmod, privexp FROM cmpt WHERE idadmin = ?");
                			    checkUserStmt.setString(1, userId);
                			    userResultSet = checkUserStmt.executeQuery();
                			    if (userResultSet.next()) { 
  
                   			        BigInteger serverPrivateExponent = new BigInteger(in.readLine());
                   			        out.println(serverPrivateExponent);
                   			        BigInteger serverPrivateModulus = new BigInteger(in.readLine());
                   			        out.println(serverPrivateModulus);
                   			        byte[] sessionKey = chanelopened(soc, in, out, serverPrivateModulus, serverPrivateExponent, PublicMod, PublicExpo);
                   			        // Création d'une clé AES avec la clé masquée K obtenue :
                   					SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

                   			        // Créer une instance de Cipher pour le chiffrement AES
                   			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
                   			        // Initialiser le Cipher en mode déchiffrement avec la clé
                   			        cipher.init(Cipher.DECRYPT_MODE, secretKey);

                   			        // déchiffrer la signature obtenue :
                   			        BigInteger passwordBigInt = new BigInteger(in.readLine());
                   			        
                   			        byte[] passwordBytes = cipher.doFinal(bigIntegerToBytes(passwordBigInt));
                   			        String password = byteArrayToString(passwordBytes);
                   			        if(password.equals(userPIN)){
                   			        	out.println("mot de passe correct !");
                  
                   			        }
                                    formServer frame = new formServer(userId, socket, soc, in, out, serverPrivateModulus, serverPrivateExponent);
                                    sendEmailcon(userEmail);
                                    frame.setVisible(true);
                			    
                			    
                			    }       
                        } 
                }else {
                	if((type.equals(userType) && (id.equals(userId)) && userType.equals("user"))) {   //connecter comme user
                		//recuperer les cle pub du client
                		
                		 checkUserStmt = con.prepareStatement("SELECT clépublique_mod, clépublique_exp FROM comptes WHERE id = ?");
         			    checkUserStmt.setString(1, userId);
         			    userResultSet = checkUserStmt.executeQuery();
         			    String test = in.readLine();
         			    if(test.equals("1")){
         			    if (userResultSet.next()) { 
         			        String clientpubmod = userResultSet.getString("clépublique_mod");
         			        String clientpubexp = userResultSet.getString("clépublique_exp");
         			        BigInteger publicMod = new BigInteger(clientpubmod);
         			        BigInteger publicExpo = new BigInteger(clientpubexp);
         			        //envoi du cle du client
         			        out.println(clientpubmod);
         			        out.println(clientpubexp);
         			        
         			       // recupirer les cle pub de serveur
         			        
         			        
         			       checkUserStmt = con.prepareStatement("SELECT clépublique_mod, clépublique_exp FROM comptes WHERE type = ?");
            			    checkUserStmt.setString(1, "admin");
            			    userResultSet = checkUserStmt.executeQuery();
            			    if (userResultSet.next()) { 
             			        String serverpubmod = userResultSet.getString("clépublique_mod");
             			        String serverpubexp = userResultSet.getString("clépublique_exp");
             			        //envoi du cle du client
             			        out.println(serverpubmod);
             			        out.println(serverpubexp);
             			    
           			        
           			        BigInteger serverPrivateExponent = new BigInteger(in.readLine());
           			        out.println(serverPrivateExponent);
           			        BigInteger serverPrivateModulus = new BigInteger(in.readLine());
           			        out.println(serverPrivateModulus);
           			        byte[] sessionKey = chanelopened(soc, in, out, serverPrivateModulus, serverPrivateExponent, publicMod, publicExpo);
           			        // Création d'une clé AES avec la clé masquée K obtenue :
           					SecretKeySpec secretKey = new SecretKeySpec(sessionKey, "AES");

           			        // Créer une instance de Cipher pour le chiffrement AES
           			        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
           			        // Initialiser le Cipher en mode déchiffrement avec la clé
           			        cipher.init(Cipher.DECRYPT_MODE, secretKey);

           			        // déchiffrer la signature obtenue :
           			        BigInteger passwordBigInt = new BigInteger(in.readLine());
           			        
           			        byte[] passwordBytes = cipher.doFinal(bigIntegerToBytes(passwordBigInt));
           			        String password = byteArrayToString(passwordBytes);
           			        if(password.equals(userPIN)){
           			        	out.println("mot de passe correct !");
           			        	sendEmailcon(userEmail);
                			    bienvenueserver bienv = new bienvenueserver(userId, socket, soc, in, out, serverPrivateModulus, serverPrivateExponent, sessionKey);
           			        }
           			        
            			    }       

         			    }
                		}
                		}
                	}
                } 
            }           
		}	
	
	//methods
	   // Méthode pour insérer une entrée dans l'historique de connexion
 private static void insertIntoHistory(String userId, boolean success) {
     try {
         InetAddress localhost = InetAddress.getLocalHost();
         String ipAddress = localhost.getHostAddress();

         String query = "INSERT INTO historique(id, idconnexion, dateetheure, adresseip, résultat) VALUES (?, ?, ?, ?, ?)";
         PreparedStatement pstmt = con.prepareStatement(query);
         pstmt.setString(1, userId);
         pstmt.setString(2, userId);
         pstmt.setTimestamp(3, new Timestamp(new Date().getTime()));
         pstmt.setString(4, ipAddress); 
         pstmt.setString(5, success ? "réussie" : "échec");
         pstmt.executeUpdate();
     } catch (Exception ex) {
         ex.printStackTrace();
     }
 }
 
 // Méthode pour bloquer le compte
 private void blockAccount(int userId) {
     try {
         PreparedStatement blockAccountStmt = con.prepareStatement("UPDATE comptes SET statut = ? WHERE id = ?");
         blockAccountStmt.setString(1, "bloqué");
         blockAccountStmt.setInt(2, userId);
         blockAccountStmt.executeUpdate();
     } catch (Exception ex) {
         ex.printStackTrace();
     }
 }
 
 // Méthode pour envoyer un e-mail de notification de connexion
 private static void sendEmailcon(String userEmail) {
     final String username = "esibanque@gmail.com"; 
     final String password = "bqfc iacn zfpj fcyt";
     
     try {
     	InetAddress localhost = InetAddress.getLocalHost();
         String ipAddress = localhost.getHostAddress();
         
         
         Properties prop = new Properties();
         prop.put("mail.smtp.auth", "true");
         prop.put("mail.smtp.starttls.enable", "true");
         prop.put("mail.smtp.host", "smtp.gmail.com");
         prop.put("mail.smtp.port", "587");

         Session session = Session.getInstance(prop,
             new javax.mail.Authenticator() {
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(username, password);
                 }
             });

         try {
             Message message = new MimeMessage(session);
             message.setFrom(new InternetAddress(username));
             message.setRecipients(
                 Message.RecipientType.TO,
                 InternetAddress.parse(userEmail)
             );
             message.setSubject("Nouvelle connexion à votre compte bancaire");
             
             // Obtention de la date et de l'heure actuelles
             Date date = new Date();
             
             // Obtention du nom d'hôte associé à l'adresse IP
             String hostName = "";
             try {
                 InetAddress addr = InetAddress.getByName(ipAddress);
                 hostName = addr.getHostName();
             } catch (UnknownHostException ex) {
                 hostName = "Inconnu";
             }

             // Contenu du message
             String messageContent = "Une nouvelle connexion a été détectée sur votre compte bancaire.\n\n"
                                     + "Date et heure: " + date.toString() + "\n"
                                     + "Adresse IP: " + ipAddress + "\n"
                                     + "Nom d'hôte: " + hostName + "\n\n"
                                     + "Si cette connexion ne vous semble pas familière, veuillez contacter l'administrateur de la banque.";

             message.setText(messageContent);

             Transport.send(message);

             System.out.println("E-mail envoyé avec succès.");

         } catch (MessagingException e) {
             e.printStackTrace();
         }
     } catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     
 }
 
 // Méthode pour envoyer un e-mail en cas de blocage de compte
 private void sendEmail(String userEmail) {
     final String username = "esibanque@gmail.com"; 
     final String password = "bqfc iacn zfpj fcyt";

     Properties prop = new Properties();
     prop.put("mail.smtp.auth", "true");
     prop.put("mail.smtp.starttls.enable", "true");
     prop.put("mail.smtp.host", "smtp.gmail.com");
     prop.put("mail.smtp.port", "587");

     Session session = Session.getInstance(prop,
         new javax.mail.Authenticator() {
             protected PasswordAuthentication getPasswordAuthentication() {
                 return new PasswordAuthentication(username, password);
             }
         });

     try {
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(username));
         message.setRecipients(
             Message.RecipientType.TO,
             InternetAddress.parse(userEmail)
         );
         message.setSubject("Compte bloqué");
         message.setText("Votre compte a été bloqué en raison de plusieurs tentatives de connexion infructueuses.");

         Transport.send(message);

         System.out.println("E-mail envoyé avec succès.");

     } catch (MessagingException e) {
         e.printStackTrace();
     }
 }

	 public static RSAPrivateKey buildPrivateKey(BigInteger modulus, BigInteger privateExponent) throws Exception {
	        RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);
	        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	        return (RSAPrivateKey) keyFactory.generatePrivate(privateKeySpec);
	    }
	    
	    /* Classe contentant une méthode pour construire une clé public RSA (utilisée pour
	    reconstruire la clé publique du client dans la vérification de la signature)*/
	    
	        public static RSAPublicKey buildPublicKey(BigInteger modulus, BigInteger privateExponent) throws Exception {
	            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, privateExponent);
	            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	            return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
	        }
	    
	    public static byte[] chanelopened(Socket soc, BufferedReader in, PrintWriter out, BigInteger serverPrivateModulus, BigInteger serverPrivateExponent, BigInteger publicMod, BigInteger publicExpo) {
	    	
	    	// Récupération des paramètres DH auprès du client ( Y, P, G) :
	        byte[] maskedK = new byte[128];
			try {
				String str = in.readLine();
				BigInteger y = new BigInteger(str);
		        byte[] yBytes = bigIntegerToBytes(y);
		        out.println("Y: "+y.toString(16));
		        str = in.readLine();
		        BigInteger p = new BigInteger(str);
		        out.println("P: "+p.toString(16));
		        str = in.readLine();
		        BigInteger g = new BigInteger(str);
		        out.println("G: "+g.toString(16));
		    	
		    	//Calcul aléatoire de n ( 0 < n < P ) n est la clé privée DH du serveur et elle est sur 1024 bits :
			    BigInteger n;
			    SecureRandom secureRandom = new SecureRandom();
		        do {
		            n = new BigInteger(p.bitLength(), secureRandom);
		        } while (n.compareTo(p) > 0);
			    
			    // X = G^n mod P :
			    BigInteger x = g.modPow(n, p);
			    byte[] xBytes = bigIntegerToBytes(x);
			    
			    // Affichage x (1024 bits) :
			    out.println(x.toString());
			    out.println("x : "+x.toString(16));
			    
			    // K = Y^n mod P (1024 bits) :
			    BigInteger K = y.modPow(n, p);
			    byte[] kBytes = bigIntegerToBytes(K);
			    
			    // Affichage de K :
			    out.println(K.toString());
			    out.println("K : "+K.toString(16));
			    
			    // Application du masque à K (résultat sur 128 bits)
		        maskedK = maskFunction(kBytes,16);
		        
		        //Affichage de K :
		        out.println(bytesToBigInteger(maskedK).toString());
		        
		        // Concaténation de X et Y :
		        byte[] concat = new byte[xBytes.length + yBytes.length];
		        System.arraycopy(yBytes, 0, concat, 0, yBytes.length);
		        System.arraycopy(xBytes, 0, concat, yBytes.length, xBytes.length);
		        
		        // Affichage de la concaténation :
		        out.println(bytesToBigInteger(concat).toString());
		        
		        // Initialisation de l'algorithme de signature RSA :
		        Signature signature = Signature.getInstance("SHA256withRSA");
		   
		        // Reconstruction de la clé RSA privée du serveur :
		    
		        RSAPrivateKey serverPrivateKey = buildPrivateKey(serverPrivateModulus, serverPrivateExponent);
		        
		        // Signature de la concaténation avec la clé privée du serveur :
				signature.initSign(serverPrivateKey);
				signature.update(concat);
				byte[] signed = signature.sign();
				
				// Affichage de la signature :
				out.println(bytesToBigInteger(signed).toString());
				
				// Création d'une clé AES avec la clé masquée K obtenue :
				SecretKeySpec secretKey = new SecretKeySpec(maskedK, "AES");

		        // Créer une instance de Cipher pour le chiffrement AES
		        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

		        // Initialiser le Cipher en mode de chiffrement avec la clé
		        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		        // Chiffrer la signature obtenue :
		        byte[] encryptedBytes = cipher.doFinal(signed);
		        
		        // Affichage de la donnée chiffrée :
		        out.println(bytesToBigInteger(encryptedBytes));
		        
				// Initialiser le Cipher en mode déchiffrement avec la clé
		        cipher.init(Cipher.DECRYPT_MODE, secretKey);

		        // déchiffrer la signature obtenue :
		        BigInteger bigsignedclient = new BigInteger(in.readLine());
		        byte[] signedclient = cipher.doFinal(bigIntegerToBytes(bigsignedclient));
		        
		        // Initialisation de l'algorithme de signature RSA :
		        Signature signatureClient = Signature.getInstance("SHA256withRSA");
		     
				// Vérification signature de la concaténation avec la clé publique du client :
		        // Initialisation de la clé privée et publique :
		        
		        RSAPublicKey rsaPublicKey = buildPublicKey(publicMod, publicExpo);
				signatureClient.initVerify(rsaPublicKey);
				signatureClient.update(concat);
				boolean verifClient = signatureClient.verify(signedclient);
				
				// Envoi de la réponse du serveur :
				// Si verifClient est à vrai alors le client est authentifié :
			    out.println(verifClient);
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

	    //Méthode pour convertir un BigInteger en tableau de byte :
	    public static byte[] bigIntegerToBytes(BigInteger bigInteger) {
	    byte[] byteArray = bigInteger.toByteArray();
	    //Si le tableau a une longueur égale à 1 et que la première valeur est ègale à 0, c'est-à-dire que le BigInteger est zéro ou négatif, nous devons supprimer le signe.
	    if (byteArray.length > 1 && byteArray[0] == 0) {
	    byte[] trimmedArray = new byte[byteArray.length - 1];
	    System.arraycopy(byteArray, 1, trimmedArray, 0, trimmedArray.length);
	    return trimmedArray;
	    }
	    return byteArray;
	    }

	    //Méthode pour convertir un tableau de byte en BigInteger :
	    public static BigInteger bytesToBigInteger(byte[] byteArray) {
	    //Si le tableau de bytes est vide, retourner BigInteger.ZERO
	    if (byteArray.length == 0) {
	    return BigInteger.ZERO;
	    }
	    return new BigInteger(1, byteArray); // Le "1" signifie que le tableau de bytes est positif ou nul
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

}
