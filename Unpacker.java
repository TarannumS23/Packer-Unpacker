import java.io.*;

class Unpacker
{
    public void unpack(String PackFileName, String OutputFolder) throws Exception
    {
        File fpackobj = null;
        FileInputStream fiobj = null;
        FileOutputStream foobj = null;

        byte Header[] = new byte[100];
        byte Buffer[] = null;

        int iRet = 0;

        String strHeadr = null;
        String Tokens[] = null;
        File NewFile = null;

        fpackobj = new File(PackFileName);

        if(fpackobj.exists())
        {
            fiobj = new FileInputStream(fpackobj);

            // Read header
            while((iRet = fiobj.read(Header, 0, 100)) != -1)
            {
                // Header should always contain 100 bytes
                if(iRet != 100)
                {
                    throw new IOException("Invalid packed file");
                }

                strHeadr = new String(Header);

                strHeadr = strHeadr.trim();
                strHeadr = strHeadr.replaceAll("\\s+", " ");

                Tokens = strHeadr.split(" ");

                if(Tokens.length < 2)
                {
                    throw new IOException("Invalid header");
                }

                System.out.println("File Name : " + Tokens[0]);
                System.out.println("File Size : " + Tokens[1]);

                // Create file inside selected destination folder
                NewFile = new File(OutputFolder, Tokens[0]);

                NewFile.createNewFile();

                foobj = new FileOutputStream(NewFile);

                int FileSize = Integer.parseInt(Tokens[1]);

                Buffer = new byte[FileSize];

                // Read data
                int TotalRead = 0;

                while(TotalRead < FileSize)
                {
                    int Read = fiobj.read(
                            Buffer,
                            TotalRead,
                            FileSize - TotalRead
                    );

                    if(Read == -1)
                    {
                        throw new IOException(
                                "Unexpected end of packed file"
                        );
                    }

                    TotalRead = TotalRead + Read;
                }

                // Write data
                foobj.write(Buffer, 0, FileSize);

                foobj.close();

                Header = new byte[100];
            }

            fiobj.close();
        }
        else
        {
            throw new IOException("There is no such pack file");
        }
    }
}