# Packer-Unpacker with GUI & Encryption

## Overview

Packer-Unpacker is a Java desktop application that combines multiple files into a single packed file and restores them back when required. The application provides a Swing-based graphical user interface and supports password-protected AES-GCM encryption for secure file storage and extraction.

## Features

* Pack multiple files into a single file
* Unpack files from a packed file
* Java Swing based GUI
* Password-protected encryption and decryption
* AES-GCM encryption for security
* File metadata stored using a fixed 100-byte header
* Simple and user-friendly interface

## Technologies Used

* Java
* Java Swing
* Java File I/O
* AES-GCM Encryption
* PBKDF2 Key Derivation

## Project Structure

```text
Packer-Unpacker/
│
├── Packer.java
├── Unpacker.java
├── Encryption.java
├── PackerUnpackerGUI.java
├── README.md
└── .gitignore
```

## How It Works

### Packing Process

1. Select a folder containing files.
2. Create a packed file containing:

   * File Name
   * File Size
   * File Data
3. Encrypt the packed file using AES-GCM.

### Unpacking Process

1. Select the encrypted packed file.
2. Enter the correct password.
3. Decrypt the packed file.
4. Extract all original files.

## Compilation

```bash
javac *.java
```

## Run

```bash
java PackerUnpackerGUI
```

## Future Enhancements

* Progress bar for large files
* Drag-and-drop support

## Author

Tarannum Jakirhusen Shaikh

