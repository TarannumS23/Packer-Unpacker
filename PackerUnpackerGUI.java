import javax.swing.*;
import java.awt.*;
import java.io.*;

class PackerUnpackerGUI extends JFrame
{
    JLabel TitleLabel;
    JButton PackButton;
    JButton UnpackButton;
    JTextArea StatusArea;

    PackerUnpackerGUI()
    {
        setTitle("Packer - Unpacker");
        setSize(600, 450);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        TitleLabel = new JLabel("PACKER - UNPACKER");
        TitleLabel.setFont(new Font("Arial", Font.BOLD, 24));

        TitleLabel.setBounds(170, 30, 300, 40);

        add(TitleLabel);

        PackButton = new JButton("PACK FILES");
        PackButton.setFont(new Font("Arial", Font.BOLD, 16));

        PackButton.setBounds(170, 100, 250, 55);

        add(PackButton);

        UnpackButton = new JButton("UNPACK FILE");
        UnpackButton.setFont(new Font("Arial", Font.BOLD, 16));

        UnpackButton.setBounds(170, 175, 250, 55);

        add(UnpackButton);

        JLabel StatusLabel = new JLabel("Status :");

        StatusLabel.setBounds(50, 255, 100, 30);

        add(StatusLabel);

        StatusArea = new JTextArea();

        StatusArea.setEditable(false);
        StatusArea.setLineWrap(true);
        StatusArea.setWrapStyleWord(true);

        JScrollPane ScrollPane =
                new JScrollPane(StatusArea);

        ScrollPane.setBounds(50, 285, 500, 90);

        add(ScrollPane);

        // Pack button
        PackButton.addActionListener(e -> packFiles());

        // Unpack button
        UnpackButton.addActionListener(e -> unpackFile());

        setVisible(true);
    }

    public void packFiles()
    {
        JFileChooser chooser = new JFileChooser();

        chooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );

        int Result = chooser.showOpenDialog(this);

        if(Result != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File Folder = chooser.getSelectedFile();

        JFileChooser SaveChooser =
                new JFileChooser();

        SaveChooser.setDialogTitle(
                "Select location for packed file"
        );

        int SaveResult =
                SaveChooser.showSaveDialog(this);

        if(SaveResult != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File PackFile =
                SaveChooser.getSelectedFile();

        String Password =
                getPassword();

        if(Password == null)
        {
            return;
        }

        File TempFile = null;

        try
        {
            TempFile = File.createTempFile(
                    "packed_",
                    ".tmp"
            );

            StatusArea.setText(
                    "Packing files...\n"
            );

            Packer PackerObj = new Packer();

            PackerObj.pack(
                    Folder.getAbsolutePath(),
                    TempFile.getAbsolutePath()
            );

            StatusArea.append(
                    "Packing completed.\n"
            );

            StatusArea.append(
                    "Encrypting packed file...\n"
            );

            Encryption.encryptFile(
                    TempFile.getAbsolutePath(),
                    PackFile.getAbsolutePath(),
                    Password
            );

            TempFile.delete();

            StatusArea.append(
                    "Encryption completed.\n"
            );

            StatusArea.append(
                    "Packed file created successfully."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Packing and Encryption Successful!"
            );
        }
        catch(Exception ex)
        {
            StatusArea.setText(
                    "Error : " + ex.getMessage()
            );

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            if(TempFile != null)
            {
                TempFile.delete();
            }
        }
    }

    public void unpackFile()
    {
        JFileChooser chooser =
                new JFileChooser();

        chooser.setDialogTitle(
                "Select encrypted packed file"
        );

        int Result =
                chooser.showOpenDialog(this);

        if(Result != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File PackFile =
                chooser.getSelectedFile();

        String Password =
                getPassword();

        if(Password == null)
        {
            return;
        }

        JFileChooser FolderChooser =
                new JFileChooser();

        FolderChooser.setDialogTitle(
                "Select destination folder"
        );

        FolderChooser.setFileSelectionMode(
                JFileChooser.DIRECTORIES_ONLY
        );

        int FolderResult =
                FolderChooser.showOpenDialog(this);

        if(FolderResult != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File DestinationFolder =
                FolderChooser.getSelectedFile();

        File TempFile = null;

        String OldDirectory =
                System.getProperty("user.dir");

        try
        {
            TempFile = File.createTempFile(
                    "decrypted_",
                    ".tmp"
            );

            StatusArea.setText(
                    "Decrypting packed file...\n"
            );

            Encryption.decryptFile(
                    PackFile.getAbsolutePath(),
                    TempFile.getAbsolutePath(),
                    Password
            );

            StatusArea.append(
                    "Decryption completed.\n"
            );

            /*
             * Unpacker creates extracted files
             * in the current working directory.
             *
             * Therefore temporarily change the
             * working directory.
             */
            System.setProperty(
                    "user.dir",
                    DestinationFolder.getAbsolutePath()
            );

            StatusArea.append(
                    "Extracting files...\n"
            );

            Unpacker UnpackerObj =
                    new Unpacker();

            UnpackerObj.unpack(
                    TempFile.getAbsolutePath(),
                    DestinationFolder.getAbsolutePath()
            );

            System.setProperty(
                    "user.dir",
                    OldDirectory
            );

            TempFile.delete();

            StatusArea.append(
                    "Extraction completed successfully."
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Unpacking and Decryption Successful!"
            );
        }
        catch(Exception ex)
        {
            System.setProperty(
                    "user.dir",
                    OldDirectory
            );

            StatusArea.setText(
                    "Error : " + ex.getMessage()
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Wrong password or invalid packed file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );

            if(TempFile != null)
            {
                TempFile.delete();
            }
        }
    }

    public String getPassword()
    {
        JPasswordField PasswordField =
                new JPasswordField();

        int Result =
                JOptionPane.showConfirmDialog(
                        this,
                        PasswordField,
                        "Enter Password",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if(Result == JOptionPane.OK_OPTION)
        {
            return new String(
                    PasswordField.getPassword()
            );
        }

        return null;
    }

    public static void main(String A[])
    {
        new PackerUnpackerGUI();
    }
}