package Services;

import Exceptions.CustomException;
import Models.DataBaseModel;
import Views.OutputView;
import org.jetbrains.annotations.NotNull;

import javax.xml.crypto.Data;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Logic of database (Run, delete, add)
 * Login, Logout
 */

public class DataBaseService {

    private Connection Session = null;
    private final DataBaseModel DBModel;
    private final UserService UService;

    private final String url;
    private final String login;
    private final String password;


    public DataBaseService(DataBaseModel DBModel, UserService UService) {
        this.DBModel = DBModel;
        this.UService = UService;
        this.url = DBModel.GetUrl();
        this.login = DBModel.GetLogin();
        this.password = DBModel.GetPassword();
    }

    /**
     * Setup connection, test connection
     */
    public void SetupDataBaseConnection() {
        try {
            Connect();
            SQL_TestConnectivity();
            Disconnect();
        } catch (SQLException | ClassNotFoundException e) {
            System.exit(1);
        }
    }

    /**
     * Search for driver and connect using local url, login and pwd
     */
    public void Connect() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Session = DriverManager.getConnection(this.url, this.login, this.password);
    }

    /**
     * Test connection with database using query
     */
    private void SQL_TestConnectivity() throws SQLException {
        String query = "SELECT * FROM user;";
        Session.createStatement().executeQuery(query);
    }

    /**
     * Disconnect from database
     */
    public void Disconnect() throws SQLException {
        if (Session != null) {
            Session.close();
            Session = null;
        }
    }

    /**
     * SignUp method
     * 1) Must connect to database
     * 2) Must check any occupied logins
     * 3) If free then SignUp and add login into database
     */
    public void SignUp(String login, String password) throws SQLException, ClassNotFoundException, CustomException {
        Connect();
        SQL_Check_LoginAvailable(login);
        SQL_SignUp(login, password);
        SignInLocally(login);
        Disconnect();
    }

    /**
     * Login checker
     */
    private void SQL_Check_LoginAvailable(String login) throws SQLException, CustomException {
        String query = "SELECT * FROM user WHERE UserName='" + login + "';";
        ResultSet rt = Session.createStatement().executeQuery(query);
        if (rt.next()) throw new CustomException("login already exist");
    }

    /**
     * Add login into database
     */
    private void SQL_SignUp(String login, String password) throws SQLException {
        String query = "INSERT INTO user(UserName, Password) VALUES ('" + login + "', '" + password + "');";
        Session.createStatement().executeUpdate(query);
    }

    /**
     * SignIn method
     * 1) Must connect to database
     * 2) Must check any occupied logins
     * 3) If not free and password fits then SignIn
     */
    public void SignIn(String login, String password) throws SQLException, ClassNotFoundException, CustomException {
        Connect();
        SQL_Check_UserExist(login, password);
        SignInLocally(login);
        Disconnect();
    }

    /**
     * Login checker
     */
    private void SQL_Check_UserExist(String login, String password) throws SQLException, CustomException {
        String query = "SELECT * FROM user WHERE UserName='" + login + "' AND Password='" + password + "';";
        ResultSet rt = Session.createStatement().executeQuery(query);
        if (!rt.isBeforeFirst()) {
            throw new CustomException("Login or Password Incorrect");
        }
    }

    /**
     * SignIn and change login status
     */
    private void SignInLocally(String login) {
        UService.SignIn(login);
    }

    /**
     * Returns list of all files
     */
    public String @NotNull [] GetMediaList() throws SQLException, ClassNotFoundException {
        String[] mediaList;
        Connect();
        mediaList = SQL_GetMediaList();
        Disconnect();
        return mediaList;
    }


    /**
     * Get files list using query
     */
    private String @NotNull [] SQL_GetMediaList() throws SQLException {
        String query = "SELECT FileName FROM media;";
        ResultSet dataSet = Session.createStatement().executeQuery(query);
        return ConvertMediaListToStringArray(dataSet);
    }

    /**
     * Gets dataset and extracts filenames from it then converts to string array
     */
    private String @NotNull [] ConvertMediaListToStringArray(@NotNull ResultSet DataSet) throws SQLException {
        List<String> dataList = new ArrayList<>();
        while (DataSet.next()) {
            dataList.add(DataSet.getString(1));
        }

        String[] dataStringArray = new String[dataList.size()];
        dataList.toArray(dataStringArray);
        return dataStringArray;
    }

    /**
     * Get list of file parameters
     */
    public String @NotNull [] GetMediaDescription(String itemName) throws SQLException, ClassNotFoundException {
        Connect();
        String[] ItemDescription = SQL_GetItemDescription(itemName);
        Disconnect();
        return ItemDescription;
    }

    /**
     * Get parameters using query
     */
    private String @NotNull [] SQL_GetItemDescription(String itemName) throws SQLException {
        String query = "SELECT AddedBy, UploadDate, Location FROM media WHERE FileName='" + itemName + "';";
        ResultSet dataSet = Session.createStatement().executeQuery(query);
        return ConvertItemDescriptionToStringArray(dataSet);
    }

    public String getAddedBy(String itemName) throws SQLException, ClassNotFoundException {
        String user = null;
        Connect();
        String query = "SELECT AddedBy FROM media WHERE FileName='" + itemName + "';";
        ResultSet dataSet = Session.createStatement().executeQuery(query);
        if (dataSet.next())
            user = dataSet.getString(1);
        Disconnect();
        return user;
    }

    /**
     * Convert parameters list to string array
     */
    private String @NotNull [] ConvertItemDescriptionToStringArray(@NotNull ResultSet dataSet) throws SQLException {
        List<String> dataList = new ArrayList<>();
        int i = 1;
        if (dataSet.next()) {
            int dataSetSize = dataSet.getMetaData().getColumnCount();
            while (i <= dataSetSize) {
                dataList.add(dataSet.getString(i++));
            }

            String[] dataStringArray = new String[dataList.size()];
            dataList.toArray(dataStringArray);
            return dataStringArray;
        }
        return new String[4];
    }

    /**
     * Open file
     */
    public void RunMedia(String mediaName) throws SQLException, ClassNotFoundException, IOException {
        Connect();
        Desktop myDesktop = Desktop.getDesktop();
        myDesktop.open(new File(SQL_GetMediaPath(mediaName)));
        Disconnect();
    }

    /**
     * Get file path
     */
    private String SQL_GetMediaPath(String mediaName) throws SQLException {
        String query = "SELECT Location FROM media where FileName = '" + mediaName + "';";
        ResultSet dataset = Session.createStatement().executeQuery(query);
        dataset.next();
        return dataset.getString(1);
    }

    /**
     * Add file
     */
    public void AddMedia(String[] mediaData) throws SQLException, ClassNotFoundException, IOException {
        Connect();
        SQL_AddMedia(mediaData);
        Disconnect();
    }

    /*
    Add file to database using query
     */
    private void SQL_AddMedia(String @NotNull [] mediaData) throws SQLException, IOException {
        File file = new File(mediaData[3]);
        try (FileInputStream fis = new FileInputStream(file); PreparedStatement ps = Session.prepareStatement
                ("INSERT INTO media(FileName, Data, AddedBy, UploadDate, Location) VALUES ('" + mediaData[0] + "',?, '" +
                         mediaData[1] + "'," +
                         " '" + mediaData[2] + "', '" + mediaData[3] + "');")) {
            ps.setBinaryStream(1, fis, (int) file.length());
            ps.executeUpdate();
        }
    }

    //    public String EditMedia(String oldName, String newName)
//            throws SQLException, ClassNotFoundException, IOException, RenamedFileException {
//        Connect();
//        int mediaID = SQL_GetMediaID(oldName);
//        String fileExtension = oldName.substring(oldName.lastIndexOf('.'));
//        newName = newName.concat(fileExtension);
//        String oldPath = SQL_GetMediaPath(oldName);
//        String newPath = oldPath.replace(oldName, newName).replaceAll("\\\\","\\\\\\\\");
//        SQL_EditMedia(mediaID, newName, newPath);
//        if (!MediaRenamed(oldPath, newPath))
//            throw new RenamedFileException("An error occurred while renaming the selected file");
//        Disconnect();
//        return newName;
//    }
    private int SQL_GetMediaID(String mediaName) throws SQLException {
        String query = "SELECT ID FROM media WHERE FileName = '" + mediaName + "';";
        ResultSet dataSet = Session.createStatement().executeQuery(query);
        dataSet.next();
        return dataSet.getInt(1);
    }
//    private void SQL_EditMedia(int mediaID, String newMediaName, String newMediaPath) throws SQLException {
//        Session.createStatement().executeUpdate(
//                "UPDATE media SET FileName ='" + newMediaName + "', Location ='" + newMediaPath + "' WHERE ID='" + mediaID +"';"
//        );
//    }
//    private boolean MediaRenamed(String oldMediaPath, String newMediaPath) {
//        File selectedFile = new File(oldMediaPath);
//        return selectedFile.renameTo(new File(newMediaPath));
//    }

    /**
     * Delete file
     */
    public void DeleteMedia(String mediaName, String login) throws SQLException, ClassNotFoundException, CustomException {
        Connect();
        SQL_DeleteMedia(mediaName, login);
        Disconnect();
    }

    /**
     * Delete file from database using query
     */
    private void SQL_DeleteMedia(String mediaName, String login) throws SQLException, CustomException {
        String queryUser = "SELECT * FROM media WHERE FileName='" + mediaName + "';";
        ResultSet rt = Session.createStatement().executeQuery(queryUser);
        if (rt.next()) {
            if (!Objects.equals(rt.getString("AddedBy"), login)) {
                throw new CustomException("U do not have permissions to do this");
            } else {
                String query = "DELETE FROM media WHERE FileName='" + mediaName + "';";
                Session.createStatement().executeUpdate(query);
            }
        }


    }


}
