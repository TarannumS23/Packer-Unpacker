import java.io.*;

class Packer
{
    public void pack(String FolderName, String PackFileName) throws IOException
    {
        int iRet = 0;
        int Size = 0;
        int i = 0, j = 0;

        String header = "";

        FileOutputStream foobj = null;
        FileInputStream fiobj = null;

        byte Buffer[] = new byte[1024];
        byte bHeader[] = null;

        File fobjfolder = new File(FolderName);

        if((fobjfolder.exists()) && (fobjfolder.isDirectory()))
        {
            File fobjpack = new File(PackFileName);

            foobj = new FileOutputStream(fobjpack);

            File fArr[] = fobjfolder.listFiles();

            for(i = 0; i < fArr.length; i++)
            {
                // Ignore directories
                if(!fArr[i].isFile())
                {
                    continue;
                }

                fiobj = new FileInputStream(fArr[i]);

                header = header + fArr[i].getName();
                header = header + " ";
                header = header + fArr[i].length();

                Size = 100 - header.length();

                for(j = 1; j <= Size; j++)
                {
                    header = header + " ";
                }

                bHeader = header.getBytes();

                // Write file name and size
                foobj.write(bHeader);

                // Read file data and write it into packed file
                while((iRet = fiobj.read(Buffer)) != -1)
                {
                    foobj.write(Buffer, 0, iRet);
                }

                fiobj.close();

                header = "";
            }

            foobj.close();
        }
        else
        {
            throw new IOException("There is no such folder");
        }
    }
}